package org.yk.example.batch.jobs.fault_tolerance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.StepExecution;
import org.springframework.boot.test.context.SpringBootTest;
import org.yk.example.batch.jobs.AbstractJobTest;

@Slf4j
@SpringBootTest(classes = FaultToleranceRetryExampleJobConfig.class)
public class FaultToleranceRetryExampleJobTest extends AbstractJobTest {

	@Test
	public void faultToleranceRetryExampleJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);

		for (StepExecution stepExecution : jobExecution.getStepExecutions()) {
			log.info(stepExecution.toString());
		}
	}
}