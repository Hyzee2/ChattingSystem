package user;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserGUI extends JFrame { // 사용자 프로필이 뜨는 화면 구현

	private JLabel usernameLabel; // 프로필 이름 라벨
	private JButton editProfileButton; // 프로필 이름 편집 버튼
	private JButton startChatButton; // 채팅 시작 버튼
	private JButton chatRoomListButton; // 채팅방 목록 버튼

	private boolean isSelf = true;

	private User user;
	private ChatRoom chatroom;
	private ChatroomDAO chatroomDAO;
	private UserDAO userDAO;
	private List<ChatRoom> roomlist;

	public UserGUI(User user, boolean isSelf) throws SQLException {
		this.user = user;

		this.isSelf = isSelf;

		this.chatroomDAO = new ChatroomDAO(); // DAO 객체 초기화. 필드에서는 선언만 하고 초기화가 안됐었다.
		// UserGUI 클래스의 인스턴스가 생성될 때 ChatroomDAO 객체도 함께 초기화되어, searchRoomId 메소드를 안전하게 호출
		// 가능.
		this.userDAO = new UserDAO();

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
		if (isSelf) {
			editProfileButton = new JButton("프로필 편집");
			editProfileButton.setBounds(labelX, labelY + labelHeight + 10, 120, labelHeight); // 버튼의 너비를 더 넓게 조정
			editProfileButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					try {
						openEditProfileDialog(user);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			});
			panel.add(editProfileButton);
		}



		chatRoomListButton = new JButton("채팅목록");
		chatRoomListButton.setBounds(labelX + 130, labelY + labelHeight + 10, 120, labelHeight); //
		chatRoomListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					openWaitingRoomGUI(user);
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} // 채팅방 목록 GUI 열기
			}
		});
		panel.add(chatRoomListButton);
		
		getContentPane().add(panel);
		setVisible(true);

	}

	/**
	 * 현재 사용자가 속한 채팅방 목록을 가져옴 
	 * @param user
	 * @throws SQLException
	 */
	private void openWaitingRoomGUI(User user) throws SQLException {
		
		List<String> roomlist = chatroomDAO.searchRoomList(user.getUserId());

		new WaitingRoomGUI(roomlist, user, true).setVisible(true); // 채팅방 목록과 사용자 정보를 포함하여 WaitingRoomGUI 열기
	}

	/**
	 * 현재 사용자의 프로필 이름을 변경할 수 있는 기능 
	 * @param user
	 * @throws SQLException
	 */
	private void openEditProfileDialog(User user) throws SQLException {
		String newUsername = JOptionPane.showInputDialog(this, "프로필 수정하기:");
		if (newUsername != null && !newUsername.isEmpty()) {

			this.userDAO = new UserDAO(); // user db 연결해서

			userDAO.updateUsername(newUsername, user); // 변경된 username으로 User 테이블 데이터 update
			usernameLabel.setText("프로필 이름: " + newUsername);
		}
	}


}
