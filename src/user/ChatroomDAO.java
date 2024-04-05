package user;

import java.sql.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class ChatroomDAO {

	Connection conn;
	PreparedStatement pstmt; // prepared

	private int roomId;
	private String roomname;
	private List<String> roomlist;

	private User user;
	private String userId;

	private static final String SELECT_ROOMLIST = "select roomname from Chatroom where roomId in (select roomId from Participants where userId = ?)";
	private static final String SELECT_ONE_ROOMID = "SELECT roomId FROM Chatroom where roomname = ?";
	private static final String INSERT_CHATROOM = "insert into Chatroom (roomname) values(?)";
	private static final String INSERT_PARTICIPANTS = "insert into Participants values (?,?)";

	public ChatroomDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/mychat?serverTimezone=UTC", "root", "qwe123!@#"); //qwe123!@#
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

	public List<String> searchRoomList(String userId) {
		List<String> roomlist = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ROOMLIST)) {
			pstmt.setString(1, userId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String roomname = rs.getString("roomname");
				roomlist.add(roomname);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return this.roomlist = roomlist;
	}

	public int searchRoomId(String roomname) { // roomId는 중복되지 않은 pk이다. 결과값은 1개만 나온다.
		int targetRoomId = 0;
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ONE_ROOMID)) {
			pstmt.setString(1, roomname);
			ResultSet rs = pstmt.executeQuery();

			if (rs.next()) {
				targetRoomId = rs.getInt("roomId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return this.roomId = targetRoomId;
	}

	public int inputChatRoom(String roomname, User user) throws SQLException { // 채팅방 새로 생성시, Chatroom 테이블과 Participants 테이블에
																		// insert
		int targetRoomId = 0;

		// Chatroom 테이블에 채팅방 추가
	    try (PreparedStatement pstmt = conn.prepareStatement(INSERT_CHATROOM, Statement.RETURN_GENERATED_KEYS)) {
	        pstmt.setString(1, roomname);
	        pstmt.executeUpdate();
	        // 생성된 roomId 얻기
	        try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
	            if (generatedKeys.next()) {
	                targetRoomId = generatedKeys.getInt(1);
	            }
	        }
	    }

	    // Participants 테이블에 사용자 추가
	    try (PreparedStatement pstmt3 = conn.prepareStatement(INSERT_PARTICIPANTS)) {
	        pstmt3.setInt(1, targetRoomId);
	        pstmt3.setString(2, user.getUserId());
	        pstmt3.executeUpdate();
	    }
	    return this.roomId = targetRoomId; 
	}
	
	public int createChatRoom(String roomname) throws SQLException {
	    // Chatroom 테이블에 새 채팅방 추가
	    try (PreparedStatement pstmt = conn.prepareStatement(INSERT_CHATROOM, Statement.RETURN_GENERATED_KEYS)) {
	        pstmt.setString(1, roomname);
	        pstmt.executeUpdate();
	        try (ResultSet rs = pstmt.getGeneratedKeys()) {
	            if (rs.next()) {
	                return rs.getInt(1); // 생성된 채팅방 ID 반환
	            }
	        }
	    }
	    return -1; // 채팅방 생성 실패
	}

	public void addParticipantToRoom(int roomId, String userId) throws SQLException {
	    // Participants 테이블에 새 참가자 추가
	    try (PreparedStatement pstmt = conn.prepareStatement(INSERT_PARTICIPANTS)) {
	        pstmt.setInt(1, roomId);
	        pstmt.setString(2, userId);
	        pstmt.executeUpdate();
	    }
	}
	
}
