import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
public class OutgoingMessageThread extends Thread {
	
	public Node node;
	private MessageType mt;
	private int reqID=1;
	String receiver;
	public OutgoingMessageThread(MessageType mt,Node node, String receiver)
	{
		this.node = node;
		this.mt = mt;
		this.receiver = receiver; 
	}
	Socket peer = null;
	ObjectOutputStream out = null;
	@Override
	public void run()
	{
		try {
			peer = new Socket(node.hostName,node.portNo);
			out = new ObjectOutputStream(peer.getOutputStream());
			if(node.NodeId == 0)
			{
				if(mt == MessageType.INITITATE)
				{
					Message m = new Message(00, String.valueOf(node.NodeId), MessageType.INITITATE,00);
					out.writeObject(m);
					out.flush();
				}
			}
			else if(node.NodeId >0 )
			{
				if(mt == MessageType.CSREQUEST)
				{
					Message m = new Message(00, String.valueOf(node.NodeId), MessageType.CSREQUEST,00);
					out.writeObject(m);
					out.flush();
						
				}
				else if(mt == MessageType.RESPONSE)
				{
					Message m = new Message(00, String.valueOf(node.NodeId), MessageType.RESPONSE,00);
					out.writeObject(m);
					Main.peerOutput[Integer.parseInt(receiver)] = out;
					out.flush();
				}
				else if(mt == MessageType.CSEXEREQ)
				{
					Message m = new Message(00, String.valueOf(node.NodeId), MessageType.CSEXEREQ,reqID);
					out.writeObject(m);
					out.flush();
					reqID++;
				}
				else if(mt == MessageType.ACK)
				{
					Message m = new Message(00, String.valueOf(node.NodeId), MessageType.ACK,00);
					out.writeObject(m);
					out.flush();
				}
				
			}
			/*Message m = new Message(00, String.valueOf(node.NodeId), MessageType.CSREQUEST);
			out.writeObject(m);
			out.flush();*/
			ListenerThread lt = new ListenerThread(peer,this);
			lt.start();
		} catch ( IOException e) {
		
			e.printStackTrace();
		}
		
		
	}

}
