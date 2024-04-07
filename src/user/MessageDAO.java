package user;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO { // Message 테이블과의 연동 관리
	Connection conn;
	PreparedStatement pstmt; // prepared

	// String message;
	List<Message> messages; // Message 타입
	List<String> senderIds;
	List<LocalDate> times;

	private static final String SELECT_ROOM_MESSAGE = "select senderId, content from Message where roomId = ?";
	private static final String SELECT_SENDERID = "select senderId from Message where roomId = ?";
	private static final String UPDATE_MESSAGE = "insert into Message (roomId, senderId, content, time) values (?,?,?, NOW())";
	private static final String WHAT_DATE = "select distinct date(time) as date from Message where roomId = ? order by date(time)";
	private static final String SELECT_MESSAGE = "select senderId, content from Message where date(time) = ? and roomId = ?";

	public MessageDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/mychat?serverTimezone=UTC", "root", "375@hyunji");
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

	/**
	 * 메시지 전송 시 Message 테이블에 업데이트
	 * 
	 * @param messageText
	 * @param roomId
	 * @param senderId
	 */
	public void inputMessage(String messageText, int roomId, String senderId) {

		try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_MESSAGE)) {
			pstmt.setInt(1, roomId);
			pstmt.setString(2, senderId);
			pstmt.setString(3, messageText);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 참여한 채팅방의 메시지들의 날짜 정보 조회
	 * 
	 * @param roomId
	 * @return times
	 */
	public List<LocalDate> searchDate(int roomId) {
		List<LocalDate> times = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(WHAT_DATE)) {
			pstmt.setInt(1, roomId);

			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				// 'date'라는 별칭으로 조회
				Date date = rs.getDate("date");
				LocalDate localDate = date.toLocalDate();

				times.add(localDate);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return this.times = times;

	}

	/**
	 * 해당 채팅방의 날짜별로 채팅 내용 조회하는 기능
	 * 
	 * @param date, roomId
	 * @return senderId, content가 담긴 message 객체의 리스트 반환
	 */
	public List<Message> getMessage(LocalDate date, int roomId) {
		List<Message> messages = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_MESSAGE)) {
			pstmt.setDate(1, Date.valueOf(date)); // LocalDate를 SQL Date로 변환
			pstmt.setInt(2, roomId);
			ResultSet rs = pstmt.executeQuery();
			while (rs.next()) {
				String senderId = rs.getString("senderId");
				String content = rs.getString("content");
				messages.add(new Message(senderId, content));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return messages;
	}

	/**
	 * 채팅방별 보낸사람 Id와 채팅내용 조회
	 * 
	 * @param roomId
	 * @return messages : senderId + message
	 */
	public List<Message> searchMessage(int roomId) {
		List<Message> messages = new ArrayList<>(); // 추가를 위해 arrayList 선언

		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ROOM_MESSAGE)) {
			pstmt.setInt(1, roomId);

			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				String senderId = rs.getString("senderId");
				String content = rs.getString("content");

				Message message = new Message(senderId, content); // 객체 타입의 Message를 생성하여 senderIddhk content를 담아준다.

				messages.add(message); // 배열에 추가
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println("나는 DAO에서 실행중 ");
		System.out.println(messages.toString());

		return this.messages = messages;
	}

	/**
	 * 채팅방별 senderId가 본인 여부 조회
	 * 
	 * @param roomId
	 * @return senderIds : 채팅방별 존재하는 senderId의 목록들 반환
	 */
	public List<String> searchSender(int roomId) {
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

}
