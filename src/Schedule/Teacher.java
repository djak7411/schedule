package Schedule;
import java.util.ArrayList;

public class Teacher {
    
    private int id = 0;
    private String fio = null;
    public boolean busy = false;
    public float weight = 0;

    
    public Teacher(int id, String fio) throws Exception{
	try {
        	setId(id);
        	setFio(fio);
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
    public String getFio() {
        return fio;
    }
    public void setFio(String fio) throws Exception{
	if(fio != null && !fio.equals(' '))
	    this.fio = fio;
	else throw new Exception("invalid number " + String.valueOf(fio));
    }


}
