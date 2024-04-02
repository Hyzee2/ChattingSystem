package user;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class UserGUI extends JFrame{
	private JLabel nicknameLabel;
    private JButton editProfileButton;
    private JButton startChatButton;

    private User user;
    private boolean isSelfProfile;

    public UserGUI(User user, boolean isSelfProfile) {
        this.user = user;
        this.isSelfProfile = isSelfProfile;

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
        nicknameLabel = new JLabel("프로필 이름: " + user.getNickname());
        nicknameLabel.setBounds(135, labelY, labelWidth, labelHeight);
        panel.add(nicknameLabel);

        // 프로필 편집 버튼
        if (isSelfProfile) {
            editProfileButton = new JButton("프로필 편집");
            editProfileButton.setBounds(labelX, labelY + labelHeight + 10, 120, labelHeight); // 버튼의 너비를 더 넓게 조정
            editProfileButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    openEditProfileDialog();
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
                startChat();
            }
        });
        panel.add(startChatButton);

        getContentPane().add(panel);
        setVisible(true);
    }

    private void openEditProfileDialog() {
        String newNickname = JOptionPane.showInputDialog(this, "프로필 수정하기:");
        if (newNickname != null && !newNickname.isEmpty()) {
            user.setNickname(newNickname);
            nicknameLabel.setText("프로필 이름: " + newNickname);
        }
    }

    private void startChat() {
        String roomId = "room1";
		// 채팅방으로 이동하는 로직을 추가할 수 있음
        // 여기서는 간단히 채팅방으로 이동하는 기능만 구현
        ChatRoomGUI chatRoomGUI = new ChatRoomGUI(user, roomId );
        chatRoomGUI.setVisible(true);
        dispose(); // 현재 창 닫기
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                User user = new User("test", "password", "TestUser", "test@example.com");
                
                UserGUI userGUI = new UserGUI(user, true);
                userGUI.setVisible(true);
            }
        });
    }
    
}



