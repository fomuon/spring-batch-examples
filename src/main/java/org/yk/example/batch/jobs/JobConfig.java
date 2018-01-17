package org.yk.example.batch.jobs;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.BeanWrapperFieldSetMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.support.PassThroughItemProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

@Configuration
public class JobConfig {
	private final JobBuilderFactory jobBuilderFactory;
	private final StepBuilderFactory stepBuilderFactory;
	private final DataSource dataSource;

	@Autowired
	public JobConfig(JobBuilderFactory jobBuilderFactory, StepBuilderFactory stepBuilderFactory, DataSource dataSource) {
		this.jobBuilderFactory = jobBuilderFactory;
		this.stepBuilderFactory = stepBuilderFactory;
		this.dataSource = dataSource;
	}

	@Bean
	public FlatFileItemReader<Member> sampleDataReader() {
		FlatFileItemReader<Member> reader = new FlatFileItemReader<>();
		reader.setResource(new ClassPathResource("sample-data.txt"));
		reader.setLineMapper(new DefaultLineMapper<Member>() {{
			setLineTokenizer(new DelimitedLineTokenizer() {{
				setNames(new String[] { "name", "age", "gender" });
			}});
			setFieldSetMapper(new BeanWrapperFieldSetMapper<Member>() {{
				setTargetType(Member.class);
			}});
		}});
		return reader;
	}
	@Bean
	public JdbcBatchItemWriter<Member> sampleDataWriter() {
		JdbcBatchItemWriter<Member> writer = new JdbcBatchItemWriter<>();
		writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<>());
		writer.setSql("INSERT INTO member (name, age, gender) VALUES (:name, :age, :gender);");
		writer.setDataSource(dataSource);
		return writer;
	}

	@Bean
	public Job sampleJob(JobCompletionNotificationListener listener) {
		return jobBuilderFactory.get("sampleJob")
			.incrementer(new RunIdIncrementer())
			.listener(listener)
			.flow(sampleDataStep())
			.end()
			.build();
	}

	@Bean
	public Step sampleDataStep() {
		return stepBuilderFactory.get("sampleDataStep")
			.<Member, Member> chunk(10)
			.reader(sampleDataReader())
			.processor(new MemberProcessor())
			.writer(sampleDataWriter())
			.build();
	}
}
