package user;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;
public class Friend {
	private User user; // 이 관리자가 관리하는 사용자
    private List<User> friends; // 해당 사용자의 친구 목록
    private UserDAO userDAO; // UserDAO 객체 선언

    public Friend(User user) {
    	 this.user = user;
         this.friends = new ArrayList<>(); // 친구 목록 초기화
    }

    // 친구 추가
    public void addFriend() {
    	userDAO = new UserDAO(); // 필요에 따라 클래스 내 다른 위치에서 초기화
        String input = JOptionPane.showInputDialog(this, "친구의 ID 혹은 닉네임 입력:");
        if (input != null && !input.isEmpty()) {
            User friend = userDAO.userSearch(input); // 찾은 user를 friend 객체로 담아준다. 
            if (friend != null) {
            	
                // 친구 관계를 DB에 추가하는 로직 구현 (가정)
                // 예: userDAO.addFriendship(currentUser, friend);
                JOptionPane.showMessageDialog(this, friend.getUsername() + "을(를) 친구로 추가했습니다.");
            } else {
                JOptionPane.showMessageDialog(this, "해당 사용자를 찾을 수 없습니다.");
            }
        }
    }

    // 친구 제거
    public void removeFriend(User friend) {
        friends.remove(friend);
    }

    // 친구 목록 가져오기
    public List<User> getFriends() {
        return friends;
    }

    // 주어진 사용자가 친구 목록에 속하는지 확인
    public boolean isFriend(User user) {
        return friends.contains(user);
    }
}
