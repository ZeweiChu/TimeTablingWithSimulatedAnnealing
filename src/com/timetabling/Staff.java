package com.timetabling;

import java.util.HashMap;
import java.util.Map;

public class Staff {
	private String staffID;
	private Map<String, Session> sessions;
	private String name;
	
	public Staff(String staffID, String name){
		this.staffID = staffID;
		this.name = name;
		this.sessions = new HashMap<String, Session>();
	}
	
	public String getStaffID() {
		return staffID;
	}
	public void setStaffID(String staffID) {
		this.staffID = staffID;
	}
	public Map<String, Session> getSessions() {
		return sessions;
	}
	public void setSessions(Map<String, Session> sessions) {
		this.sessions = sessions;
	}
	public void addSession(Session s){
		this.sessions.put(s.getSessionID(), s);
	}
	public void removeSession(Session s){
		this.sessions.remove(s.getSessionID());
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String toString(){
		String res = "";
		res += this.staffID + "\t" + this.name + "\n";
		for (Session s: sessions.values()){
			res += s.toString();
		}
		return res;
	}
}
