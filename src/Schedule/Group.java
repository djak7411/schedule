package Schedule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Group {
    
    private int id = 0;
    private String name = null;
    public ArrayList<Discipline> disciplines = new ArrayList<Discipline>();
    public boolean busy = false;
    public float weight = 0;
    public int disciplinesCounter = 0;
    public final int MAX_LESSONS = 4;


    
    public Group(int id, String name) throws Exception{
	try {
        	setId(id);
        	setName(name);

	}catch(Exception e) {
	    throw new Exception(e);
	}
	
	
	
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) throws Exception{
	if(id > 0)
	    this.id = id;
	else throw new Exception("invalid id " + String.valueOf(id));
        
    }
    public String getName() {
        return name;
    }
    public void setName(String name) throws Exception{
	if(name != null && !name.equals(' '))
	    this.name = name;
	else throw new Exception("invalid name " + String.valueOf(name));
    }

    

}
