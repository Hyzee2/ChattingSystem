package user;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ChatroomDAO {
	Connection conn;
	PreparedStatement pstmt; // prepared
	
	int roomId;

	private static final String SELECT_ONE_ROOMID = "SELECT roomId FROM Chatroom where userId = ?";
	
	public ChatroomDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/mychat?serverTimezone=UTC", "root", "qwe123!@#");
		// 아래 생성자 이용
		System.out.println("DB 연결에 성공했습니다.");
	}

	public ChatroomDAO(String url, String user, String pw) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public int searchRoomId(String userId) {
		
		try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ONE_ROOMID)) {
	        preparedStatement.setString(1, userId);
	        
	        ResultSet rs = preparedStatement.executeQuery();

	        if (rs.next()) {
	            roomId = rs.getInt("roomId");
	            
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return roomId;
	}

}
