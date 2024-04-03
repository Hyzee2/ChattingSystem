package user;
import java.util.*;

class User {
    private String userId;
    private String username;
    private String password;
    private int roomId;
 
    //private List<Friend> friends = new ArrayList<>();  

    public User(String userId, String username, String password, int roomId) {
        this.userId = userId;
    	this.username = username;
        this.password = password;
        this.roomId = roomId;
        //this.friends = new ArrayList<>(); // User를 생성할 때 친구 배열을 생성한다. 
    }

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	
    
}