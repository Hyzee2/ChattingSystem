package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
	Connection conn;
	PreparedStatement pstmt; // prepared
	
	String message;
	List<String> messages;
	
	private static final String SELECT_ROOM_MESSAGE = "select content from Message where roomId = ? order by messageId asc";
	
	public MessageDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/mychat?serverTimezone=UTC", "root", "qwe123!@#");
		// 아래 생성자 이용
		System.out.println("DB 연결에 성공했습니다.");
	}

	public MessageDAO(String url, String user, String pw) {

		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			conn = DriverManager.getConnection(url, user, pw);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<String> searchMessage(int roomId) { //db에 저장된 채팅방별 메시지 불러오는 메소드
		List<String> messages = new ArrayList<>(); // 메시지를 저장할 리스트
		try (PreparedStatement preparedStatement = conn.prepareStatement(SELECT_ROOM_MESSAGE)) {
	        preparedStatement.setInt(1, roomId);
	        
	        ResultSet rs = preparedStatement.executeQuery();

	        if (rs.next()) {
	            String message = rs.getString("content");
	            messages.add(message);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return messages;
	}
}
