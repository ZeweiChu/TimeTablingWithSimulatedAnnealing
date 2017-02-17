package com.timetabling;

public class Room {
	private Label label;
	private int capacity;
	private String roomID;
	
	public Room(String roomID, int capacity, Label label){
		this.label = label;
		this.capacity = capacity;
		this.roomID = roomID;
	}
	
	public Label getLabel() {
		return label;
	}
	public void setLabel(Label label) {
		this.label = label;
	}
	public int getCapacity() {
		return capacity;
	}
	public void setCapacity(int capacity) {
		this.capacity = capacity;
	}
	public String getRoomID() {
		return roomID;
	}
	public void setRoomID(String roomID) {
		this.roomID = roomID;
	}
	
	public String toString(){
		return this.getRoomID() + "\t" + this.getCapacity() + "\t" + this.getLabel() + "\n";
	}
	
}
