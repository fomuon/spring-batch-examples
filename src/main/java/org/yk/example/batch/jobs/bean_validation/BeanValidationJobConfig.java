package org.yk.example.batch.jobs.bean_validation;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.support.CompositeItemProcessor;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.batch.item.validator.SpringValidator;
import org.springframework.batch.item.validator.ValidatingItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.yk.example.batch.common.LoggingItemWriter;

import java.util.Arrays;

@Configuration
public class BeanValidationJobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;

	@Autowired
	public BeanValidationJobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
	}

	@Bean
	public Validator springValidator() {
		//JSR-303 Validator
		return new LocalValidatorFactoryBean();
	}

	@Bean
	public SpringValidator<Person> springValidatorAdapter(Validator springValidator) {
		SpringValidator<Person> springValidatorAdapter = new SpringValidator<>();
		springValidatorAdapter.setValidator(springValidator);

		return springValidatorAdapter;
	}

	@Bean
	public ValidatingItemProcessor<Person> validatingItemProcessor(SpringValidator<Person> springValidatorAdapter) {
		ValidatingItemProcessor<Person> validatingItemProcessor = new ValidatingItemProcessor<>();
		validatingItemProcessor.setValidator(springValidatorAdapter);

		return validatingItemProcessor;
	}

	@Bean
	public Job beanValidationJob(SpringValidator<Person> springValidatorAdapter) throws Exception {
		return jobBuilderFactory.get("beanValidationJob")
				.start(beanValidationStep(springValidatorAdapter))
				.build();

	}

	private Step beanValidationStep(SpringValidator<Person> springValidatorAdapter) throws Exception {
		return stepBuilderFactory.get("beanValidationStep")
				.<Person, Person> chunk(5)
				.reader(new PersonItemReader())
				.processor(makeProcessor(springValidatorAdapter))
				.writer(new LoggingItemWriter<>())
				.build();
	}

	private ItemProcessor<Person, Person> makeProcessor(SpringValidator<Person> springValidatorAdapter) throws Exception {
		ValidatingItemProcessor<Person> validatingItemProcessor = new ValidatingItemProcessor<>();
		validatingItemProcessor.setValidator(springValidatorAdapter);
		validatingItemProcessor.afterPropertiesSet();

		PassThroughItemProcessor<Person> personPassThroughItemProcessor = new PassThroughItemProcessor<>();

		CompositeItemProcessor<Person, Person> itemProcessor = new CompositeItemProcessor<>();

		itemProcessor.setDelegates(Arrays.asList(
				validatingItemProcessor,
				personPassThroughItemProcessor
		));

		itemProcessor.afterPropertiesSet();
		return itemProcessor;
	}
}