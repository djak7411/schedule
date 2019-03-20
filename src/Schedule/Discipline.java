package Schedule;
import java.util.ArrayList;
import java.util.Date;

public class Discipline {
    
    private int id = 0;
    private String name = null;
    private long start = 0;
    private long end = 0;
    private int hours = 0;
    private boolean type = false;
    public boolean canUse = true;
    public int Weight = 0;
    public int counter = 0;
    public ArrayList<Integer> teachers = new ArrayList<Integer>();
    
    public Discipline(int id, String name, long start, long end, int hours, boolean type) throws Exception{
	try {
        	setId(id);
        	setName(name);
        	setStart(start);
        	setEnd(end);
        	setHours(hours);
        	setType(type);
        	recountWeight();
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
    public long getStart() {
        return start;
    }
    public void setStart(long start) throws Exception{
	if(start > 0)
	    this.start = start;
	else throw new Exception("invalid date null");
    }
    public long getEnd() {
        return end;
    }
    public void setEnd(long end) throws Exception{
	if(end > 0)
	    this.end = end;
	else throw new Exception("invalid date null");
    }
    public int getHours() {
        return hours;
    }
    public void setHours(int hours) throws Exception{
	if(hours > 0)
	    this.hours = hours;
	else throw new Exception("invalid hours " + String.valueOf(hours));
    }
    public boolean getType() {
        return type;
    }
    public void setType(boolean type) throws Exception{
	try {
	    this.type = type;
	}
	catch(Exception e) {
	    throw new Exception("invalid type " + String.valueOf(type));
	}
    }
    
    public void decrementHours() {
	if(this.hours>=2) {
	    this.hours-=2;
	    recountWeight();
	    this.counter++;
	}
	else
	    canUse = false;
    }
    public boolean canUse() {
	if(this.hours > 0 ) return true;
	else return false;
    }
    
    public void recountWeight() {
	double w = 0;
	int typeRate = 0;
	if (type) typeRate = 1;
	double time = this.end - this.start;
	float coef = ((float)1+typeRate)/((float)this.hours);
	w = coef / time;
	this.Weight = (int) (w * 100000);
    }
}
