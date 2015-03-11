public class Message implements  Comparable<Message> {
	

	
	int timeStamp; // Lamports clock
	String sentBy; // Senders address
	MessageType messageType; //Send or acknowledge
	int reqID;
	public Message(int timeStamp, String sentBy,
			MessageType messageType,int reqID) {
		super();
		this.timeStamp = timeStamp;
		this.sentBy = sentBy;
		this.messageType = messageType;
		this.reqID = reqID;
		
	}

	public int getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(int timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getSentBy() {
		return sentBy;
	}

	public void setSentBy(String sentBy) {
		this.sentBy = sentBy;
	}

	public MessageType getMessageType() {
		return messageType;
	}

	public void setMessageType(MessageType messageType) {
		this.messageType = messageType;
	}

	public int getReqID() {
		return reqID;
	}

	public void setReqID(int reqID) {
		this.reqID = reqID;
	}
	
	@Override
	public int compareTo(Message msg) {
		int compareQuantity = ((Message) msg).getTimeStamp(); 
		 
		//order requests in queue in descending order based on timestamp
		return compareQuantity - this.timeStamp;
	
	}
		}

