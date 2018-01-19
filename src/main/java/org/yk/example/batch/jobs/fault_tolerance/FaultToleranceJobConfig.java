package org.yk.example.batch.jobs.fault_tolerance;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yongkyu.lee@navercorp.com
 */
@Configuration
public class FaultToleranceJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	public FaultToleranceJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job faultToleranceSkipExampleJob() {
		return jobBuilderFactory.get("faultToleranceSkipExampleJob")
			.start(faultToleranceSkipExampleStep())
			.build();
	}

	public Step faultToleranceSkipExampleStep() {
		return stepBuilderFactory.get("faultToleranceSkipExampleStep")
			.<Integer, Integer>chunk(5)
			.reader(new FaultToleranceReader())
			.processor(new FaultToleranceProcessor())
			.writer(new FaultToleranceWriter())
			.faultTolerant()
			.skipLimit(100)
			.skip(SkipException.class)
			.listener(new StepCompletionListener())
			.build();
	}

	@Bean
	public Job faultToleranceRetryExampleJob() {
		return jobBuilderFactory.get("faultToleranceRetryExampleJob")
			.start(faultToleranceRetryExampleStep())
			.build();
	}

	public Step faultToleranceRetryExampleStep() {
		return stepBuilderFactory.get("faultToleranceRetryExampleStep")
			.<Integer, Integer>chunk(5)
			.reader(new RetryTestReader())
			.processor(new RetryTestProcessor())
			.writer(new RetryTestWriter())
			.faultTolerant()
			.retryLimit(2)
			.retry(RetryException.class)
			.listener(new StepCompletionListener())
			.build();
	}
}
