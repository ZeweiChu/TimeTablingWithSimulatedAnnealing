package com.timetabling;

public class Session {
	private Course course;
	private String sessionID;
	private Label label;
	private String roomID;
	private String staffID;
	private String slotID;

	public Session(Course course, String sessionID, Label label, String staff){
		this.setCourse(course);
		this.sessionID = sessionID;
		this.label = label;
		this.staffID = staff;
	}
	
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public String getSessionID() {
		return sessionID;
	}
	public void setSessionID(String sessionID) {
		this.sessionID = sessionID;
	}
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	
	public String toString(){
		String res = "";
		res += this.sessionID + "\t" + this.label + "\n";
		res += "Staff: " + this.staffID + "\n";
		
		return res;
	}

	public Course getCourse() {
		return course;
	}

	public void setCourse(Course course) {
		this.course = course;
	}

	public String getSlotID() {
		return slotID;
	}

	public void setSlotID(String slotID) {
		this.slotID = slotID;
	}
}
