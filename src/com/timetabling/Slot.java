package com.timetabling;

public class Slot {
	private String slotID;
	private String day;
	private String duration;
	
	public Slot(String slotID, String day, String duration){
		this.slotID = slotID;
		this.day = day;
		this.duration = duration;
	}
	
	public String getSlotID() {
		return slotID;
	}
	public void setSlotID(String slotID) {
		this.slotID = slotID;
	}
	public String getDay() {
		return day;
	}
	public void setDay(String day) {
		this.day = day;
	}
	public String getDuration() {
		return duration;
	}
	public void setDuration(String duration) {
		this.duration = duration;
	}
	
	public String toString(){
		return this.getSlotID() + "\t" + this.getDay() + "\t" + this.duration + "\n";
	}
	
	
}
