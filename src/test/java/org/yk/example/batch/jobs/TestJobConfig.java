package org.yk.example.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author yongkyu.lee@navercorp.com
 */
@Configuration
public class TestJobConfig {
	@Bean
	public JobLauncherTestUtils sampleJobLauncherTestUtils() {
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("sampleJob")Job job) {
				super.setJob(job);
			}
		};
	}

	@Bean
	public JobLauncherTestUtils faultToleranceSkipExampleJobLauncherTestUtils() {
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("faultToleranceSkipExampleJob")Job job) {
				super.setJob(job);
			}
		};
	}

	@Bean
	public JobLauncherTestUtils faultToleranceRetryExampleJobLauncherTestUtils() {
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("faultToleranceRetryExampleJob")Job job) {
				super.setJob(job);
			}
		};
	}

	@Bean
	public JobLauncherTestUtils sequentialFlowJobLauncherTestUtils() {
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("sequentialFlowJob")Job job) {
				super.setJob(job);
			}
		};
	}

	@Bean
	public JobLauncherTestUtils conditionalFlowJobLauncherTestUtils() {
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("conditionalFlowJob")Job job) {
				super.setJob(job);
			}
		};
	}

	@Bean
	public JobLauncherTestUtils splitFlowJobLauncherTestUtils() {
		return new JobLauncherTestUtils() {
			@Override
			@Autowired
			public void setJob(@Qualifier("splitFlowJob")Job job) {
				super.setJob(job);
			}
		};
	}
}
