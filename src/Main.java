import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
public class Main {
	final static Charset ENCODING = StandardCharsets.UTF_8;
	public static String INPUT_FILE_PATH = "input.txt";
	public static List<String> lines;
	public static InetSocketAddress thisNode;
	public static Socket peer;
	public static Node currentNode; 
	public static int NO_OF_NODES;
	public static int COMMON_SERVER_PORT = 1918;
	public static ObjectInputStream[] peerInput;
	public static ObjectOutputStream[] peerOutput;
	public static Socket[] peerSocket;
	public static InetSocketAddress[] PEERS;
	public static int CST;
	public static int[] quorum;
	public static int startingNode = 35; //Assign the value of the starting node depending on which node inititates the process
	public static void main(String args[])
	{

		Path sampleFilePath = Paths.get(INPUT_FILE_PATH);
			try {
				lines = Files.readAllLines(sampleFilePath, ENCODING);
				NO_OF_NODES =  Integer.parseInt(String.valueOf(lines.get(0).charAt(2)));
				CST = Integer.parseInt(lines.get(3).substring(4));
				thisNode = new InetSocketAddress(InetAddress.getLocalHost().getHostName(),
		    			COMMON_SERVER_PORT);
				Node tempNode =  new Node();
				for(int j=0;j<=NO_OF_NODES;j++)
				{
					if(thisNode.getHostName().substring(4,6).equals(startingNode))
					{
					tempNode.NodeId = j;
					break;
					}
					startingNode++;
				}
				if(tempNode.NodeId== NO_OF_NODES)
				{
						tempNode.NodeId = 22;
				}
				tempNode.portNo = thisNode.getPort();
				tempNode.hostName = thisNode.getHostName();
				currentNode = tempNode;
				//To populate current node's quorum
				for(int k=4,i=0;k<lines.size();k++)
				{
				if(currentNode.NodeId == lines.get(k).charAt(1))
				{
					quorum[i] = Integer.parseInt(lines.get(k).substring(3));
					i++;
				}
				
				}
				//If the current node is the csnode,then spawn the CS Listener thread
				if(thisNode.getHostString().substring(4,6).equals(startingNode+NO_OF_NODES))
				{
					peer = new Socket(thisNode.getHostString(),thisNode.getPort());
					CSNodeListener csNL = new CSNodeListener(peer,currentNode);
					csNL.start();
				}
				//Starts the process of sending requests to all nodes in the quorum                
				InitiateProcess();
				
			}
			catch (IOException e) {
			
				e.printStackTrace();
			}
		
	}
	
	//Method used to initiate the process of nodes sending request to enter critical section
	public static void InitiateProcess() {
		
		Thread it = new IncomingMessageThread(currentNode);
		peerInput = new ObjectInputStream[NO_OF_NODES];
		peerOutput = new ObjectOutputStream[NO_OF_NODES];
		
		
		if(currentNode.NodeId == 0)
		{
			IncomingMessageThread imt =  new IncomingMessageThread(currentNode);
			imt.start();
			OutgoingMessageThread ot = new OutgoingMessageThread(MessageType.INITITATE,currentNode,"");
			ot.start();
		}
		else if(currentNode.NodeId > 0)
		{
			IncomingMessageThread imt =  new IncomingMessageThread(currentNode);
			imt.start();
		}
	//critical section node
		/*else if(currentNode.NodeId == 22)
		{
			IncomingMessageThread imt =  new IncomingMessageThread(currentNode);
			imt.start();
		}*/
		/*for(int l=0;l<quorum.length;l++)
		{
			OutgoingMessageThread ot = new OutgoingMessageThread(currentNode);
			ot.start();
		}*/
	}

}
