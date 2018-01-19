package org.yk.example.batch.jobs.sample;

/**
 * @author yongkyu.lee@navercorp.com
 */
public class Member {
	private long seq;
	private String name;
	private int age;
	private String gender;

	public Member() {
	}

	public Member(long seq, String name, int age, String gender) {
		this.seq = seq;
		this.name = name;
		this.age = age;
		this.gender = gender;
	}

	public long getSeq() {
		return seq;
	}

	public void setSeq(long seq) {
		this.seq = seq;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	@Override
	public String toString() {
		return String.format("%d - name: %s, age: %d, gender: %s", seq, name, age, gender);
	}
}
