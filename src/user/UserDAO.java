package user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO { // Users 테이블과의 연동 관리
	Connection conn;
	PreparedStatement pstmt; // prepared
	
	private User user;
	private List<User> users;

	private static final String INSERT_USERS_SQL = "INSERT INTO Users"
			+ "  (userId, username, password) VALUES " + " (?, ?, ?);";
	private static final String SELECT_ALL_USERS = "SELECT * FROM Users";
	private static final String UPDATE_USERNAME = "update Users set username = ? where userId = ?";

	public UserDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/mychat?serverTimezone=UTC", "root", "qwe123!@#");
		// 아래 생성자 이용
		System.out.println("DB 연결에 성공했습니다.");
	}

	public UserDAO(String url, String user, String pw) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public List<User> selectAllUsers() { // Users 테이블에 있는 user 데이터 배열로 불러오기 
		List<User> users;
		
		users = new ArrayList<>(); // add() 기능을 사용하기 위해 ArrayList로 다시 객체 생성 
		
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_USERS)) {
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String userId = rs.getString("userId");
				String username = rs.getString("username");
				String password = rs.getString("password");
				
				users.add(new User(userId, username, password));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return this.users = users;
	}
	
	public void updateUsername(String username, User user) { // 프로필 편집 버튼 누르면 Users 테이블에도 update  
		
		try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_USERNAME)) {
			pstmt.setString(1, username);
			pstmt.setString(2, user.getUserId()); 
			pstmt.executeUpdate();
			
		}catch(SQLException e) {
			e.printStackTrace();
		}

	}

	public void addUser(User user) {
		try (PreparedStatement pstmt = conn.prepareStatement(INSERT_USERS_SQL)) {
			pstmt.setString(1, user.getUserId());
			pstmt.setString(2, user.getUsername());
			pstmt.setString(3, user.getPassword());
		
			pstmt.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	
	public User userSearch(String currentUserId) { // userId로 사용자 객체 반환 
		User user = null;
	    String sql = "SELECT * FROM Users WHERE userId = ?";

	    try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
	    	pstmt.setString(1, currentUserId);
	
	        ResultSet rs = pstmt.executeQuery();

	        if (rs.next()) {
	            String userId = rs.getString("userId");
	            String username = rs.getString("username");
	            String password = rs.getString("password");
	        
	            user = new User(userId, username, password);
	        }
	    } catch (SQLException e) {
	        printSQLException(e);
	    }
	    return this.user = user; 
	}

	private void printSQLException(SQLException ex) {
		for (Throwable e : ex) {
			if (e instanceof SQLException) {
				e.printStackTrace(System.err);
				System.err.println("SQLState: " + ((SQLException) e).getSQLState());
				System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
				System.err.println("Message: " + e.getMessage());
			}
		}
	}
}
