package user;

import java.sql.Date;
import java.sql.Timestamp;
import java.time.LocalDate;

public class Login {
	private String userId;
	private Timestamp date;   
	
	public Login(String userId, Timestamp date) {
		this.userId = userId;
		this.date = date;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Timestamp getDate() {
		return date;
	}
	public void setDate(Timestamp date) {
		this.date = date;
	}

}
