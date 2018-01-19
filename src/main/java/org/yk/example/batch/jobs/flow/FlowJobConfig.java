package org.yk.example.batch.jobs.flow;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import javax.sql.DataSource;

/**
 * @author yongkyu.lee@navercorp.com
 */
@Configuration
public class FlowJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Autowired
	public FlowJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.dataSource = dataSource;
	}

	@Bean
	public Job sequentialFlowJob() {
		Step taskA = createStep("TaskA", 3);
		Step taskB = createStep("TaskB", 3);
		Step taskC = createStep("TaskC", 3);

		return jobBuilderFactory.get("sequentialFlowJob")
			.flow(taskA)
			.next(taskB)
			.next(taskC)
			.end()
			.build();
	}

	@Bean
	public Job conditionalFlowJob() {
		Step taskA = createStep("TaskA", 5, 3);
		Step taskB = createStep("TaskB", 3);
		Step taskC = createStep("TaskC", 3);

		return jobBuilderFactory.get("conditionalFlowJob")
			.flow(taskA)
			.on("FAILED").to(taskB)
			.from(taskA)
			.on("COMPLETED").to(taskC)
			.end()
			.build();
	}

	@Bean
	public Job splitFlowJob() {
		Step taskA = createStep("TaskA", 3);
		Step taskB = createStep("TaskB", 3);
		Step taskC = createStep("TaskC", 3);

		Flow splitFlow = new FlowBuilder<Flow>("splitFlow")
			.split(createThreadPoolTaskExecutor(5))
			.add(
				new FlowBuilder<Flow>("taskAFlow").start(taskA).build(),
				new FlowBuilder<Flow>("taskBFlow").start(taskB).build(),
				new FlowBuilder<Flow>("taskCFlow").start(taskC).build()
			).build();


		return jobBuilderFactory.get("splitFlowJob")
			.start(splitFlow)
			.end()
			.build();
	}

	public Step createStep(String taskName, int procCount) {
		return createStep(taskName, procCount, 0);
	}
	public Step createStep(String taskName, int procCount, int errCount) {
		return stepBuilderFactory.get(taskName + "Step")
			.tasklet(new TestTasklet(taskName, procCount, errCount))
			.build();
	}

	public TaskExecutor createThreadPoolTaskExecutor(int poolSize) {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(poolSize);
		threadPoolTaskExecutor.afterPropertiesSet();

		return threadPoolTaskExecutor;
	}
}
