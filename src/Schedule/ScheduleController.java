package Schedule;

public class ScheduleController {
    
	

    public static void main(String[] args) {
	 MultiThreadedServer server = new MultiThreadedServer(11119);
	        new Thread(server).start();
	        try {
	            Thread monitor = new StopMonitor(11118);
	            monitor.start();
	            monitor.join();
	            System.out.println("Right after join.....");
	        } catch (InterruptedException e) {
	            e.printStackTrace();
	        }
	        System.out.println("Stopping Server");
	        server.stop();
	    }
	
	
    }



