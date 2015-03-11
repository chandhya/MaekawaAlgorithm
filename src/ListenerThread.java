
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

public class ListenerThread extends Thread
{

private Socket peer =null;
private ObjectInputStream in = null;
private OutgoingMessageThread ot;
public ListenerThread(Socket peer,OutgoingMessageThread ot)
{
	this.peer = peer;
	this.ot= ot;
}
@Override
public void run()
{
		try {
			in = new ObjectInputStream(peer.getInputStream());
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	
	System.out.println("Receiving the input stream");
	while(true)
	{
		
		Message m;
		try {
			m = (Message) in.readObject();
		if(m.messageType == MessageType.RESPONSE)
		{
			System.out.println("Node "+ot.node.NodeId+" enters critical Section");
			OutgoingMessageThread ot = new OutgoingMessageThread(MessageType.CSEXEREQ,Main.currentNode,"");
			ot.start();
			//CSNodeListener csnl = new CSNodeListener();
			//csnl.start();
			break;
		}
	}
  
	catch (IOException e) {

	e.printStackTrace();
}
	catch (ClassNotFoundException e) {

		e.printStackTrace();
	}
	}
}
}
