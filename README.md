
## Chunk-Oriented Processing

### Chunk-Oriented Processing 특징

* ItemReader
  * read() 에서 null을 리턴하면 처리는 종료 된다.
  * read() 에서 리턴한 횟수는 `BATCH_STEP_EXECUTION.READ_COUNT` 에 기록 된다.
* ItemProcessor
  * process() 에서 null을 리턴하면 해당 Item을 Filter 처리 된다.
  * process() 에서 null을 리턴한 횟수는 `BATCH_STEP_EXECUTION.FILTER_COUNT` 에 기록 된다.
* ItemWriter
  * write(List items) 로 넘어온 items 의 사이즈는 `chunk()` (or commit-interval) 로 설정한 사이즈 이다. (또는 reader 에서 null을 리턴하기 전까지의 items)
  * write(List items) 에서 처리된 items 의 총 갯수는 `BATCH_STEP_EXECUTION.WRITE_COUNT` 에 기록 된다.
  * write() 호출 (commit) 횟수 는 `BATCH_STEP_EXECUTION.COMMIT_COUNT`에 기록 된다.

```java
@Bean
public Job basicSampleJob() {
	return jobBuilderFactory.get("sampleJob")
		.start(sampleDataStep())
		.build();
}

@Bean
public Step sampleDataStep() {
	return stepBuilderFactory.get("sampleDataStep")
		.<Member, Member> chunk(10)
		.reader(sampleDataReader())
		.processor(new MemberProcessor())
		.writer(sampleDataWriter())
		.build();
}
```

### 처리중 실패한 항목 스킵 하기

* 데이터 처리 과정중 특정 Throwable 이 발생한 경우 Job을 중단하지 않고 해당 Item 만 Skip 처리를 하기 위해서 아래와 같이 설정한다.

```java
public Step faultToleranceExampleStep() {
	return stepBuilderFactory.get("faultToleranceExampleStep")
		.<Integer, Integer>chunk(5)
		.reader(new FaultToleranceReader())
		.processor(new FaultToleranceProcessor())
		.writer(new FaultToleranceWriter())
		.faultTolerant()
		.skipLimit(100)
		.skip(CustomException.class)
		.listener(new StepCompletionListener())
		.build();
}
```

* `faultTolerant()` 를 통해 read-process-writ 과정중 Throwable 이 발생했을 경우 해당 Item을 Skip 하도록 설정 한다.
* `skipLimit()` 를 통해 몇개의 오류까지 Skip 할 것인지 설정한다. (default 0)
* `skip()` 을 통해 Skip 할 Throwable 타입을 설정한다. (중복호출하여 add)
  * `noSkip()` 을 통해 Skip 하지 않을 타입 지정 가능.
* reader 에서 Skip된 Item 의 갯수는 `BATCH_STEP_EXECUTION.READ_SKIP_COUNT` 에 기록 된다.
* processor 에서 Skip된 Item 의 갯수는 `BATCH_STEP_EXECUTION.PROCESS_SKIP_COUNT` 에 기록 된다.
* writer 에서 Skip된 Item 의 갯수는 `BATCH_STEP_EXECUTION.WRITE_SKIP_COUNT` 에 기록 된다.
  * write에서는 chunk로 넘어온 items 중 특정 Item에서 오류가 발생하는 경우 해당 chunk에 대해 각각 write() 를 재호출 함으로서, 오류가 발생하는 Item을 제외한 Item들이 문제없이 처리되도록 한다.

### 처리중 실패한 항목 재시도 하기

* processor or writer 처리 과정중 특정 Throwable 이 발생한 경우 해당 Item의 처리를 재시도 하도록 하기 위해서 아래와 같이 설정한다.

```java
public Step faultToleranceRetryExampleStep() {
	return stepBuilderFactory.get("faultToleranceRetryExampleStep")
		.<Integer, Integer>chunk(5)
		.reader(new RetryTestReader())
		.processor(new RetryTestProcessor())
		.writer(new RetryTestWriter())
		.faultTolerant()
		.retryLimit(2)
		.retry(RetryException.class)
		.listener(new StepCompletionListener())
		.build();
}
```

* `retryLimit()` 을 통해 재시도 횟수를 설정한다.
* `retry()` 을 통해 재시도할 Throwable 을 설정한다.
* reader 에서 오류 발생시 retry 되지 않는다.
* processor or writer 에서 오류 발생시 process 부터 재시도 된다.

## Step Flow
> 하나의 Job 이 여러개의 Step 으로 이루어진 경우 Step 간의 처리 흐름을 설정 할 수 있다.

### 순차적 Flow
* Step들이 순서에 따라 순차적으로 처리된다.
* taskA, taskB, taskC 가 순서대로 실행되며 중간 Step 이 실패 하는 경우 Job은 종료 된다.

```java
@Bean
public Job sequentialFlowJob() {
	Step taskA = createStep("TaskA", 3);
	Step taskB = createStep("TaskB", 3);
	Step taskC = createStep("TaskC", 3);

	return jobBuilderFactory.get("sequentialFlowJob")
		.flow(taskA)
		.next(taskB)
		.next(taskC)
		.end()
		.build();
}
```

### 조건부 Flow
* 이전 Step의 처리 결과에 따라 서로 다른 Step으로 분기 처리 할 수 있다.
* taskA 가 실패한다면 taskB 를 수행하고, taskA가 성공한다면 taskC를 수행하도록 아래와 같이 설정할 수 있다.
* `on()` 에 from Step의 ExitStatus 와 매치 되면 `to()` 를 수행한다.
* 조건은 Programmatic 으로도 처리 가능하다. (JobExecutionDecider)

```java
@Bean
public Job conditionalFlowJob() {
	Step taskA = createStep("TaskA", 5, 3);
	Step taskB = createStep("TaskB", 3);
	Step taskC = createStep("TaskC", 3);

	return jobBuilderFactory.get("conditionalFlowJob")
		.flow(taskA)
		.on("FAILED").to(taskB)
		.from(taskA)
		.on("COMPLETED").to(taskC)
		.end()
		.build();
}
```

### Split Flow (병렬 수행)
* Split Flow 를 통해 각 Step을 서로 독립적으로 수행 할 수 있다.
* ThreadPoolTaskExecutor 를 통해 병렬 처리도 가능하다.
* 아래는 taskA, taskB, taskC 를 병렬로 처리하는 설정 이다.

```java
@Bean
public Job splitFlowJob() {
	Step taskA = createStep("TaskA", 3);
	Step taskB = createStep("TaskB", 3);
	Step taskC = createStep("TaskC", 3);

	Flow splitFlow = new FlowBuilder<Flow>("splitFlow")
		.split(createThreadPoolTaskExecutor(5))
		.add(
			new FlowBuilder<Flow>("taskAFlow").start(taskA).build(),
			new FlowBuilder<Flow>("taskBFlow").start(taskB).build(),
			new FlowBuilder<Flow>("taskCFlow").start(taskC).build()
		).build();


	return jobBuilderFactory.get("splitFlowJob")
		.start(splitFlow)
		.end()
		.build();
}
```