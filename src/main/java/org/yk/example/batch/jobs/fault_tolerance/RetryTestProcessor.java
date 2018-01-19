package org.yk.example.batch.jobs.fault_tolerance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class RetryTestProcessor implements ItemProcessor<Integer, Integer> {
	private Logger log = LoggerFactory.getLogger(RetryTestProcessor.class);

	@Override
	public Integer process(Integer item) throws Exception {
		log.info("Process : {}", item);

//		if (item % 3 == 0) {
//			throw new RetryException();
//		}

		return item;
	}
}
