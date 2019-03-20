package Schedule;
import java.net.ServerSocket;
import java.net.Socket;
import java.io.IOException;
 
public class MultiThreadedServer implements Runnable{
 
    protected int          serverPort   = 11119;
    protected ServerSocket serverSocket = null;
    protected boolean      isStopped    = false;
 
    public MultiThreadedServer(int port){
        this.serverPort = port;
    }
 
    public void run(){
        openServerSocket();
        while(! isStopped()){
            Socket clientSocket = null;
            try {
                clientSocket = this.serverSocket.accept();
            } catch (IOException e) {
                if(isStopped()) {
                    System.out.println("Server Stopped.") ;
                    return;
                }
                throw new RuntimeException("Error accepting client connection", e);
            }
            new Thread(
                new ClientThread(clientSocket)
            ).start();
        }
        System.out.println("Server Stopped.") ;
    }
 
 
    private synchronized boolean isStopped() {
        return this.isStopped;
    }
 
    public synchronized void stop(){
        this.isStopped = true;
        try {
            this.serverSocket.close();
        } catch (IOException e) {
            throw new RuntimeException("Error closing server", e);
        }
    }
 
    private void openServerSocket() {
        System.out.println("Opening server socket...");
     try {
            this.serverSocket = new ServerSocket(this.serverPort);
        } catch (IOException e) {
            throw new RuntimeException("Cannot open port " + this.serverPort, e);
        }
    }
 
}