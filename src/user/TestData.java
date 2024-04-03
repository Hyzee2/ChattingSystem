package user;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

		users.get(4).addFriend(users.get(0)); // 인덱스는 0~4까지 존재 
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
	
	private static Map<String, List<String>> userChatRooms = new HashMap<>();
    static {
        // khj 사용자에게 room1 채팅방 할당
        userChatRooms.put("khj", Arrays.asList("room1"));
    }

    public static List<String> getUserChatRooms(String userId) {
        return userChatRooms.getOrDefault(userId, new ArrayList<>());
    }
    
    // 채팅방 ID와 해당 채팅방의 대화 내용을 매핑하는 Map
    private static Map<String, List<String>> chatMessages = new HashMap<>();
    static {
        chatMessages.put("room1", Arrays.asList(
            "user1: 안녕하세요!",
            "khj: 여러분 안녕하세요, khj입니다.",
            "user2: 오늘도 좋은 하루 되세요!"
        ));
    }

 	// 채팅방 ID에 따른 대화 내용 리스트를 반환하는 메소드
    public static List<String> getChatMessages(int roomId) {
        return chatMessages.getOrDefault(roomId, Arrays.asList("대화 내용이 없습니다."));
    }

	
}
