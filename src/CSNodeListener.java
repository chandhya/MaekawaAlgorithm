import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;


public class CSNodeListener extends Thread {
	
	public Node node;
	Socket peer = null;
	ObjectInputStream in;
	String msg1;
	String msg2;
	CSNodeListener(Socket peer, Node node)
	{
		this.peer = peer;
		this.node = node;
	}
	@Override
	public void run()
	{
		try {
			in = new ObjectInputStream(peer.getInputStream());
		
		System.out.println("executing Critical Section");
		while(true)
		{
			
			Message m = (Message) in.readObject();
			if(m.messageType == MessageType.CSEXEREQ)
			{
				System.out.println("Node "+node.NodeId+" enters critical Section");
				BufferedWriter bw = new BufferedWriter((new FileWriter("logs.txt")));
				msg1 = "Nodeid:  "+node.NodeId + ", "+"ReqId: "+m.reqID+", "+"Start time: "+m.timeStamp;
				bw.write(msg1);
				Thread.sleep(Main.CST);
				msg2 = "Nodeid:  "+node.NodeId + ", "+"ReqId: "+m.reqID+", "+"Finish time: "+m.timeStamp;
				bw.write(msg2);
				bw.close();
				
				OutgoingMessageThread ot  = new OutgoingMessageThread(MessageType.ACK,node,"");
				ot.start();
				
				//
				
				//CSNodeListener csnl = new CSNodeListener();
				//csnl.start();
				
				break;
			}
			
		}
		} catch (IOException | ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		


		
	}

}
