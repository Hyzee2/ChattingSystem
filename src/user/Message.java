package user;

import java.util.Date;

public class Message {
	 private User sender;
	    private String content;
	    private Date sentTime;

	    public Message(User sender, String content) {
	        this.sender = sender;
	        this.content = content;
	        this.sentTime = new Date();
	    }

	    // Getter 메서드 생략
}
