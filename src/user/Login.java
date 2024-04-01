//package user;
//import java.util.Scanner;
//public class Login { 
//	
//	int user_id;
//	String password;
//	User user;
//	
//	Scanner sc = new Scanner(System.in);
//
//	public int getUser_id() {
//		return user_id;
//	}
//
//	public void setUser_id(int user_id) {
//		this.user_id = user_id;
//	}
//
//	public String getPassword() {
//		return password;
//	}
//
//	public void setPassword(String password) {
//		this.password = password;
//	}
//
//	public User check(User user) {
//		
//		do {
//			System.out.println("id 입력");
//			setUser_id(sc.nextInt());
//			System.out.println("pw 입력");
//			setPassword(sc.next());
//			
//			if (user_id == user.userId() && password == user.password()) {
//				System.out.println("도담도담에 입장 중입니다. ");
//				try {
//					Thread.sleep(2000);
//				} catch (InterruptedException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//				break;
//
//			} else {
//				System.out.println("id 혹은 pw가 틀렸습니다. 다시 시도해주세요.");
//			} 
//		} while (true);
//		
//		return this.user = user;
//	}
//	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		Login login = new Login();
//		User user = new User();
//		
//		login.check(user);
//		
//	}
//
//}
