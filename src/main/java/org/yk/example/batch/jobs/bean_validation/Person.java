package org.yk.example.batch.jobs.bean_validation;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@AllArgsConstructor(staticName = "of")
@Data
public class Person {
	@NotNull
	private Integer id;

	private String name;

	@Min(0) @Max(100)
	private Integer age;

	private LocalDate birthDay;
}