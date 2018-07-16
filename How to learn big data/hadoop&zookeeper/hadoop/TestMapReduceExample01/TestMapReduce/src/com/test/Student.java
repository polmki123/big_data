package com.test;

public class Student implements Comparable<Student>{
	private String name;
	public Student(String name, int age) {
		this.name = name;
		this.age = age;
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
	private int age;
	
	@Override
	public int compareTo(Student o) {
		return name.compareTo(o.getName()) * -1;
	}
//	public int compareTo(Student o) {
//		int a = ((Student)o).getAge();
//		
//		return this.age - a;
//	}
}
