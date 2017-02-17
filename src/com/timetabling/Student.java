package com.timetabling;

import java.util.HashMap;
import java.util.Map;

public class Student {
	private String studentID;
	private String name;
	private int level;
	private Map<String, Course> courses; //map from courseId to course
	
	public Student(String studentID, String name, int level){
		this.studentID = studentID;
		this.name = name;
		this.level = level;
		this.courses = new HashMap<String, Course>();
	}
	public String getStudentID() {
		return studentID;
	}
	public void setStudentID(String studentID) {
		this.studentID = studentID;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getLevel() {
		return level;
	}
	public void setLevel(int level) {
		this.level = level;
	}
	public Map<String, Course> getCourses() {
		return courses;
	}
	public void setCourses(Map<String, Course> courses) {
		this.courses = courses;
	}

	public String toString(){
		String res = "";
		res += this.getStudentID() + "\t" + this.getName() + "\t" + this.getLevel() + "\n";
		for (Course c: this.getCourses().values()){
			res += c.toString();
		}
		return res;
	}
}

