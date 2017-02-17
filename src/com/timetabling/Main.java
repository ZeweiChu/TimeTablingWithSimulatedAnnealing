package com.timetabling;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class Main {
	
	public static int ANNEAL_GOOD_THRESHOLD = 10;
	public static int ANNEAL_BAD_THRESHOLD = 20;
	public static double ANNEAL_TEMP_START = 100.0;
	public static double ANNEAL_TEMP_REDUCTION = 0.75;
	public static PrintWriter temp_writer;
	public static PrintWriter optimal_writer;
	public static PrintWriter current_writer;
	
	public static Map<String, Map<String, List<String>>> schedule;
	
	public static Map<String, Map<String, List<String>>> optimal_schedule;
	public static Map<String, Map<String, List<String>>> prev_schedule;
	public static double optimal_cost = 999999.0;
	public static double curr_cost = 999999.0;
	public static double prev_cost = 999999.0;
	public static String old_slotID;
	public static String old_roomID;
	public static int old_index;
	public static String new_slotID;
	public static String new_roomID;
	public static int new_index;
	
	public static void main(String[] args) {
		try {
			temp_writer = new PrintWriter("result/temp.txt", "UTF-8");
			current_writer = new PrintWriter("result/current_cost.txt", "UTF-8");
			optimal_writer = new PrintWriter("result/optimal_cost.txt", "UTF-8");
		} catch (Exception e) {
		}
		TimeTabling.loadAllData();
		anneal();
		temp_writer.close();
		current_writer.close();
		optimal_writer.close();
	}

	public static void copy_to_optimal_schedule(){
		optimal_schedule = new HashMap<String, Map<String, List<String>>>();
		
		//initialize our schedule
		for (int i = 1; i <= TimeTabling.slots.size(); ++i){
			optimal_schedule.put("slot" + i, new HashMap<String, List<String>>());
			for (int j = 1; j <= TimeTabling.rooms.size(); ++j){
				optimal_schedule.get("slot" + i).put("room" + j, new ArrayList<String>());
				for (String sessionID: schedule.get("slot"+i).get("room"+j)){
					optimal_schedule.get("slot"+i).get("room"+j).add(sessionID);
				}
			}
		} 
	}
	
	public static String pop_random_session(){
		Random rand = new Random();
		while (true){
			int slot = rand.nextInt(TimeTabling.slots.values().size()) + 1;
			int room = rand.nextInt(TimeTabling.rooms.values().size()) + 1;
			int size = schedule.get("slot" + slot).get("room" + room).size();
			if (size > 0){
				old_slotID = "slot" + slot;
				old_roomID = "room" + room;
				
				int r = rand.nextInt(size);
				old_index = r;
				return schedule.get("slot" + slot).get("room" + room).remove(r);
			}
		}
	}
	
	public static double move(){
		String sessionID = pop_random_session();
		Random rand = new Random();
		new_slotID = "slot" + (rand.nextInt(TimeTabling.slots.values().size()) + 1);
		new_roomID = "room" + (rand.nextInt(TimeTabling.rooms.values().size()) + 1);
		schedule.get(new_slotID).get(new_roomID).add(sessionID);
		
		prev_cost = curr_cost;
		curr_cost = evaluate(schedule);
		
		if (curr_cost < optimal_cost){
			//optimal_schedule.get(old_slotID).get(old_roomID).remove(old_index);
			//optimal_schedule.get(new_slotID).get(new_roomID).add(sessionID);
			optimal_cost = curr_cost;
			copy_to_optimal_schedule();
		}
		
		System.out.println("schedule cost: " + curr_cost);
		System.out.println("prev schedule cost: " + prev_cost);
		
		return curr_cost - prev_cost;
	}
	
	public static void anneal(){
		int good = 1, bad;
		double temp = ANNEAL_TEMP_START;
		
		/*
		 * model of our schedule
		 * Map<time slot, Map<room, List<course>>>
		 */		
		gen_random_schedule();
		
		printSchedule(schedule);
		evaluate(schedule);
		
		while (good > 0){
			good = bad = 0;
			while (good < ANNEAL_GOOD_THRESHOLD && bad < ANNEAL_BAD_THRESHOLD){
				double delta = move();
				
				Random rand = new Random();
				if (delta < 0 || 
						(double)rand.nextInt(999) / 1000.0 < Math.exp(-delta / temp)){
					good++;
					prev_schedule.get(new_slotID).get(new_roomID).add(
						prev_schedule.get(old_slotID).get(old_roomID).remove(old_index)
					);
				} else {
					//restore the old state
					schedule.get(old_slotID).get(old_roomID).add(
						schedule.get(new_slotID).get(new_roomID).remove(schedule.get(new_slotID).get(new_roomID).size()-1)
					);
					bad++;
					curr_cost = prev_cost;
				}
				temp_writer.println(temp);
				current_writer.println(curr_cost);
				optimal_writer.println(optimal_cost);
			}
			temp *= ANNEAL_TEMP_REDUCTION;
		}
		
		printSchedule(optimal_schedule);
		evaluate(optimal_schedule);
		
		System.out.println("optimal cost: " + optimal_cost);
	}
	
	public static void gen_random_schedule(){
		/*
		 * model of our schedule
		 * Map<time slot, Map<room, List<course>>>
		 */		
		schedule = new HashMap<String, Map<String, List<String>>>();
		prev_schedule = new HashMap<String, Map<String, List<String>>>();
		optimal_schedule = new HashMap<String, Map<String, List<String>>>();
		
		//initialize our schedule
		for (int i = 1; i <= TimeTabling.slots.size(); ++i){
			schedule.put("slot" + i, new HashMap<String, List<String>>());
			prev_schedule.put("slot" + i, new HashMap<String, List<String>>());
			optimal_schedule.put("slot" + i, new HashMap<String, List<String>>());
			for (int j = 1; j <= TimeTabling.rooms.size(); ++j){
				schedule.get("slot" + i).put("room" + j, new ArrayList<String>());
				prev_schedule.get("slot" + i).put("room" + j, new ArrayList<String>());
				optimal_schedule.get("slot" + i).put("room" + j, new ArrayList<String>());
			}
		}
		
		//generate a schedule
//		int slotID = 0, roomID = 0;
//		int num_rooms = TimeTabling.rooms.values().size();
//		int num_slots = TimeTabling.slots.values().size();
//		for (int i = 1; i <= TimeTabling.sessions.size(); ++i){
//			roomID = roomID % num_rooms + 1;
//			if (roomID == 1){
//				slotID = slotID % num_slots + 1;
//			}
//			System.out.println(slotID + " " + roomID);
//			schedule.get("slot" + slotID).get("room" + roomID).add("session" + i);
//			prev_schedule.get("slot" + slotID).get("room" + roomID).add("session" + i);
//			optimal_schedule.get("slot" + slotID).get("room" + roomID).add("session" + i);
//			TimeTabling.sessions.get("session" + i).setSlotID("slot" + slotID);
//			TimeTabling.sessions.get("session" + i).setRoomID("room" + roomID);
//			
//		}
		
		//generate a random schedule
		for (int i = 1; i <= TimeTabling.sessions.size(); ++i){
			String slotID = TimeTabling.pickRandomSlotID();
			String roomID = TimeTabling.pickRandomRoomID();
			schedule.get(slotID).get(roomID).add("session" + i);
			prev_schedule.get(slotID).get(roomID).add("session" + i);
			optimal_schedule.get(slotID).get(roomID).add("session" + i);
			TimeTabling.sessions.get("session" + i).setSlotID(slotID);
			TimeTabling.sessions.get("session" + i).setRoomID(roomID);
		}
	}
	
	public static void printSchedule(Map<String, Map<String, List<String>>> schedule){
		System.out.println("-----------Course Schedule-------------");
		for (String slotID: schedule.keySet()){
			for (String roomID: schedule.get(slotID).keySet()){
				for (String sessionID: schedule.get(slotID).get(roomID)){
					System.out.println(slotID + "\t" + roomID);
					System.out.println(TimeTabling.sessions.get(sessionID));
				}
			}
		}
	}
	
	/*
	 * Evaluate the cost function of current schedule
	 * This function defines what cost function we want to optimize in our time table scheduling problem
	 * including: 1. a staff cannot teach two classes at the same time
	 * 2. the same classroom cannot hold two classes at the same time
	 * 3. a student cannot take two classes at the same time
	 * 4. a lecture class cannot be held in a lab classroom and vice versa
	 * 
	 */
	public static double evaluate(Map<String, Map<String, List<String>>> schedule){
		double cost = 0.0;
		/*
		 * test weather two classes are allocated to the same classroom
		 * this is a very severe condition that should be avoided
		 * whenever we find several classes in the same time slot and same room,
		 * we add 100 * number of class conflict pairs to the cost
		 */
		int num_violation = 0;
		int total = 0;
		for (String slotID: schedule.keySet()){
			for (String roomID: schedule.get(slotID).keySet()){
				int num_classes = schedule.get(slotID).get(roomID).size(); 
				
				num_violation += num_classes * (num_classes - 1) / 2;
				//System.out.println("Different class session happens "
				//		+ "at the same class room at the same time with cost" + cost);
			}
		}
		cost += 100.0 * ((double)num_violation / 
				(double)(TimeTabling.courses.size()*2*(TimeTabling.courses.size()-1)*2/2));
		System.out.println("Cost after slot and room conflicts: " + cost);
		
		/*
		 * test weather the same staff needs to teach two class sessions at the same time slot.
		 * This is a severe condition that should be avoided 
		 */
		num_violation = 0;
		total = 0;
		for (Staff staff: TimeTabling.staffs.values()){
			for (String sessionID: staff.getSessions().keySet()){
				for (String _sessionID: staff.getSessions().keySet()){
					total ++;
					if (!sessionID.equals(_sessionID) &&
							TimeTabling.sessions.get(sessionID).getSlotID().
							equals(TimeTabling.sessions.get(_sessionID).getSlotID())){
						num_violation++;
						//cost += 50;
//						System.out.println(sessionID + "\t" + _sessionID);
//						System.out.println("Same staff teaching different sessions which happens at the same time cost: " + cost);
					}
				}
			}
		}
		cost += 50.0 * (double)num_violation / (double)total;
		System.out.println("Cost after staff conflicts: " + cost);
		
		/*
		 * test if the same students registered two sessions which are at the same time.
		 * This is not a huge issue, as students can take the course next year
		 * but should be avoided as much as possible.
		 */
		num_violation = 0;
		total = 0;
		for (Student student: TimeTabling.students.values()){
			for (Course course: student.getCourses().values()){
				for (Session session: course.getSessions().values()){
					for (Course _course: student.getCourses().values()){
						for (Session _session: _course.getSessions().values()){
							total++;
							if (!session.equals(_session) && 
									session.getSlotID().equals(_session.getSlotID())){
								num_violation++;
//								System.out.println("Student: " + student);
//								System.out.println("session: " + session);
//								System.out.println("session: " + _session);
//								System.out.println("Cost: " + cost);
							}
						}
					}		
				}
			}
		}
		cost += 2.0 * (double)num_violation / (double)total;
		System.out.println("Cost after students conflicts: " + cost);
		
		/*
		 * test if a lab class is allocated to a lecture classroom or vice versa?
		 * This is a servere condition that should definitely be avoided. cost: 200
		 */
		num_violation = 0;
		total = 0;
		for (String slotID: schedule.keySet()){
			for (String roomID: schedule.get(slotID).keySet()){
				for (String sessionID: schedule.get(slotID).get(roomID)){
					total++;
					if (TimeTabling.rooms.get(roomID).getLabel() != 
							TimeTabling.sessions.get(sessionID).getLabel()){
						
						
						num_violation++;
//						System.out.println("room type not matching cost: " + cost);
//						System.out.println(roomID + " " + sessionID);
						/*
						 * handles the case when room is too small for the class
						 */
					}
				}
			}
		}
		cost += 100.0 * (double)num_violation / (double)total;
		
		
		num_violation = 0;
		total = 0;
		for (String slotID: schedule.keySet()){
			for (String roomID: schedule.get(slotID).keySet()){
				for (String sessionID: schedule.get(slotID).get(roomID)){
					total++;
					if (TimeTabling.sessions.get(sessionID).getCourse().getStudents().size()
							> TimeTabling.rooms.get(roomID).getCapacity()){
						num_violation++;
						//						System.out.println("Room " + roomID + " too small for " + sessionID);
//						System.out.println("cost: " + cost);
					}
				}
			}
		}
		cost += 100.0 * (double)num_violation / (double)total;
		System.out.println("Cost after room type conflicts: " + cost);
		System.out.println("----------total cost: " + cost);
		
		
		return cost;
	}

}
