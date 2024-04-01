package user;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class User {

	private int userId;
	private String username;
	private String password;
	private String email;
	private String mobilePhone;
	private String userStatus;
	
	List<User> userList;
	
	public User(int userId, String username, String password, String email, String mobilePhone, String userStatus) {
		this.userId = userId;
		this.username = username;
		this.password = password;
		this.email = email;
		this.mobilePhone = mobilePhone;
		this.userStatus = userStatus;
	}
	
	public static void main(String[] args) {
        // 랜덤하게 10명의 사용자 생성
        List<User> userList = generateRandomUserList(10);
        
        // ArrayList 출력
        for (User user : userList) {
            System.out.println(user);
        }
    }
	
	private static List<User> generateRandomUserList(int count) {
        List<User> userList = new ArrayList<>();

        for (int i = 1; i <= count; i++) {
            userList.add(generateRandomUser(i));
        }

        return userList;
    }
	
	 private static User generateRandomUser(int userId) {
	        Random random = new Random();

	        // 사용자 데이터 생성
	        String username = "user" + random.nextInt(1000);
	        String password = "password" + random.nextInt(1000);
	        String email = "user" + random.nextInt(1000) + "@example.com";
	        String mobilePhone = "010-" + String.format("%08d", random.nextInt(100000000));
	        String userStatus = "Active";

	        // User 객체 생성 및 반환
	        return new User(userId, username, password, email, mobilePhone, userStatus);
	    }
	


	@Override
	public String toString() {
		return "userId=" + userId + "\n" + " username: " + username + "\n" + " password: " + password + "\n"
				+ " email: " + email + "\n" + " mobilePhone: " + mobilePhone + "\n" + " userStatus: " + userStatus
				+ "\n";

	}

}
