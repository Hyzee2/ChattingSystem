package user;
import java.util.*;

class User {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private List<Friend> friends = new ArrayList<>();

    public User(String userId, String password, String nickname, String email) {
        this.userId = userId;
        this.password = password;
        this.nickname = nickname;
        this.email = email;
        this.friends = new ArrayList<>();
    }

    // Getter 및 Setter 메서드 생략

    public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	// 친구를 추가하는 메서드
	public void addFriend(User friendUser) {
		// 이미 친구인지 확인
        for (Friend friend : friends) {
            if (friend.involvesUser(friendUser)) {
                return; // 이미 친구면 추가하지 않음
            }
        }
        // 새로운 친구 관계를 추가
        friends.add(new Friend(this, friendUser));
    }


	// 이 사용자의 친구 목록을 가져오는 메서드
    public List<User> getFriends() {
        List<User> friendUsers = new ArrayList<>();
        for (Friend friend : friends) {
            if (friend.getUser1().equals(this)) {
                friendUsers.add(friend.getUser2());
            } else if (friend.getUser2().equals(this)) {
                friendUsers.add(friend.getUser1());
            }
        }
        return friendUsers;
    }

    // 주어진 ID나 닉네임으로 친구를 찾는 메서드 (예시, 실제 구현 필요)
    public User findUserByIdOrNickname(String input) {
        for (User friend : getFriends()) {
            if (friend.getUserId().equals(input) || friend.getNickname().equals(input)) {
                return friend;
            }
        }
        return null; // 찾지 못했으면 null 반환
    }
}