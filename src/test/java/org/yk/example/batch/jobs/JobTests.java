package org.yk.example.batch.jobs;


import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest(properties = {"spring.batch.job.enabled=false", "logging.level.root=info"})
public class JobTests {
	@Autowired
	@Qualifier("sampleJobLauncherTestUtils")
	private JobLauncherTestUtils sampleJobLauncherTestUtils;

	@Autowired
	@Qualifier("faultToleranceSkipExampleJobLauncherTestUtils")
	private JobLauncherTestUtils faultToleranceSkipExampleJobLauncherTestUtils;

	@Autowired
	@Qualifier("faultToleranceRetryExampleJobLauncherTestUtils")
	private JobLauncherTestUtils faultToleranceRetryExampleJobLauncherTestUtils;

	@Autowired
	@Qualifier("sequentialFlowJobLauncherTestUtils")
	private JobLauncherTestUtils sequentialFlowJobLauncherTestUtils;

	@Autowired
	@Qualifier("conditionalFlowJobLauncherTestUtils")
	private JobLauncherTestUtils conditionalFlowJobLauncherTestUtils;

	@Autowired
	@Qualifier("splitFlowJobLauncherTestUtils")
	private JobLauncherTestUtils splitFlowJobLauncherTestUtils;

	@Autowired
	@Qualifier("emptyJobLauncherTestUtils")
	private JobLauncherTestUtils emptyJobLauncherTestUtils;

	@Test
	public void sampleJobTest() throws Exception {
		JobExecution jobExecution = sampleJobLauncherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@Test
	public void faultToleranceSkipExampleJobTest() throws Exception {
		JobExecution jobExecution = faultToleranceSkipExampleJobLauncherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@Test
	public void faultToleranceRetryExampleJobTest() throws Exception {
		JobExecution jobExecution = faultToleranceRetryExampleJobLauncherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@Test
	public void sequentialFlowJobTest() throws Exception {
		JobExecution jobExecution = sequentialFlowJobLauncherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@Test
	public void conditionalFlowJobTest() throws Exception {
		JobExecution jobExecution = conditionalFlowJobLauncherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@Test
	public void splitFlowJobTest() throws Exception {
		JobExecution jobExecution = splitFlowJobLauncherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}

	@Test
	public void emptyJobTest() throws Exception {
		JobExecution jobExecution = emptyJobLauncherTestUtils.launchJob();
		Assert.assertEquals(BatchStatus.COMPLETED, jobExecution.getStatus());
	}
}
