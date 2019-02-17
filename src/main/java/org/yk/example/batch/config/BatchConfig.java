package org.yk.example.batch.config;

import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

	@Bean
	public JobRegistryBeanPostProcessor postProcessor(JobRegistry jobRegistry, BeanFactory beanFactory) {
		JobRegistryBeanPostProcessor processor = new JobRegistryBeanPostProcessor();
		processor.setJobRegistry(jobRegistry);
		processor.setBeanFactory(beanFactory);
		return processor;
	}
}
