package org.yk.example.batch.jobs.bean_validation;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.boot.test.context.SpringBootTest;
import org.yk.example.batch.jobs.AbstractJobTest;

@SpringBootTest(classes = BeanValidationJobConfig.class)
public class BeanValidationJobTest extends AbstractJobTest {

	@Test
	public void beanValidationJob() throws Exception {
		JobExecution jobExecution = jobLauncherTestUtils.launchJob();
		Assert.assertEquals(jobExecution.getStatus(), BatchStatus.COMPLETED);
	}
}