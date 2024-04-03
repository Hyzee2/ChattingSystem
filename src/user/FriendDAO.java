package user;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FriendDAO {
	Connection conn;
	PreparedStatement pstmt; // prepared
	
	// 친구 관계 추가
    private static final String INSERT_FRIENDS_SQL = "INSERT INTO Friends (user1_id, user2_id) VALUES (?, ?);";

    // 특정 사용자의 모든 친구 조회
    private static final String SELECT_ALL_FRIENDS = "SELECT * FROM Users WHERE user_id IN (SELECT user2_id FROM Friends WHERE user1_id = ? UNION SELECT user1_id FROM Friends WHERE user2_id = ?);";

	
	public FriendDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/miniproject1?serverTimezone=UTC", "root", "qwe123!@#");
		// 아래 생성자 이용
		System.out.println("DB 연결에 성공했습니다.");
	}

	public FriendDAO(String url, String user, String pw) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	 public void addFriendship(String user1Id, String user2Id) {
	        try (
	             PreparedStatement preparedStatement = conn.prepareStatement(INSERT_FRIENDS_SQL)) {
	            preparedStatement.setString(1, user1Id);
	            preparedStatement.setString(2, user2Id);
	            preparedStatement.executeUpdate();
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	    }
	 
	 public List<User> getAllFriends(String inputuserId) {
	        List<User> friends = new ArrayList<>();
	        try (
	             PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ALL_FRIENDS)) {
	            preparedStatement.setString(1, inputuserId);
	            preparedStatement.setString(2, inputuserId);
	            ResultSet rs = preparedStatement.executeQuery();

	            while (rs.next()) {
	            	String userId = rs.getString("user_id");
					String username = rs.getString("username");
					String password = rs.getString("password");
					String email = rs.getString("email");
					String mobilePhone = rs.getString("mobilePhone");
	                // 다른 필드 초기화
	                User user = new User(userId, username, password, email, mobilePhone); // User 객체 생성 및 초기화
	                friends.add(user);
	            }
	        } catch (SQLException e) {
	            printSQLException(e);
	        }
	        return friends;
	    }
	 
	 private void printSQLException(SQLException ex) {
	        for (Throwable e : ex) {
	            if (e instanceof SQLException) {
	                e.printStackTrace(System.err);
	                System.err.println("SQLState: " + ((SQLException) e).getSQLState());
	                System.err.println("Error Code: " + ((SQLException) e).getErrorCode());
	                System.err.println("Message: " + e.getMessage());
	                Throwable t = ex.getCause();
	                while (t != null) {
	                    System.out.println("Cause: " + t);
	                    t = t.getCause();
	                }
	            }
	        }
	    }
	
}
