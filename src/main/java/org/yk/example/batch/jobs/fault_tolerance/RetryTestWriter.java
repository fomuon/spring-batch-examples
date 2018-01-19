package org.yk.example.batch.jobs.fault_tolerance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class RetryTestWriter implements ItemWriter<Integer> {
	private Logger log = LoggerFactory.getLogger(RetryTestWriter.class);

	@Override
	public void write(List<? extends Integer> items) throws Exception {
		log.info("Write items - {}", items.stream().map(String::valueOf).collect(Collectors.joining(",")));

		items.forEach(item -> {
			if (item % 8 == 0) {
				log.info("Writer Raise RetryException {}", item);
				throw new RetryException();
			}
		});
	}
}
