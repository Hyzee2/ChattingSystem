package user;

public class Message { 
	private String senderId;
	private String content;
	
	public Message(String senderId, String content){
		this.senderId = senderId;
		this.content = content;
	}
	
	public String getSenderId() {
		return senderId;
	}
	public String getContent() {
		return content;
	}
	
	
}
