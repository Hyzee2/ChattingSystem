package user;

public class Participants {
	int roomId;
	String userId;
	
	public Participants(int roomId, String userId) {
		this.roomId = roomId;
		this.userId = userId;
	}

	public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
