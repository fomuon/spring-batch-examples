package org.yk.example.batch.jobs.fault_tolerance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.StepExecution;
import org.springframework.batch.core.StepExecutionListener;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class StepCompletionListener implements StepExecutionListener {
	private static final Logger log = LoggerFactory.getLogger(StepCompletionListener.class);

	@Override
	public void beforeStep(StepExecution stepExecution) {
	}

	@Override
	public ExitStatus afterStep(StepExecution stepExecution) {
		log.info("===========Step FINISHED!==========");
		log.info("{} Result: {}", stepExecution.getStepName(), stepExecution.toString());

		return stepExecution.getExitStatus();
	}
}
