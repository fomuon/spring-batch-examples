package org.yk.example.batch.jobs.fault_tolerance;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.yk.example.batch.config.LoggingItemWriter;

import java.util.Arrays;

@Configuration
public class FaultToleranceSkipExampleJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	public FaultToleranceSkipExampleJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job faultToleranceSkipExampleJob() {
		return jobBuilderFactory.get("faultToleranceSkipExampleJob")
			.start(faultToleranceSkipExampleStep())
			.build();
	}

	@Bean
	public Step faultToleranceSkipExampleStep() {
		return stepBuilderFactory.get("faultToleranceSkipExampleStep")
				.<Integer, Integer>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(new LoggingItemWriter<>())
				.faultTolerant()
				.skipLimit(100)
				.skip(Exception.class)
				.build();
	}

	private ItemReader<Integer> reader() {
		return new ListItemReader<>(Arrays.asList(1, 2, 3, 4, 5));
	}

	private ItemProcessor<Integer, Integer> processor() {
		return item -> {
			if (item == 3) {
				throw new Exception("sample exception");
			}
			return item;
		};
	}
}
