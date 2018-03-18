package org.yk.example.batch.jobs.emptyjob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemWriter;
import org.yk.example.batch.jobs.fault_tolerance.FaultToleranceReader;

import java.util.List;

/**
 * @author fomuo@navercorp.com
 */
public class EmptyItemWriter implements ItemWriter<Integer> {
	private Logger log = LoggerFactory.getLogger(EmptyItemWriter.class);

	@Override
	public void write(List<? extends Integer> list) throws Exception {
		Thread.sleep(200);
		log.info("EmptyWrite: {}", list);
	}
}
