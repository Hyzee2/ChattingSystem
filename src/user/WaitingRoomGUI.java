package user;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;

public class WaitingRoomGUI extends JFrame {
	private JList<String> chatRoomList; // 채팅방 목록을 표시하는 컴포넌트
    private JButton addFriendButton; // '친구 추가' 버튼
    private WaitingRoom waitingRoom; // WaitingRoom 인스턴스
    private User currentUser;

    public WaitingRoomGUI(WaitingRoom waitingRoom, User currentUser) {
        this.waitingRoom = waitingRoom;
        this.currentUser = currentUser;
        
        setTitle("채팅 목록");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setLayout(new BorderLayout());

        chatRoomList = new JList<>();
        updateChatRoomList(); // 채팅방 목록 업데이트
        chatRoomList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) { // 더블 클릭 이벤트 처리
                    String selectedRoomId = chatRoomList.getSelectedValue();
                    ChatRoom selectedRoom = waitingRoom.findChatRoomById(selectedRoomId);
                    if (selectedRoom != null) {
                        new ChatRoomGUI(currentUser, selectedRoom.getRoomId());
                    }
                }
            }
        });
        add(new JScrollPane(chatRoomList), BorderLayout.CENTER);

        addFriendButton = new JButton("친구 추가");
        addFriendButton.addActionListener(e -> addFriend());
        add(addFriendButton, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void updateChatRoomList() {
    	DefaultListModel<String> model = new DefaultListModel<>();
        for (ChatRoom room : waitingRoom.getChatRooms()) {
            model.addElement(room.getRoomId());
        }
        chatRoomList.setModel(model);
    }
    
    private void addFriend() {
        String input = JOptionPane.showInputDialog(this, "친구의 ID 혹은 닉네임 입력:");
        if (input != null && !input.isEmpty()) {
            // 입력받은 정보로 친구를 찾아 추가하는 로직 구현
            // 예시 코드로 DB 연동 부분은 생략되어 있으며, 실제 애플리케이션에서는 DB 조회 로직 필요
            User friend = currentUser.findUserByIdOrNickname(input);
            if (friend != null) {
                currentUser.addFriend(friend);
                JOptionPane.showMessageDialog(this, friend.getNickname() + "을(를) 친구로 추가했습니다.");
                // TODO: DB에 친구 관계 추가 로직 구현 필요
            } else {
                JOptionPane.showMessageDialog(this, "해당 사용자를 찾을 수 없습니다.");
            }
        }
    }

    // 사용자 ID나 닉네임으로 사용자 찾기 (가상의 메서드, 실제 DB 연동 필요)
//    private User findUserByIdOrNickname(String input) {
//        // 이 메서드는 사용자 입력을 받아 해당하는 User 객체를 반환하도록 구현해야 함
//        // 여기서는 예시로, 입력값과 일치하는 User 객체를 반환하는 간단한 로직만 제시
//        for (User user : getUsersFromDB()) { // getUsersFromDB()는 모든 사용자를 반환하는 가상의 메서드
//            if (user.getUserId().equals(input) || user.getNickname().equals(input)) {
//                return user;
//            }
//        }
//        return null; // 찾지 못했을 때 null 반환
//    }

    // DB에서 모든 사용자를 가져오는 가상의 메서드 (실제 DB 연동 로직 필요)
    private List<User> getUsersFromDB() {
        // 여기서는 예시로, 모든 사용자 목록을 반환하는 간단한 코드만 제공
        //List<User> users = new ArrayList<>();
        // DB 조회 로직으로 사용자 목록을 가져오는 코드 구현 필요
        return TestData.getUsers();    
     }
    
    public static void main(String[] args) {
    	SwingUtilities.invokeLater(() -> {
            User currentUser = new User("khj", "123", "admin", "test@example.com");
            WaitingRoom waitingRoom = new WaitingRoom();

            // 임시로 채팅방 추가
            waitingRoom.addChatRoom(new ChatRoom("room1"));

            new WaitingRoomGUI(waitingRoom, currentUser);
        });
    }

}
