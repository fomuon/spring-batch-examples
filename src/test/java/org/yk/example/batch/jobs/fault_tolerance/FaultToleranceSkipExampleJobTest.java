package org.yk.example.batch.jobs.fault_tolerance;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.test.context.SpringBootTest;
import org.yk.example.batch.jobs.AbstractJobTest;

@SpringBootTest(classes = FaultToleranceSkipExampleJobConfig.class)
public class FaultToleranceSkipExampleJobTest extends AbstractJobTest {

	@Test
	public void faultToleranceSkipExampleJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);

	}
}