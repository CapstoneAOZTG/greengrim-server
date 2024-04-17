package com.greengrim.green.common.config;

import com.greengrim.green.common.web3j.Abi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.web3j.crypto.Credentials;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.TransactionManager;
import org.web3j.tx.gas.DefaultGasProvider;

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

    public Web3j web3j;

    @Bean
    public Abi contract() {

        Credentials credentials = Credentials.create(privateKey);
        web3j = Web3j.build(new HttpService(networkURL));

        TransactionManager txManager = new RawTransactionManager(web3j, credentials, networkId);
        return Abi.load(contractAddress, web3j, txManager, new DefaultGasProvider());
    }

}
