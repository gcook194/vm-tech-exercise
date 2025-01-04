package com.virginmoney.transactionlog;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import com.virginmoney.transactionlog.config.propertysource.RsaKeyProperties;
import com.virginmoney.transactionlog.config.propertysource.BasicAuthProperties;

@EnableConfigurationProperties({RsaKeyProperties.class, BasicAuthProperties.class})
@SpringBootApplication
public class TransactionLogApplication {

	public static void main(String[] args) {
		SpringApplication.run(TransactionLogApplication.class, args);
	}

}
