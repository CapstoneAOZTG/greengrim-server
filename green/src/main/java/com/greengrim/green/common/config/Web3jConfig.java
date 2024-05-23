package com.greengrim.green.common.config;

import com.greengrim.green.common.web3j.Abi;
import java.math.BigInteger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.ContractGasProvider;
import org.web3j.tx.gas.StaticGasProvider;

@Configuration
public class Web3jConfig {

    @Value("${spring.web3.networkURL}")
    private String networkURL;
    @Value("${spring.web3.contractAddress}")
    private String contractAddress;
    @Value("${spring.web3.privateKey}")
    private String privateKey;
    @Value("${spring.web3.networkID}")
    private long networkId;

    private static final BigInteger GAS_PRICE = new BigInteger("50000000000");
    private static final BigInteger GAS_LIMIT = BigInteger.valueOf(1000000);

    @Bean
    public Abi contract() {
        Credentials credentials = Credentials.create(privateKey);
        Web3j web3j = Web3j.build(new HttpService(networkURL));
        TransactionManager txManager = new RawTransactionManager(web3j, credentials, networkId);
        ContractGasProvider gasProvider = new StaticGasProvider(GAS_PRICE, GAS_LIMIT);

        return Abi.load(contractAddress, web3j, txManager, gasProvider);
    }

}
