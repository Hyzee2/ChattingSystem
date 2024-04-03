package user;

public class Friend {
	private User user1;
    private User user2;

    public Friend(User user1, User user2) {
        this.user1 = user1;
        this.user2 = user2;
    }

    // 친구 관계에 있는 첫 번째 사용자를 반환합니다.
    public User getUser1() {
        return user1;
    }

    // 친구 관계에 있는 두 번째 사용자를 반환합니다.
    public User getUser2() {
        return user2;
    }
    
    // 주어진 사용자가 이 친구 관계에 속하는지 확인합니다.
    public boolean involvesUser(User user) {
        return user.equals(user1) || user.equals(user2);
    }
}
