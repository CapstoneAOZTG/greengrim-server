package com.greengrim.green.common.s3;


import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.greengrim.green.common.s3.dto.S3Result;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

@RequiredArgsConstructor
@PropertySource("classpath:application.yml")
@Service
public class S3Service {

    private final AmazonS3Client s3Client;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String region;

    /**
     * 파일 업로드 시 파일명을 난수화하기 위해 random 으로 돌림
     */
    private String createFileName(String fileName) {
        return UUID.randomUUID().toString().concat(getFileExtension(fileName));
    }

    /**
     * file 형식이 잘못된 경우를 확인하기 위해 만들어진 로직
     * 파일 타입과 상관없이 업로드할 수 있게 하기 위해 .의 존재 유무만 판단
     */
    private String getFileExtension(String fileName) {
        try {
            return fileName.substring(fileName.lastIndexOf("."));
        } catch (StringIndexOutOfBoundsException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "잘못된 형식의 파일(" + fileName + ") 입니다.");
        }
    }

    /**
     * MultipartFile 을 S3에 업로드하고 S3Result 로 반환
     */
    public S3Result uploadFile(MultipartFile multipartFile) {
        String fileName = createFileName(multipartFile.getOriginalFilename());

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(multipartFile.getSize());
        objectMetadata.setContentType(multipartFile.getContentType());

        try (InputStream inputStream = multipartFile.getInputStream()) {
            s3Client.putObject(
                    new PutObjectRequest(bucket, fileName, inputStream, objectMetadata)
                            .withCannedAcl(CannedAccessControlList.PublicRead));
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                    "파일 업로드에 실패했습니다.");
        }
        return new S3Result(s3Client.getUrl(bucket, fileName).toString());
    }

    /**
     * MultipartFile 을 S3에 업로드하고 S3Result 로 반환
     */
    public List<S3Result> uploadFiles(List<MultipartFile> multipartFiles) {
        List<S3Result> fileList = new ArrayList<>();

        // forEach 구문을 통해 multipartFile로 넘어온 파일들 하나씩 fileList에 추가
        multipartFiles.forEach(file -> {
            ObjectMetadata objectMetadata = new ObjectMetadata();
            objectMetadata.setContentLength(file.getSize());
            objectMetadata.setContentType(file.getContentType());

            try (InputStream inputStream = file.getInputStream()) {
                s3Client.putObject(
                        new PutObjectRequest(bucket, file.getOriginalFilename(), inputStream, objectMetadata)
                                .withCannedAcl(CannedAccessControlList.PublicRead));
            } catch (IOException e) {
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,
                        "파일 업로드에 실패했습니다.");
            }
            fileList.add(
                    new S3Result(
                            s3Client.getUrl(bucket, file.getOriginalFilename()).toString()));
        });
        return fileList;
    }

    public void deleteFile(String imgUrl) {
        String fileName = parseFileName(imgUrl);
        s3Client.deleteObject(new DeleteObjectRequest(bucket, fileName));
    }

    public String parseFileName(String imgUrl) {
        String[] st = imgUrl.split("/");
        return st[st.length - 1];
    }
}

