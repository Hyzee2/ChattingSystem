package user;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGUI extends JFrame{ // 사용자 프로필이 뜨는 화면 구현 
	
	private JLabel usernameLabel; // 프로필 이름 라벨
    private JButton editProfileButton; // 프로필 이름 편집 버튼
    private JButton startChatButton; // 채팅 시작 버튼 
    private JButton chatRoomListButton; // 채팅방 목록 버튼

    private boolean isSelfProfile;
    
    private User user;
    private ChatRoom chatroom; 
    private ChatroomDAO chatroomDAO;
    private List<ChatRoom> roomlist; 

    public UserGUI(User user, boolean isSelfProfile) throws SQLException {
        this.user = user;
        this.isSelfProfile = isSelfProfile;
        this.chatroomDAO = new ChatroomDAO(); // DAO 객체 초기화. 필드에서는 선언만 하고 초기화가 안됐었다. 
        //UserGUI 클래스의 인스턴스가 생성될 때 ChatroomDAO 객체도 함께 초기화되어, searchRoomId 메소드를 안전하게 호출 가능.

        setTitle("User Profile");
        setSize(400, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        panel.setLayout(null); // Layout Manager 제거

        // 닉네임 레이블 위치 설정
        int labelWidth = 200;
        int labelHeight = 20;
        int labelX = 75;
        int labelY = 400;
        usernameLabel = new JLabel("프로필 이름: " + user.getUsername());
        usernameLabel.setBounds(135, labelY, labelWidth, labelHeight);
        panel.add(usernameLabel);

        // 프로필 편집 버튼
        if (isSelfProfile) {
            editProfileButton = new JButton("프로필 편집");
            editProfileButton.setBounds(labelX, labelY + labelHeight + 10, 120, labelHeight); // 버튼의 너비를 더 넓게 조정
            editProfileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openEditProfileDialog(user); // 프로필 편집 메소드 호출 
                }
            });
            panel.add(editProfileButton);
        }

        // 채팅 시작하기 버튼
        startChatButton = new JButton("대화 시작하기");
        startChatButton.setBounds(labelX + 130, labelY + labelHeight + 10, 120, labelHeight); // 프로필 편집 버튼의 오른쪽에 배치
        startChatButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                startChat(); // 채팅방으로 넘어가는 메소드 호출 
            }
        });
        panel.add(startChatButton);

        getContentPane().add(panel);
        setVisible(true);
        
        chatRoomListButton = new JButton("채팅목록");
        chatRoomListButton.setBounds(10, 10, 100, 30); // 상단 왼쪽 위치 설정
        chatRoomListButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                openWaitingRoomGUI(user); // 채팅방 목록 GUI 열기
            }
        });
        panel.add(chatRoomListButton);
        
    }
    
    private void openWaitingRoomGUI(User user) {
    	 // 현재 사용자가 속한 채팅방 목록을 가져옴
    	List<ChatRoom> roomlist = chatroomDAO.searchRoomList(user.getUserId());
    	
        new WaitingRoomGUI(roomlist, user).setVisible(true); // 채팅방 목록과 사용자 정보를 포함하여 WaitingRoomGUI 열기
    }

    private void openEditProfileDialog(User user) {
        String newUsername = JOptionPane.showInputDialog(this, "프로필 수정하기:");
        if (newUsername != null && !newUsername.isEmpty()) {
            // Users 테이블에 update 하는 함수 생성 
            usernameLabel.setText("프로필 이름: " + newUsername);
        }
    }

    private void startChat() {
        int roomId = chatroomDAO.searchRoomId(user.getUserId());
        
        ChatRoomGUI chatRoomGUI = new ChatRoomGUI(user, roomId);
        
        chatRoomGUI.setVisible(true);
        dispose(); // 현재 창 닫기
    }

//    public static void main(String[] args) throws SQLException {
//
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
////            	UserDAO userDAO = new UserDAO();
////     
////                User user = new User(userDAO.userSearch(null)); // 이 user는 로그인한 user 여야 한다.  
////                
//                UserGUI userGUI; // 선언 
//                
//				try {
//					userGUI = new UserGUI(foundUser, true);
//					userGUI.setVisible(true);
//				} catch (SQLException e) {
//					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
//                
//            }
//        });
//    }
    
}



