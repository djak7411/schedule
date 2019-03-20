package Schedule;

public class Lecture {
    
    private int id = 0;
    private String number = null;
    private boolean type = false;
    public boolean busy = false;
    public float weight = 0;


    
    public Lecture(int id, String number, boolean type) throws Exception{
	try {
        	setId(id);
        	setNumber(number);
        	setType(type);
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
    public String getNumber() {
        return number;
    }
    public void setNumber(String number) throws Exception{
	if(number != null && !number.equals(' '))
	    this.number = number;
	else throw new Exception("invalid number " + String.valueOf(number));
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
}
