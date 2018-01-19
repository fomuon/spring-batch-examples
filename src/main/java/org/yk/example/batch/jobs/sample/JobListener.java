package org.yk.example.batch.jobs.sample;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.BatchStatus;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.listener.JobExecutionListenerSupport;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class JobListener extends JobExecutionListenerSupport {

	private static final Logger log = LoggerFactory.getLogger(JobListener.class);

	private final JdbcTemplate jdbcTemplate;

	@Autowired
	public JobListener(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	@Override
	public void beforeJob(JobExecution jobExecution) {
		log.info("===========JOB STARTED!==========");
		log.info("JobId: {}, JobExecutionId: {}, JobParams: {}", jobExecution.getJobId(), jobExecution.getId(), jobExecution.getJobParameters());
	}

	@Override
	public void afterJob(JobExecution jobExecution) {
		if(jobExecution.getStatus() == BatchStatus.COMPLETED) {
			log.info("===========JOB FINISHED!(verify the results)==========");

			List<Member> results = jdbcTemplate.query("SELECT seq, name, age, gender FROM member", new RowMapper<Member>() {
				@Override
				public Member mapRow(ResultSet rs, int row) throws SQLException {
					return new Member(rs.getLong("seq"), rs.getString("name"), rs.getInt("age"), rs.getString("gender"));
				}
			});

			for (Member member : results) {
				log.info("Found <" + member + "> in the database.");
			}
		}
	}
}
