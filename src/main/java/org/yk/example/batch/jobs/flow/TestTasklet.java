package org.yk.example.batch.jobs.flow;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class TestTasklet implements Tasklet {
	private Logger log = LoggerFactory.getLogger(TestTasklet.class);

	private String taskName = null;
	private int count = 0;
	private int max;
	private int errCount = 0; //에러 번호

	public TestTasklet(String taskName, int max) {
		this.taskName = taskName;
		this.max = max;
	}

	public TestTasklet(String taskName, int max, int errCount) {
		this(taskName, max);
		this.errCount = errCount;
	}

	@Override
	public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {

		if (count++ >= max) {
			return RepeatStatus.FINISHED;
		}

		log.info("{} processing {}", taskName, count);

		if (count == errCount) {
			log.info("{} error occurred", taskName);
			throw new RuntimeException("intended exception");
		}

		try { Thread.sleep(1000); } catch (Exception ignore) {}

		return RepeatStatus.CONTINUABLE;
	}
}
