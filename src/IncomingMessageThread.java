import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.ServerSocket;
import java.net.Socket;



public class IncomingMessageThread extends Thread {
	
	Node currentNode = null;
	
	Socket peer = null;
	ObjectInputStream in = null;
	Message m = null;
	public IncomingMessageThread(Node currentNode) {
	
		
	this.currentNode = currentNode;
	}
	
	@Override
	public void run()
	{
		ServerSocket server = null;
		try {
			
			server = new ServerSocket(currentNode.portNo);
			while(true)
		{
			//try{
			
				peer = server.accept();
				
				ServerListeningThread sl = new ServerListeningThread(peer);
				sl.start();
				CSNodeListener csl = new CSNodeListener(peer,Main.currentNode);
				csl.start();
				//server.close();
				break;
		}
		
		}catch (IOException e) 
		{
		
		e.printStackTrace();
	}
		
	}
	


}
