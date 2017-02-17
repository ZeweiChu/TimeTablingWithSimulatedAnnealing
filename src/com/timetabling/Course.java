package com.timetabling;

import java.util.HashMap;
import java.util.Map;

public class Course {
	
	private String courseID;
	private Map<String, Student> students;
	private String courseTitle;
	private Map<String, Staff> staffs;
	private Map<String, Session> sessions;
	
	public Course(String courseID, String courseTitle){
		this.courseID = courseID;
		this.courseTitle = courseTitle;
		this.students = new HashMap<String, Student>();
		this.staffs = new HashMap<String, Staff>();
		this.sessions = new HashMap<String, Session>();
	}
	
	public String getCourseID() {
		return courseID;
	}
	public void setCourseID(String courseID) {
		this.courseID = courseID;
	}
	public Map<String, Student> getStudents() {
		return students;
	}
	public void setStudents(Map<String, Student> students) {
		this.students = students;
	}
	public Map<String, Staff> getStaffs() {
		return staffs;
	}
	public void setStaffs(Map<String, Staff> staffs) {
		this.staffs = staffs;
	}
	public Map<String, Session> getSessions() {
		return sessions;
	}
	public void setSessions(Map<String, Session> sessions) {
		this.sessions = sessions;
	}
	public String getCourseTitle() {
		return courseTitle;
	}
	public void setCourseTitle(String courseTitle) {
		this.courseTitle = courseTitle;
	}
	
	public void addSession(Session session){
		this.sessions.put(session.getSessionID(), session);
	}
	
	public String toString(){
		String res = "";
		res += this.courseID + "\t" + this.courseTitle + "\n";
		for (Session s: sessions.values()){
			res += s.toString();
		}
		
		return res;
	}
}
