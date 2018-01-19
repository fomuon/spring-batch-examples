package org.yk.example.batch.jobs.fault_tolerance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class RetryTestReader implements ItemReader<Integer> {
	private Logger log = LoggerFactory.getLogger(RetryTestReader.class);

	private int num = 0;
	private int max = 50;

	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		num++;
		log.info("Reader : {}", num);

		if (num <= max) {
			return num;
		}

		return null;
	}
}
