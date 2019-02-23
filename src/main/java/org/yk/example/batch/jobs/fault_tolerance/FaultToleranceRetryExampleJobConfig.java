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
public class FaultToleranceRetryExampleJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	public FaultToleranceRetryExampleJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job faultToleranceRetryExampleJob() {
		return jobBuilderFactory.get("faultToleranceRetryExampleJob")
			.start(faultToleranceRetryExampleStep())
			.build();
	}

	@Bean
	public Step faultToleranceRetryExampleStep() {
		return stepBuilderFactory.get("faultToleranceRetryExampleStep")
				.<Integer, Integer>chunk(3)
				.reader(reader())
				.processor(processor())
				.writer(new LoggingItemWriter<>())
				.faultTolerant()
				.retryLimit(3)
				.retry(Exception.class)
				.build();
	}

	private ItemReader<Integer> reader() {
		return new ListItemReader<>(Arrays.asList(1, 2, 3, 4, 5));
	}

	private ItemProcessor<Integer, Integer> processor() {

		return new ItemProcessor<Integer, Integer>() {
			private int retryCnt = 0;

			@Override
			public Integer process(Integer item) throws Exception {
				if (item == 4 && retryCnt < 1) {
					retryCnt++;
					throw new Exception("sample exception");
				}

				return item;
			}
		};
	}
}
