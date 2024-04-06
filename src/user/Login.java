package user;

import java.sql.Date;
import java.time.LocalDate;

public class Login {
	private String userId;
	private Date date; // 날짜 데이터 타입말고 나중에 시간도 같이 나오는 타입으로 수정하기  
	
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
