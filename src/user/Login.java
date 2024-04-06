package user;

import java.sql.Date;
import java.time.LocalDate;

public class Login {
	private String userId;
	private Date date;
	
	public Login(String userId, Date date) {
		this.userId = userId;
		this.date = date;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}

}
