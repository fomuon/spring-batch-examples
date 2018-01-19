package org.yk.example.batch.jobs.fault_tolerance;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class FaultToleranceWriter implements ItemWriter<Integer> {
	private Logger log = LoggerFactory.getLogger(FaultToleranceWriter.class);

	@Override
	public void write(List<? extends Integer> items) throws Exception {
		StringBuilder sb = new StringBuilder();

		items.forEach(item -> {
			if (item % 8 == 0) {
				throw new SkipException();
			}
			sb.append(item).append(", ");
		});

		sb.delete(sb.length() - 2, sb.length() - 1);

		log.info("Write items - {}", sb.toString());
	}
}
