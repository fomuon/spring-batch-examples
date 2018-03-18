package org.yk.example.batch.jobs.emptyjob;

import org.apache.commons.lang3.RandomUtils;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import sun.invoke.empty.Empty;

/**
 * @author fomuo@navercorp.com
 */
public class EmptyItemReader implements ItemReader<Integer> {

	private int count;

	public EmptyItemReader(int count) {
		this.count = count;
	}

	@Override
	public Integer read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {

		if (count <= 0) {
			return null;
		}

		if (RandomUtils.nextInt(0, 1000) < 10) {
			throw new Exception("error from reader");
		}

		return count--;
	}
}
