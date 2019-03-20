package Schedule;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.lang.reflect.Array;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.json.*;
 
 
public class ClientThread implements Runnable {
    final int FIELD_WEIGHT = 2;
    BufferedReader in;
    PrintWriter out;
    ArrayList<Discipline> disciplinesList = new ArrayList<Discipline>();
    ArrayList<Group> groupsList = new ArrayList<Group>();
    ArrayList<Lecture> lecturesList = new ArrayList<Lecture>();
    ArrayList<Teacher> teachersList = new ArrayList<Teacher>();
    final int GROUP_HOURS = 8;
    final int COLLEGE_HOURS = 12;
    int[][] dataSheet;
    Map<Integer, ArrayList> schedule = new HashMap<Integer, ArrayList>();
    Map<Integer, Array> dayScheduleByGroups = new HashMap<Integer, Array>();
    




    protected Socket clientSocket = null;
 
    public ClientThread(Socket clientSocket) {
	System.out.println("clientConnected");
        this.clientSocket = clientSocket;
    }
 
    
    public void run() {
        try {
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            out = new PrintWriter(new OutputStreamWriter(clientSocket.getOutputStream()));
            JSONObject request = new JSONObject(in.readLine());
            initObjects(request);
            printObjects();
            generateDataSheet();
            printDataSheet();
            generateSchedule();
            
        } catch (Exception e) {
            e.printStackTrace();
            out.println("{\"result\":\"bad request\"}");
            
        }
        finally {
            try {
		in.close();
		out.close();
	    } catch (IOException e) {
		e.printStackTrace();
	    }
            
        }
    }
    
    private void initObjects(JSONObject request) throws Exception{
        try {
        	JSONArray disciplines = request.getJSONObject("data").getJSONArray("disciplines");
        	JSONArray groups = request.getJSONObject("data").getJSONArray("groups");
        	JSONArray teachers = request.getJSONObject("data").getJSONArray("teachers");
        	JSONArray lectures = request.getJSONObject("data").getJSONArray("lectures");
        	
        	for(int i = 0; i < disciplines.length(); i++) {
        	    disciplinesList.add(new Discipline(disciplines.getJSONObject(i).getInt("id"),
        		    		disciplines.getJSONObject(i).getString("name"),
        		    		disciplines.getJSONObject(i).getLong("dateStart") / 86400000,
        		    		disciplines.getJSONObject(i).getLong("dateEnd") / 86400000,
        		    		disciplines.getJSONObject(i).getInt("hours"),
        		    		disciplines.getJSONObject(i).getBoolean("type")
        		    		)
        		    );
        	}
        	
        	for(int i = 0; i < groups.length(); i++) {
        	    groupsList.add(new Group(groups.getJSONObject(i).getInt("id"),
        		    		groups.getJSONObject(i).getString("name")
        		    	)
        		    );
        	    JSONArray disciplinesPool = groups.getJSONObject(i).getJSONArray("disciplinesPool");
        	    
        	    
        	    
        	    for(int j = 0; j< disciplinesPool.length(); j++) {
        		for(int k = 0; k < disciplinesPool.getJSONObject(j).getJSONArray("teachersId").length(); k++) {
        		    for(Discipline d : disciplinesList) {
        			if(d.getId() == disciplinesPool.getJSONObject(j).getInt("disciplineId")) {
        			    d.teachers.add(disciplinesPool.getJSONObject(j).getJSONArray("teachersId").getInt(k));
        			    groupsList.get(i).disciplines.add(d);
        			}
        		    }
        		}
        	    }
        	}
        	
        	for(int i = 0; i < teachers.length(); i++) {
        	    teachersList.add(new Teacher(teachers.getJSONObject(i).getInt("id"),
        		    		teachers.getJSONObject(i).getString("fio"))
        		    );
        	}
        	
        	for(int i = 0; i < lectures.length(); i++) {
        	    lecturesList.add(new Lecture(lectures.getJSONObject(i).getInt("id"),
        		    		lectures.getJSONObject(i).getString("number"),
        		    		lectures.getJSONObject(i).getBoolean("type"))
        		    );
        	}

        	
        	
        
        
        
        	
        }catch(Exception e) {
    		throw new Exception(e);
        }
    }
    
    
    public void generateDataSheet() {
	int rows = 0;
	final int COLS = 3;
	for(Group group : groupsList) {
	    for(Discipline d : group.disciplines) {
		rows++;
	    }
	}
	dataSheet = new int[rows][COLS];
	   
	int dataSheetRow = 0;
	for(Group group : groupsList) {
	    
	    for(Discipline discipline : group.disciplines) {
		dataSheet[dataSheetRow][0] = group.getId();
		dataSheet[dataSheetRow][1] = discipline.getId(); 
		dataSheet[dataSheetRow][FIELD_WEIGHT] = discipline.Weight;
		dataSheetRow++;
	    }
	    
	}
	orderDataSheet();
	
    }
    public void orderDataSheet() {
	for(int rowDataSheet = 0; rowDataSheet < dataSheet.length; rowDataSheet++ ) {
	    for(int orderRow = 0; orderRow < dataSheet.length - rowDataSheet - 1; orderRow++ ) {
		if(dataSheet[orderRow][FIELD_WEIGHT] < dataSheet[orderRow + 1][FIELD_WEIGHT]) {
		    int[] tmp = dataSheet[orderRow];
		    dataSheet[orderRow] = dataSheet[orderRow+1];
		    dataSheet[orderRow+1] = tmp;
		}
	    }
	}

    }
    public void printObjects() {
	for(int i = 0; i < disciplinesList.size(); i++) {
	    System.out.println("discipline id: " + disciplinesList.get(i).getId());
	    System.out.println("discipline days: " + (disciplinesList.get(i).getEnd() - disciplinesList.get(i).getStart()));

	}
	for(int i = 0; i < groupsList.size(); i++) {
	    System.out.println("group id: " + groupsList.get(i).getId());
	}
	for(int i = 0; i < teachersList.size(); i++) {
	    System.out.println("teacher id: " + teachersList.get(i).getId());
	}
	for(int i = 0; i < lecturesList.size(); i++) {
	    System.out.println("lectures id: " + lecturesList.get(i).getId());
	}
    }
    public void printDataSheet() {
	for(int i = 0; i < dataSheet.length ; i++) {
	    System.out.println();
	    for(int j = 0; j < dataSheet[0].length; j++) {
		if(j == 0) {
		    for(Group g : groupsList) {
			if(g.getId() == dataSheet[i][0])
			    System.out.print(g.getName() + " ");
		    }
		    continue;
		}
		System.out.print(dataSheet[i][j] + " ");
	    }
	}
    }
    
    public int findDisciplineIndexById(int id) {
	for(int i = 0; i < disciplinesList.size(); i++) {
	    if(disciplinesList.get(i).getId() == id) return i;
	}
	return -1;
	
    }
    
    public int findLectureIndexById(int id) {
	for(int i = 0; i < lecturesList.size(); i++) {
	    if(lecturesList.get(i).getId() == id) return i;
	}
	return -1;
	
    }
    
    public int findTeacherIndexById(int id) {
	for(int i = 0; i < teachersList.size(); i++) {
	    if(teachersList.get(i).getId() == id) return i;
	}
	return -1;
	
    }
    
    public void generateSchedule() {
	//TODO сгенерить расписание собсна
	int[][] b = generateRow();
	System.out.println("\ngroup discipline teacher lecture");
	for(int i = 0; i < b.length; i++) {
	    System.out.println();
	    for(int j = 0; j < b[0].length; j++) {
		System.out.print(b[i][j] + "\t");
	    }
	}
    }
    
    public int[][] generateRow() {
	for(Teacher t : teachersList)
	    t.busy = false;
	for(Lecture l : lecturesList)
	    l.busy = false;
	int[][] scheduleRow = new int[groupsList.size()][4];
	int srIndex = 0;
      for(Group g : groupsList) {
	  scheduleRow[srIndex][0] = g.getId();
	for(int i = 0; i < dataSheet.length; i++) {
	    if(dataSheet[i][2] == maxWeightByGroup(g.getId()) && disciplinesList.get(findDisciplineIndexById(dataSheet[i][1])).canUse && scheduleRow[srIndex][0] == dataSheet[i][0]){
		scheduleRow[srIndex][1] = dataSheet[i][1];
		disciplinesList.get(findDisciplineIndexById(dataSheet[i][1])).decrementHours();
		disciplinesList.get(findDisciplineIndexById(dataSheet[i][1])).Weight+=2;

		scheduleRow[srIndex][2] = findNonBusyTeacherId(dataSheet[i][1]);
		
		scheduleRow[srIndex][3] = findNonBusyLectureId(dataSheet[i][1]);
	    }
	    
	}
	srIndex++;
      }
	return scheduleRow;
    }
    
    public int maxWeightByGroup(int groupId) {
	int max = dataSheet[0][2];
	for(int i = 0; i < dataSheet.length; i++) {
	    if(dataSheet[i][0] == groupId && dataSheet[i][2] > max) {
		max = dataSheet[i][1];
	    }
	}
	return max;
    }
    
    public int findNonBusyTeacherId(int disciplineId) {
	int teacherId = -10;
	for(Discipline d : disciplinesList) {
	    if(d.getId() == disciplineId) {
        	    for(Integer t : d.teachers) {
        		if(!teachersList.get(findTeacherIndexById(t)).busy) {
        		    teacherId = t;
        		}
        	    }
	    }
	}
	teachersList.get(findTeacherIndexById(teacherId)).busy = true;

	return teacherId;
    }
    
    public int findNonBusyLectureId(int disciplineId) {
	int lectureId = 0;
	for(Lecture l : lecturesList) {
	    if(((disciplinesList.get(findDisciplineIndexById(disciplineId)).getType() == l.getType()) || (disciplinesList.get(findDisciplineIndexById(disciplineId)).getType() == false && l.getType() == true)) && !l.busy) {
		lectureId = l.getId();
	    }

	}
	lecturesList.get(findLectureIndexById(lectureId)).busy = true;

	return lectureId;
    }

}

