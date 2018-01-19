package org.yk.example.batch.jobs.fault_tolerance;

import org.springframework.batch.item.ItemProcessor;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class FaultToleranceProcessor implements ItemProcessor<Integer, Integer> {

	@Override
	public Integer process(Integer item) throws Exception {
		if (item % 3 == 0) {
			throw new SkipException();
		}

		return item;
	}
}
