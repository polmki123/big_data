package com.test;

import java.util.Arrays;

public class TestCompareTo {
	public static void main(String[] args) {
//		String[] test = new String[] 
//				{ "Apple", "Orange", "Banana"};
//		Arrays.sort(test);
//		
		Student[] stu = new Student[3];
		stu[0] = new Student("ȫ�浿", 20);
		stu[1] = new Student("�̼���", 18);
		stu[2] = new Student("��ڸ�", 31);
		
		Arrays.sort(stu);
		for(Student s: stu) {
			System.out.println(s.getName() 
					+ ", " + s.getAge());
		}
		
	}
}
