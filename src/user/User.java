package user;
import java.util.*;

class User {
    private String userId;
    private String password;
    private String nickname;
    private String email;
    private List<Friend> friends;

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

	public void addFriend(User friendUser) {
        Friend friend = new Friend(this, friendUser);
        this.friends.add(friend);
        friendUser.friends.add(friend);
    }

    public List<Friend> getFriends() {
        return friends;
    }

    // 다른 기능들을 추가할 수 있음
}