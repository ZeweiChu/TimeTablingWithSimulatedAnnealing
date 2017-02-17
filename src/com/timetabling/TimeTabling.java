package com.timetabling;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class TimeTabling {
	public static Map<String, Student> students;
	public static Map<String, Staff> staffs;
	public static Map<String, Course> courses;
	public static Map<String, Session> sessions;
	public static Map<String, Room> rooms;
	public static Map<String, Slot> slots;
	
	public static void loadStudents(String path){
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String arr[] = line.split("\t");
		    	/*
		    	 * Student ID
		    	 * Student Name
		    	 * Level
		    	 */
		    	Student _student = new Student(arr[0], arr[1], Integer.parseInt(arr[2]));
		    	students.put(arr[0], _student);
		    } 
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void printStudents(){
		System.out.println("Student List...");
		for (Student s: students.values()){
			System.out.println(s.toString());
		}
	}
	

	public static void loadStaffs(String path){
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String arr[] = line.split("\t");
		    	/*
		    	 * Staff ID
		    	 * Staff Name
		    	 */
		    	Staff _staff = new Staff(arr[0], arr[1]);
		    	staffs.put(arr[0], _staff);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void printStaffs(){
		System.out.println("Staff List...");
		for (Staff s: staffs.values()){
			System.out.println(s.toString());
		}
	}
	
	public static void loadCourses(String path){
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    int i = 0;
		    Course _course = null;
		    while ((line = br.readLine()) != null) {
		    	String arr[] = line.split("\t");
		    	if (i % 4 == 0){
		    		/*
			    	 * Course ID
			    	 * Course Name
			    	 */
			    	_course = new Course(arr[0], arr[1]);
			    	courses.put(arr[0], _course);	
		    	} else if (i % 4 == 1 || i % 4 == 2){
		    		/*
			    	 * Session ID
			    	 * Staff ID
			    	 * Label
			    	 */
		    		Label label = null;
		    		if (arr[2].equals("LECTURE")){
		    			label = Label.LECTURE;
		    		} else if (arr[2].equals("LAB")){
		    			label = Label.LAB;
		    		}
		    		String staffID = arr[1];
		    		Session session = new Session(_course, arr[0], label, staffID);
		    		sessions.put(arr[0], session);
		    		_course.addSession(session);
		    		staffs.get(staffID).addSession(session);
		    	}
		    	
		    	i++;
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void printCourses(){
		System.out.println("Student List...");
		for (Course c: courses.values()){
			System.out.println(c.toString());
		}
	}
	
	public static void loadRooms(String path){
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
	    	Room r = null;
		    while ((line = br.readLine()) != null) {
		    	String arr[] = line.split("\t");
		    	/*
		    	 * roomID: room1 
		    	 * capacity: 101 
		    	 * room type: LECTURE
		    	 */
		    	if (arr[2].equals("LECTURE")){
		    		r = new Room(arr[0], Integer.parseInt(arr[1]), Label.LECTURE);
		    	} else if (arr[2].equals("LAB")){
		    		r = new Room(arr[0], Integer.parseInt(arr[1]), Label.LAB);
		    	}
		    	
		    	rooms.put(r.getRoomID(), r);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void printRooms(){
		System.out.println("Room List...");
		for (Room r: rooms.values()){
			System.out.println(r.toString());
		}
	}
	
	
	
	public static void loadEnrollments(String path){
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    line = br.readLine();
		    while (true) {
		    	if (null == line) break;
		    	if (line.startsWith("course")){
		    		Course c = courses.get(line);
		    		while (true){
		    			line = br.readLine();
		    			if (null == line) break;
		    			if (line.startsWith("course")) break;
		    			
		    			Student s = students.get(line);
		    			c.getStudents().put(s.getStudentID(), s);
		    			s.getCourses().put(c.getCourseID(), c);
		    		}
		    	}
		    	
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void loadSlots(String path){
		try (BufferedReader br = new BufferedReader(new FileReader(path))) {
		    String line;
		    while ((line = br.readLine()) != null) {
		    	String arr[] = line.split("\t");
		    	/*
		    	 * slotID: slot5	
		    	 * day: Monday	
		    	 * duration: 16:00-18:00
		    	 */
		    	Slot s = new Slot(arr[0], arr[1], arr[2]);
		    	slots.put(s.getSlotID(), s);
		    }
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	public static void printSlots(){
		System.out.println("Slot List...");
		for (Slot s: slots.values()){
			System.out.println(s.toString());
		}
	}
	
	public static void loadAllData(){
		students = new HashMap<String, Student>();
		staffs = new HashMap<String, Staff>();
		courses = new HashMap<String, Course>();
		sessions = new HashMap<String, Session>();
		rooms = new HashMap<String, Room>();
		slots = new HashMap<String, Slot>();
		
		loadStudents("data/students.txt");
		loadStaffs("data/staffs.txt");
		loadCourses("data/courses.txt");
		loadRooms("data/rooms.txt");
		loadEnrollments("data/enrollments.txt");
		loadSlots("data/slots.txt");
		
		
		//printStudents();
		//printStaffs();
		//printCourses();
		//printRooms();
		//printSlots();
	}
	
	public static String pickRandomSlotID(){
		Random rand = new Random();
		int i = rand.nextInt(slots.size()) + 1;
		return "slot" + i;
	}
	
	public static String pickRandomRoomID(){
		Random rand = new Random();
		int i = rand.nextInt(rooms.size()) + 1;
		return "room" + i;
	}
}
