package user;

import javax.swing.*;
import javax.swing.text.BadLocationException;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public class WaitingRoomGUI extends JFrame {
	private JList<String> chatRoomList; // 채팅방 목록을 표시하는 컴포넌트
	private JButton addFriendButton; // '친구 추가' 버튼
	private JButton friendsListButton; // '친구 목록' 버튼
	private JButton createChatRoomButton; // '채팅방 생성' 버튼
	private JButton refreshButton; // '새로고침' 버튼

	private User currentUser;
	private boolean isSelf;
	private UserDAO userDAO;
	private ChatroomDAO chatroomDAO;
	private List<String> roomlist;
	private List<String> selectedMembers; // 채팅방 참여자들 리스트

	public WaitingRoomGUI(List<String> roomlist, User currentUser, boolean isSelf) throws SQLException {
		this.currentUser = currentUser;
		this.isSelf = isSelf;
		this.chatroomDAO = new ChatroomDAO();
		this.userDAO = new UserDAO();
		this.roomlist = roomlist;

		setTitle("채팅 목록 - " + currentUser.getUserId());
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLayout(new BorderLayout());

		// 채팅방 목록을 표시하는 JList를 초기화하고 채팅방 목록을 설정
		chatRoomList = new JList<>(roomlist.toArray(new String[0]));
		add(new JScrollPane(chatRoomList), BorderLayout.CENTER);

		// 채팅방 목록에서 항목을 더블클릭했을 때의 이벤트 처리
		chatRoomList.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 더블 클릭 이벤트 처리
					String selectedRoomname = chatRoomList.getSelectedValue(); // getSelectedValue()의 반환 타입은 object
					// 선택된 채팅방으로 들어가기 위해 MultiChatRoomGUI 인스턴스 생성

					try {
						new MultiChatRoomGUI(currentUser, selectedMembers, selectedRoomname, true).setVisible(true);

					} catch (SQLException | IOException | BadLocationException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
						JOptionPane.showMessageDialog(null, "에러 발생: " + e1.getMessage());
					}
				}
			}
		});

		// '채팅방 생성' 버튼 추가
		createChatRoomButton = new JButton("채팅방 생성");
		createChatRoomButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				createChatRoom();

			}

		});
		// '새로고침' 버튼 추가
		refreshButton = new JButton("새로고침");
		refreshButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					refreshChatRoomList();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();

				}
			}
		});

		// '친구 추가' 버튼 추가
		addFriendButton = new JButton("친구 추가");
		addFriendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String friendId = JOptionPane.showInputDialog("친구의 ID를 입력하세요");
				if (friendId != null && !friendId.isEmpty()) {
					if (userDAO.existsUser(friendId)) {
						userDAO.addFriend(currentUser.getUserId(), friendId);
						JOptionPane.showMessageDialog(null, "친구가 추가되었습니다.");
					} else {
						JOptionPane.showMessageDialog(null, "입력하신 ID는 없습니다.");
					}
				}
			}
		});

		// '친구 목록' 버튼 추가
		friendsListButton = new JButton("친구 목록");
		friendsListButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				List<String> friendsList = userDAO.getFriends(currentUser.getUserId());
				JOptionPane.showMessageDialog(null, "친구 목록: " + String.join(", ", friendsList));
			}
		});

		// 상단 패널에 버튼을 추가
		JPanel topPanel = new JPanel();
		topPanel.setLayout(new FlowLayout(FlowLayout.LEFT)); // 왼쪽 정렬을 위해 FlowLayout 사용
		topPanel.add(createChatRoomButton); // '채팅방 생성' 버튼 추가
		topPanel.add(refreshButton);
		topPanel.add(addFriendButton); // '친구 추가' 버튼 추가
		topPanel.add(friendsListButton); // '친구 목록' 버튼 추가
		getContentPane().add(topPanel, BorderLayout.NORTH); // 상단 패널을 프레임의 북쪽에 추가

		setVisible(true);
	}

	/**
	 * 친구목록에서 참여자들을 선택해서 채팅방을 새로 만드는 메소드
	 * 
	 * @return 친구목록에서 선택된 참여자들
	 */
	private List<String> createChatRoom() {
		// 친구 목록 가져오기
		List<String> friendsList = userDAO.getFriends(currentUser.getUserId());
		JList<String> list = new JList<>(friendsList.toArray(new String[0]));

		list.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
		JOptionPane.showMessageDialog(null, new JScrollPane(list), "친구 선택", JOptionPane.PLAIN_MESSAGE);

		// 선택된 친구들의 userId 가져오기
		List<String> selectedMembers = null; //
		List<String> selectedFriends = list.getSelectedValuesList();
		if (!selectedFriends.isEmpty()) {
			// 새 채팅방을 데이터베이스에 추가하고 선택된 친구들을 참여시키는 로직
			try {
				String newRoomName = JOptionPane.showInputDialog("채팅방 이름 입력:");
				if (newRoomName != null && !newRoomName.isEmpty()) {
					int newRoomId = chatroomDAO.createChatRoom(newRoomName); // 새로운 채팅방 이름에 대한 새로운 채팅방 번호 생성
					for (String friendId : selectedFriends) {
						chatroomDAO.addParticipantToRoom(newRoomId, friendId);
					}
					chatroomDAO.addParticipantToRoom(newRoomId, currentUser.getUserId()); // 현재 사용자도 채팅방에 추가

					// List 형태로 해당 채팅방 참여자들 담아주기
					selectedMembers = chatroomDAO.selectMembers(newRoomId);

				}
			} catch (SQLException ex) {
				ex.printStackTrace();
			}
		}
		return this.selectedMembers = selectedMembers;
	}

	/**
	 * 채팅방 목록 다시 조회
	 * 
	 * @throws SQLException
	 */
	private void refreshChatRoomList() throws SQLException {

		List<String> updatedRoomList = chatroomDAO.searchRoomList(currentUser.getUserId());
		chatRoomList.setListData(updatedRoomList.toArray(new String[0]));
	}

}
