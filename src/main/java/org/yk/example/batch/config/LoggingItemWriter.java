package org.yk.example.batch.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.item.ItemWriter;

import java.util.List;

@Slf4j
public class LoggingItemWriter<T> implements ItemWriter<T> {
	private int cnt = 0;


	@Override
	public void write(List<? extends T> items) throws Exception {
		log.info("==========Write Items Begin ({})=====", ++cnt);

		for (int i = 0; i < items.size(); i++) {
			log.info("{} - {}", i + 1, items.get(i));
		}

		log.info("==========Write Items End ({})=====", cnt);
	}
}
