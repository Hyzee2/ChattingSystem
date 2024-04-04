package user;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO { // Message 테이블과의 연동 관리 
	Connection conn;
	PreparedStatement pstmt; // prepared
	
	//String message;
	List<String> messages;
	List<String> senderIds;
	
	private static final String SELECT_ROOM_MESSAGE = "select content from Message where roomId = ? order by messageId asc";
	private static final String SELECT_SENDERID = "select senderId from Message where roomId = ?";
	
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
	
	public List<String> searchMessage(int roomId) { //채팅방별 메시지 불러오는 메소드
		List<String> messages = new ArrayList<>(); // 메시지를 저장할 리스트
		
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ROOM_MESSAGE)) {
			pstmt.setInt(1, roomId);
	        
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            String message = rs.getString("content");
	            messages.add(message);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	
	    return this.messages = messages;
	}
	
	public List<String> searchSender(int roomId) { // 채팅방별 보낸 사람 아이디가 누구인지 조회하는 메소드
		String targetSenderId = "";
		List<String> senderIds = new ArrayList<>();
		
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_SENDERID)) {
			pstmt.setInt(1, roomId);
	        
	        ResultSet rs = pstmt.executeQuery();

	        while (rs.next()) {
	            targetSenderId = rs.getString("senderId");
	            senderIds.add(targetSenderId);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
		return this.senderIds = senderIds;
	} 
	
	public void inputMessage() { // Message 테이블에 데이터 insert 하는 메소드
		
		
	}
	
}
