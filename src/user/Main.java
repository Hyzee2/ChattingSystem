package user;

public class Main {
	public static void main(String[] args) {
        // 사용자 생성
        User user1 = new User("user1", "password1", "User1", "user1@example.com");
        User user2 = new User("user2", "password2", "User2", "user2@example.com");

        // 친구 추가
        user1.addFriend(user2);

        // 채팅방 생성 및 참여자 추가
        ChatRoom chatRoom = new ChatRoom("room1");
        chatRoom.addParticipant(user1);
        chatRoom.addParticipant(user2);

        // 메시지 전송
        Message message = new Message(user1, "Hello, User2!");
        // 메시지를 채팅방에 있는 모든 참여자에게 전송하는 로직 필요

        // 프로필 설정
        Profile userProfile1 = new Profile(user1);
        userProfile1.setProfilePicture("profile1.jpg");
        userProfile1.setStatusMessage("Hello World!");
    }
}
