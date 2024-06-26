package com.greengrim.green.common.oauth.jwt;

import com.greengrim.green.common.exception.BaseException;
import com.greengrim.green.common.exception.errorCode.AuthErrorCode;
import com.greengrim.green.common.oauth.auth.PrincipalDetails;
import com.greengrim.green.common.oauth.auth.PrincipalDetailsService;
import com.greengrim.green.common.oauth.blacklist.BlackListRedisRepository;
import com.greengrim.green.core.member.dto.MemberResponseDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.SignatureException;
import io.jsonwebtoken.UnsupportedJwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    @Value("${jwt.jwt-key}")
    private String jwtSecretKey;
    @Value("${jwt.refresh-key}")
    private String refreshSecretKey;

    private final PrincipalDetailsService principalDetailsService;

    private final BlackListRedisRepository blackListRedisRepository;
    private static final String AUTHORIZATION_HEADER = "Authorization"; // 액세스 토큰 헤더 key name
    private static final String REFRESH_HEADER = "refreshToken";  // 리프레시 토큰 헤더 key name
    private static final long TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L;  // 유효기간 24시간
    private static final long REF_TOKEN_VALID_TIME = 1000 * 60L * 60L * 24L * 60L;  // 유효기간 2달

    /**
     * 의존성 주입 후 (호출 없어도) 오직 1번만 초기화 수행
     */
    @PostConstruct
    protected void init() {
        jwtSecretKey = Base64.getEncoder().encodeToString(jwtSecretKey.getBytes());
        refreshSecretKey = Base64.getEncoder().encodeToString(refreshSecretKey.getBytes());
    }

    /**
     * memberId가 적힌 클레임을 넘겨 받아 AccessToken 생성
     */
    public String generateAccessToken(Claims claims) {
        Date now = new Date();
        Date accessTokenExpirationTime = new Date(now.getTime() + TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)  // 정보 저장
                .setIssuedAt(now)   // 토큰 발행 시간 정보
                .setExpiration(accessTokenExpirationTime)   // 리프레시 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, jwtSecretKey)   // 전자 서명
                .compact();
    }

    /**
     * memberId가 적힌 클레임을 넘겨 받아 RefreshToken 생성
     */
    public String generateRefreshToken(Claims claims) {
        Date now = new Date();
        Date refreshTokenExpirationTime = new Date(now.getTime() + REF_TOKEN_VALID_TIME);

        return Jwts.builder()
                .setClaims(claims)  // 정보 저장
                .setIssuedAt(now)   // 토큰 발행 시간 정보
                .setExpiration(refreshTokenExpirationTime)  // 리프레시 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, refreshSecretKey)   // 전자 서명
                .compact();
    }

    /**
     * memberId로 AccessToken, RefreshToken 생성 후 리턴
     */
    public MemberResponseDto.TokenInfo generateToken(Long memberId) {
        Claims claims = Jwts.claims();
        claims.put("memberId", memberId);

        String accessToken = generateAccessToken(claims);
        String refreshToken = generateRefreshToken(claims);

        return new MemberResponseDto.TokenInfo(accessToken, refreshToken, memberId);
    }

    /**
     * AccessToken으로 사용자 정보 인증하고 Authentication 객체를 반환하는 함수
     */
    public Authentication getAccessAuthentication(String token) {
        return getAuthentication(getMemberIdByAccessToken(token));
    }

    /**
     * RefreshToken으로 사용자 정보 인증하고 Authentication 객체를 반환하는 함수
     */
    public Authentication getRefreshAuthentication(String token) {
        return getAuthentication(getMemberIdByRefreshToken(token));
    }

    /**
     * 추출한 memberId로 사용자 정보를 인증하고 Authentication 객체를 반환하는 함수
     * @param memberId (String)
     * @return 권한과 사용자 정보를 담은 Authentication 객체
     */
    public Authentication getAuthentication(String memberId) {
        try {
            // 헤더에서 추출한 memberId를 실제 DB에서 조회하여 사용자 정보 확인
            PrincipalDetails principalDetails
                    = principalDetailsService.loadUserByUsername(memberId);
            return new UsernamePasswordAuthenticationToken(principalDetails,
                    "", principalDetails.getAuthorities());
        } catch (UsernameNotFoundException exception) {
            throw new BaseException(AuthErrorCode.UNSUPPORTED_JWT);
        }
    }

    /**
     * AccessToken 을 검증하는 함수
     */
    public void validateAccessToken(String token) {
        validateToken(jwtSecretKey, token);
    }

    /**
     * RefreshToken 을 검증하는 함수
     */
    public void validateRefreshToken(String token) {
        validateToken(refreshSecretKey, token);
    }

    /**
     * 토큰을 검증하는 함수
     * @param key jwtSecretKey, refreshSecretKey 둘 중 하나
     * @param token accessToken, refreshToken 둘 중 하나
     */
    public void validateToken(String key, String token) {
        try {
            Jwts.parser().setSigningKey(key).parseClaimsJws(token);
            if(blackListRedisRepository.existsById(token)) {
                throw new BaseException(AuthErrorCode.LOGOUT_JWT);
            }
        } catch (SecurityException | MalformedJwtException e) {
            throw new BaseException(AuthErrorCode.INVALID_JWT);
        } catch (ExpiredJwtException e) {
            throw new BaseException(AuthErrorCode.EXPIRED_MEMBER_JWT);
        } catch (UnsupportedJwtException | SignatureException e) {
            throw new BaseException(AuthErrorCode.UNSUPPORTED_JWT);
        } catch (IllegalArgumentException e) {
            throw new BaseException(AuthErrorCode.EMPTY_JWT);
        }
    }

    /**
     * AccessToken 에서 memberId를 추출하는 함수
     * @return memberId (String)
     */
    public String getMemberIdByAccessToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecretKey).parseClaimsJws(token).
                getBody().get("memberId").toString();
    }

    /**
     * RefreshToken 에서 memberId를 추출하는 함수
     * @return memberId (String)
     */
    public String getMemberIdByRefreshToken(String token) {
        return Jwts.parser().setSigningKey(refreshSecretKey).parseClaimsJws(token).
                getBody().get("memberId").toString();
    }

    /**
     * 헤더에서 AUTHORIZATION_HEADER key의 value에 해당하는 AccessToken을 추출
     * @return AccessToken (String)
     */
    public String resolveAccessToken(HttpServletRequest request) {
        return request.getHeader(AUTHORIZATION_HEADER);
    }

    /**
     * 헤더에서 REFRESH_HEADER key의 value에 해당하는 RefreshToken을 추출
     * @return RefreshToken (String)
     */
    public String resolveRefreshToken(HttpServletRequest request) {
        return request.getHeader(REFRESH_HEADER);
    }

    /**
     * 해당 token의 남은 시간을 유효 시간으로, 블랙리스트에 토큰 저장
     */
    public void setBlackList(String token) {
        Long expiration = getExpiration(token);
        blackListRedisRepository.save(token, expiration);
    }

    public Long getExpiration(String token) {
        Date expiredDate = extractClaims(token, jwtSecretKey).getExpiration();
        long now = new Date().getTime();
        return (expiredDate.getTime() - now);
    }

    private Claims extractClaims(String token, String secretKey) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


}
