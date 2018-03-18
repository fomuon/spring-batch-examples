package org.yk.example.batch.jobs.emptyjob;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.batch.item.ItemProcessor;

/**
 * @author fomuo@navercorp.com
 */
public class EmptyItemProcessor implements ItemProcessor<Integer, Integer> {
	@Override
	public Integer process(Integer s) throws Exception {

		if (RandomUtils.nextInt(0, 1000) < 10) {
			return null;
		}

		if (RandomUtils.nextInt(0, 1000) < 10) {
			throw new Exception("error from writer");
		}

		return s;
	}
}
