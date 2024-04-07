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
	private List<Participants> parlist;

	private User user;
	private String userId;

	private static final String SELECT_ROOMLIST = "select roomname from Chatroom where roomId in (select roomId from Participants where userId = ?)";
	private static final String SELECT_ONE_ROOMID = "SELECT roomId FROM Chatroom where roomname = ?";
	private static final String INSERT_CHATROOM = "insert into Chatroom (roomname) values(?)";
	private static final String INSERT_PARTICIPANTS = "insert into Participants values (?,?)";
	private static final String SELECT_ALL_PARTICIPANTS = "select userId from Participants where roomId = ?";
	private static final String OUT_PARTICIPANTS = "delete from Participants where roomId = ? and userID = ?";
	private static final String SELECT_ALL_CHAT = "select * from Participants";

	public ChatroomDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/mychat?serverTimezone=UTC", "root", "375@hyunji"); // qwe123!@# //375@hyunji
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

	/**
	 * 모든 채팅방의 roomId와 userId의 내용 조회
	 * 
	 * @return : roomId와 userId의 정보가 들어있는 Participants 반환
	 */
	public List<Participants> searchChatroom() {
		List<Participants> parlist = new ArrayList<>();

		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_CHAT)) {

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				int roomId = rs.getInt("roomId");
				String userId = rs.getString("userId");
				Participants par = new Participants(roomId, userId);
				parlist.add(par);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return this.parlist = parlist;
	}

	/**
	 * 채팅 참여자 강퇴 기능
	 * 
	 * @param roomId
	 * @param userId
	 */
	public void outMembers(int roomId, String userId) {
		try (PreparedStatement pstmt = conn.prepareStatement(OUT_PARTICIPANTS)) {
			pstmt.setInt(1, roomId);
			pstmt.setString(2, userId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			;
		}
	}

	/**
	 * 채팅방 참여자들의 userId를 조회
	 * 
	 * @param roomId
	 * @return : 채팅방 참여자들의 userId
	 */
	public List<String> selectMembers(int roomId) {
		List<String> selectedMembers = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_PARTICIPANTS)) {
			pstmt.setInt(1, roomId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String userId = rs.getString("userId");
				selectedMembers.add(userId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return selectedMembers;
	}

	/**
	 * 현재 로그인한 사용자가 속한 채팅방 목록 조회
	 * 
	 * @param userId
	 * @return : 참여하고 있는 채팅방 목록
	 */
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

	/**
	 * 채팅방 번호 조회
	 * 
	 * @param roomname
	 * @return :채팅방 번호
	 */
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

	/**
	 * 새로운 채팅방 생성 시 Chatroom, Participants DB에 정보 업데이트
	 * 
	 * @param roomname
	 * @param user
	 * @return : 채팅방 번호
	 * @throws SQLException
	 */
	public int inputChatRoom(String roomname, User user) throws SQLException { // 채팅방 새로 생성시, Chatroom 테이블과 Participants
																				// 테이블에
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

	/**
	 * 채팅방 새로 생성 시 chatroom DB에 정보 업데이트
	 * 
	 * @param roomname
	 * @return
	 * @throws SQLException
	 */
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

	/**
	 * Participants DB에 정보 업데이트
	 * 
	 * @param roomId
	 * @param userId
	 * @throws SQLException
	 */
	public void addParticipantToRoom(int roomId, String userId) throws SQLException {

		try (PreparedStatement pstmt = conn.prepareStatement(INSERT_PARTICIPANTS)) {
			pstmt.setInt(1, roomId);
			pstmt.setString(2, userId);
			pstmt.executeUpdate();
		}
	}

}
