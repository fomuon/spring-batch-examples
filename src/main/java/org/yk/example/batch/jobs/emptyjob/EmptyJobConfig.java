package org.yk.example.batch.jobs.emptyjob;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.job.builder.FlowBuilder;
import org.springframework.batch.core.job.builder.JobFlowBuilder;
import org.springframework.batch.core.job.flow.Flow;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
public class EmptyJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	public EmptyJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Job emptyJob() {
		TaskExecutor taskExecutor = createTaskExecutor();

		Flow firstFlow = new FlowBuilder<Flow>("firstFlow").split(taskExecutor)
			.add(
				bindStepsToFlow(emptyStep1()),
				bindStepsToFlow(emptyStep2()))
			.build();

		Flow secondFlow = bindStepsToFlow("secondFlow", emptyStep3());

		return jobBuilderFactory.get("emptyJob")
			.preventRestart()
			.incrementer(new RunIdIncrementer())
			.start(firstFlow)
			.next(secondFlow)
			.end().build();
	}

	public Step emptyStep1() {
		return stepBuilderFactory.get("emptyStep1")
			.<Integer, Integer> chunk(10)
			.faultTolerant()
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.reader(new EmptyItemReader(12345))
			.processor(new EmptyItemProcessor())
			.writer(new EmptyItemWriter())
			.build();
	}

	public Step emptyStep2() {
		return stepBuilderFactory.get("emptyStep2")
			.<Integer, Integer> chunk(10)
			.faultTolerant()
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.reader(new EmptyItemReader(5063))
			.processor(new EmptyItemProcessor())
			.writer(new EmptyItemWriter())
			.build();
	}

	public Step emptyStep3() {
		return stepBuilderFactory.get("emptyStep3")
			.<Integer, Integer> chunk(10)
			.faultTolerant()
			.skip(Exception.class)
			.skipLimit(Integer.MAX_VALUE)
			.reader(new EmptyItemReader(8567))
			.processor(new EmptyItemProcessor())
			.writer(new EmptyItemWriter())
			.build();
	}

	private TaskExecutor createTaskExecutor() {
		ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
		threadPoolTaskExecutor.setCorePoolSize(10);
		threadPoolTaskExecutor.afterPropertiesSet();

		return threadPoolTaskExecutor;
	}

	private Flow bindStepsToFlow(Step step) {
		String flowName = org.apache.commons.lang3.StringUtils.replace(step.getName(), "Step", "Flow");
		return bindStepsToFlow(flowName, step);
	}

	private Flow bindStepsToFlow(String flowName, Step...steps) {
		FlowBuilder<Flow> flowBuilder = new FlowBuilder<Flow>(flowName).from(steps[0]);

		for (int i = 1; i < steps.length; i++) {
			flowBuilder.next(steps[i]);
		}

		return flowBuilder.end();
	}
}
