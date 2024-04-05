package user;
import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {

	private ArrayList<MultiServerThread> list;
	private Socket socket;
	
	public MultiServer()throws IOException{
		list = new ArrayList<MultiServerThread>();
		
		ServerSocket serverSocket = new ServerSocket(8050);
		MultiServerThread mst = null;
		boolean isStop = false;
		
		while(!isStop){
			System.out.println("Server ready...");
			socket = serverSocket.accept();
			
			mst = new MultiServerThread(this);
			list.add(mst);
			
			Thread t = new Thread(mst);
			t.start();
		}
	}
	public ArrayList<MultiServerThread>getList(){
		return list;
	}
	public Socket getSocket()
	{
		return socket;
	}
	public static void main(String arg[])throws IOException{
		new MultiServer();
	}
	
}
