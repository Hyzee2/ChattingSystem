package user;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDAO { // Users 테이블, Frineds 테이블과의 연동 관리
	Connection conn;
	PreparedStatement pstmt; // prepared

	private User user;
	private List<User> users;
	private List<Login> userLogins;

	private static final String INSERT_USERS_SQL = "INSERT INTO Users (userId, username, password) VALUES (?, ?, ?)";
	private static final String SELECT_ALL_USERS = "SELECT * FROM Users";
	private static final String UPDATE_USERNAME = "update Users set username = ? where userId = ?";
	private static final String SELECT_ONE_USER = "select userId from Users where userId = ?";
	private static final String INSERT_FRIEND = "insert into Friends values (?,?)";
	private static final String INSERT_FRIEND_SAME = "insert into Friends values (?,?)";
	private static final String SELECT_ALL_FRIENDS = "select friendId from Friends where userId = ?";
	private static final String INSERT_LOGIN = "insert into Login values (?, now())";
	private static final String SELECT_USER_LOGIN = "select * from Login where userId = ?";
	private static final String DELETE_USER = "delete from Users where userId = ?";

	public UserDAO() throws SQLException {
		this("jdbc:mysql://localhost:3306/mychat?serverTimezone=UTC", "root", "375@hyunji");
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

	/**
	 * 회원 정보 삭제
	 */
	public void deleteUser(String userId) {
		try (PreparedStatement pstmt = conn.prepareStatement(DELETE_USER)) {
			pstmt.setString(1, userId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	/**
	 * 사용자의 로그인 정보 조회
	 * 
	 * @param userId
	 * @return
	 */
	public List<Login> searchLogin(String userId) {
		List<Login> userLogins = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_USER_LOGIN)) {
			pstmt.setString(1, userId);

			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String targetUserId = rs.getString("userId");
					Timestamp date = rs.getTimestamp("time");
					Login login = new Login(targetUserId, date);
					userLogins.add(login);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return userLogins;
	}

	/**
	 * 사용자의 로그인 정보 업데이트
	 * 
	 * @param userId
	 */
	public void updateLogin(String userId) {
		try (PreparedStatement pstmt = conn.prepareStatement(INSERT_LOGIN)) {
			pstmt.setString(1, userId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}
	}

	/**
	 * 입력받은 userId가 회원인지 조회
	 * 
	 * @param userId
	 * @return
	 */
	public boolean existsUser(String userId) {
		String targetUserId = "";
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ONE_USER)) {
			pstmt.setString(1, userId);
			ResultSet rs = pstmt.executeQuery();

			while (rs.next()) {
				targetUserId = rs.getString("userId");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		if (targetUserId != null) {
			return true;
		} else
			return false;

	}

	/**
	 * friendId를 가진 사용자를 친구로 추가
	 * 
	 * @param userId   : 로그인한 사용자의 userId
	 * @param friendId : 친구로 추가하려는 userId
	 */
	public void addFriend(String userId, String friendId) {
		try (PreparedStatement pstmt = conn.prepareStatement(INSERT_FRIEND)) {
			pstmt.setString(1, userId);
			pstmt.setString(2, friendId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}

		try (PreparedStatement pstmt = conn.prepareStatement(INSERT_FRIEND_SAME)) {
			pstmt.setString(1, friendId);
			pstmt.setString(2, userId);

			pstmt.executeUpdate();
		} catch (SQLException e) {
			printSQLException(e);
		}

	}

	/**
	 * 현재 사용자의 모든 친구 목록 조회
	 * 
	 * @param userId
	 * @return : 로그인한 userId의 친구로 등록되어있는 모든 userId를 반환
	 */
	public List<String> getFriends(String userId) {
		List<String> myFriends = new ArrayList<>();
		try (PreparedStatement pstmt = conn.prepareStatement(SELECT_ALL_FRIENDS)) {
			pstmt.setString(1, userId);
			try (ResultSet rs = pstmt.executeQuery()) {
				while (rs.next()) {
					String friendId = rs.getString("friendId");
					myFriends.add(friendId);
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return myFriends;
	}

	/**
	 * User 데이터 배열로 불러오기
	 * 
	 * @return : User
	 */
	public List<User> selectAllUsers() {
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

	/**
	 * 프로필 이름 편집 시 Users DB에 업데이트
	 * 
	 * @param username
	 * @param user
	 */
	public void updateUsername(String username, User user) {

		try (PreparedStatement pstmt = conn.prepareStatement(UPDATE_USERNAME)) {
			pstmt.setString(1, username);
			pstmt.setString(2, user.getUserId());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	/**
	 * 회원가입 시 회원 정보 추가
	 * 
	 * @param user
	 */
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

	/**
	 * 현재 로그인한 userId로 사용자의 전체 정보 조회
	 * 
	 * @param currentUserId
	 * @return : User
	 */
	public User userSearch(String currentUserId) {
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
