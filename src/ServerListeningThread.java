
import java.util.Arrays;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class ServerListeningThread extends Thread{
	Socket peer = null;
	ObjectInputStream in = null;
	OutgoingMessageThread ot = null;
	Message[] requestQueue =null;
	int reqIndex =0;
	public ServerListeningThread(Socket peer) {
			this.peer = peer; 
		}

		@Override
		public void run()
		{
		//	System.out.println("Waiting for input Stream");	
			 
			while(true)
			{
				try {
					
				in = new ObjectInputStream(peer.getInputStream());
					
					Message m = (Message) in.readObject();
					if(m.equals(MessageType.INITITATE))
					{
						ot = new OutgoingMessageThread(MessageType.CSREQUEST ,Main.currentNode,"");
						ot.start();
					}
					else if(m.equals(MessageType.CSREQUEST))
					{
						//APPLY ALGORITHM RELATED CHECKS
						requestQueue[reqIndex] = m;
						reqIndex++;
					}
					Arrays.sort(requestQueue);
					//System.out.println("Message  sent from" + m.sentBy + "Received Time - "+m.timeStamp);
					
					//LamportClockUpdate(m.timeStamp);
					
					} 
					catch ( ClassNotFoundException e) 
					{
						e.printStackTrace();
					} catch (IOException e) {
						e.printStackTrace();
					}
				break;
				}
			for(int i=0;i<reqIndex;i++)
			{
				
				ot = new OutgoingMessageThread(MessageType.RESPONSE ,Main.currentNode,requestQueue[i].sentBy);
				ot.start();
			}
			
			
			
			}
		//Method to perform clock update
		/*void LamportClockUpdate(int timeStamp) 
		{
			if(Main.getTime() > timeStamp)
			{
				Main.setTime(timeStamp); 
			}
		}*/





}
