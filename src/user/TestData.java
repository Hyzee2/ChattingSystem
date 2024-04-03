package user;

import java.util.Arrays;
import java.util.List;

public class TestData {

	User user;

	private static List<User> users = Arrays.asList(
			new User("user1", "password1", "One", "user1@example.com"),
			new User("user2", "password2", "User Two", "user2@example.com"),
			new User("user3", "password3", "User Three", "user3@example.com"),
			new User("user4", "password4", "User Four", "user4@example.com"),
			new User("khj", "123", "admin", "admin@naver.com"));

	// 친구 관계 초기화
	static {
		// 예시: User1과 User2가 서로 친구
		users.get(0).addFriend(users.get(1));

		users.get(5).addFriend(users.get(1));
	}

	public static void setUsers(List<User> users) {
		TestData.users = users;
	}

	// 이 메서드는 WaitingRoomGUI 내에서 호출하여 사용자 목록을 가져올 수 있습니다.
	public static List<User> getUsers() {
		return users;
	}

	// 특정 사용자의 친구 목록을 가져오는 메소드를 추가할 수 있습니다.
	public static List<User> getFriendsOfUser(String userId) {
		for (User user : users) {
			if (user.getUserId().equals(userId)) {
				return user.getFriends();
			}
		}
		return null; // 해당 사용자가 없는 경우 null 반환
	}
}
