package org.yk.example.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableBatchProcessing
public class SpringBatchExamplesApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringBatchExamplesApplication.class, args);
	}
}
