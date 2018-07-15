package org.yk.example.batch.jobs.bean_validation;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

public class PersonItemReader implements ItemReader<Person> {
	private Iterator<Person> iterator;

	public PersonItemReader() {
		List<Person> member = Arrays.asList(
				Person.of(1, "yk1",20, LocalDate.of(1999, 3, 9)),
				Person.of(2, "yk2",21, LocalDate.of(1998, 3, 9)),
				Person.of(3, "yk3",22, LocalDate.of(1997, 3, 9)),
				Person.of(4, "yk4",23, LocalDate.of(1996, 3, 9)),
				Person.of(5, "yk5",24, LocalDate.of(1995, 3, 9)),
				Person.of(6, "yk6",25, LocalDate.of(1994, 3, 9))
		);

		this.iterator = member.iterator();
	}

	@Override
	public Person read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (iterator.hasNext()) {
			return iterator.next();
		}

		return null;
	}
}