package user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO { // Users 테이블과의 연동 관리
	Connection conn;
	PreparedStatement pstmt; // prepared

	private static final String INSERT_USERS_SQL = "INSERT INTO Users"
			+ "  (userId, username, password, roomId) VALUES " + " (?, ?, ?, ?);";
	private static final String SELECT_ALL_USERS = "SELECT * FROM Users";

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

	public List<User> selectAllUsers() {
		List<User> users = new ArrayList<>();
		try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_USERS)) {
			ResultSet rs = preparedStatement.executeQuery();

			while (rs.next()) {
				String userId = rs.getString("userId");
				String username = rs.getString("username");
				String password = rs.getString("password");
				int roomId = rs.getInt("roomId");
				
				users.add(new User(userId, username, password, roomId));
			}
		} catch (SQLException e) {
			printSQLException(e);
		}
		return users;
	}

	public void addUser(User user) {
		try (PreparedStatement preparedStatement = conn.prepareStatement(INSERT_USERS_SQL)) {
			preparedStatement.setString(1, user.getUserId());
			preparedStatement.setString(2, user.getUsername());
			preparedStatement.setString(3, user.getPassword());
			preparedStatement.setInt(4, user.getRoomId());
		
			preparedStatement.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}
	
	public User userSearch(String input) { // id 혹은 이름으로 user 찾기 
		User user = null;
	    String sql = "SELECT * FROM Users WHERE userId = ? OR username = ?";

	    try (PreparedStatement preparedStatement = conn.prepareStatement(sql)) {
	        preparedStatement.setString(1, input);
	        preparedStatement.setString(2, input);
	        ResultSet rs = preparedStatement.executeQuery();

	        if (rs.next()) {
	            String userId = rs.getString("userId");
	            String username = rs.getString("username");
	            String password = rs.getString("password");
	            int roomId = rs.getInt("roomId");
	        
	            user = new User(userId, username, password, roomId);
	        }
	    } catch (SQLException e) {
	        printSQLException(e);
	    }
	    return user; // id 혹은 이름을 입력받아서 친구인 사용자를 찾아서 반환해준다. 
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
