package user;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AdminGUI extends JFrame {

	private Component adminnameLabel;
	private UserDAO userDAO;
	private ChatroomDAO chatroomDAO;
	private JButton loginButton, deleteButton, outButton;
	private String userId;
	private List<Login> loginUsers;
	private List<Participants> parlist;

	public AdminGUI() throws SQLException {
		setTitle("Admin page");
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.userDAO = new UserDAO();
		this.chatroomDAO = new ChatroomDAO();

		JPanel panel = new JPanel();
		panel.setLayout(new GridLayout(4, 1));

		adminnameLabel = new JLabel("관리자 페이지 입니다. ");
		// adminnameLabel.setBounds(135, labelY, labelWidth, labelHeight);
		panel.add(adminnameLabel);

		loginButton = new JButton("Login 조회"); // 버튼 생성
		// loginButton.setBounds(labelX, labelY + labelHeight + 10, 120, labelHeight);
		loginButton.addActionListener(new ActionListener() { // 기능구현 가능한 리스너 붙이기
			@Override
			public void actionPerformed(ActionEvent e) {
				loginData(); // 기능 메소드
			}
		});
		panel.add(loginButton); // 로그인 조회 버튼 붙이기

		deleteButton = new JButton("회원 삭제"); // 버튼 생성
		deleteButton.addActionListener(new ActionListener() { // 기능구현 가능한 리스너 붙이기
			@Override
			public void actionPerformed(ActionEvent e) {
				deleteData(); // 기능 메소드
			}
		});
		panel.add(deleteButton); // 회원 삭제 버튼 붙이기

		outButton = new JButton("회원 강퇴"); // 버튼 생성
		outButton.addActionListener(new ActionListener() { // 기능구현 가능한 리스너 붙이기
			@Override
			public void actionPerformed(ActionEvent e) { 
				outData(); // 기능 메소드
				// 강퇴당한 사용자의 채팅창 
			}
		});
		panel.add(outButton); // 회원 강퇴 버튼 붙이기

		getContentPane().add(panel);
		setVisible(true);
	}

	/**
	 * 현재 존재하는 채팅방 ID와 채팅참여자들 ID를 강퇴 시키는 기능
	 */
	public void outData() {
		List<Participants> parlist = chatroomDAO.searchChatroom();
		
		// Login 객체의 리스트를 String의 리스트로 변환
		List<String> parInfo = new ArrayList<>();
		String str = "";
		for (Participants par : parlist) {
			try {
				str = Integer.toString(par.getRoomId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String info = "roomID: " + str + ", userID: " + par.getUserId();
			parInfo.add(info);
		}

		JList<String> allParlist = new JList<>(parInfo.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(allParlist);
		getContentPane().removeAll();
		getContentPane().add(scrollPane, BorderLayout.CENTER);

		// 더블클릭 이벤트 처리를 위한 MouseListener 추가
		allParlist.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (e.getClickCount() == 2) { // 더블클릭 감지
					int index = allParlist.locationToIndex(e.getPoint());
					if (index >= 0) { // 유효한 항목이 선택되었는지 확인
						String selectedItem = allParlist.getModel().getElementAt(index);
						// 선택된 항목으로부터 userID 추출 (구현에 따라 달라질 수 있음)
						String userId = selectedItem.split(", ")[1].split(": ")[1];
						// 참가자 삭제 로직 호출
						String roomIdStr = selectedItem.split(", ")[0].split(": ")[1];
						int roomId = Integer.parseInt(roomIdStr); // 문자열을 정수로 변환
						chatroomDAO.outMembers(roomId, userId);
						JOptionPane.showMessageDialog(null, userId + "님을 강퇴 처리했습니다.");

						// UI 갱신 로직 호출
						try {
							refreshParList();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}

			}
		});
		// 변경사항을 적용하기 위해 GUI를 새로 그림
		getContentPane().revalidate();
		getContentPane().repaint();
	}

	/**
	 * 채팅방 ID와 채팅참여자들 ID 리스트를 새로고침하는 기능
	 * 
	 * @throws SQLException
	 */
	private void refreshParList() throws SQLException {

		List<Participants> parlist = chatroomDAO.searchChatroom();
		// Login 객체의 리스트를 String의 리스트로 변환
		List<String> parInfo = new ArrayList<>();
		String str = "";
		for (Participants par : parlist) {
			try {
				str = Integer.toString(par.getRoomId());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			String info = "roomID: " + str + ", userID: " + par.getUserId();
			parInfo.add(info);
		}

		JList<String> allParlist = new JList<>(parInfo.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(allParlist);
		getContentPane().removeAll();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		// 변경사항을 적용하기 위해 GUI를 새로 그림
		getContentPane().revalidate();
		getContentPane().repaint();
	}

	/**
	 * 회원 삭제
	 */
	public void deleteData() {
		String userId = JOptionPane.showInputDialog("로그인 정보를 확인할 ID를 입력하세요");
		if (userId != null && !userId.isEmpty()) {
			if (userDAO.searchLogin(userId) != null) {
				userDAO.deleteUser(userId);
				JOptionPane.showMessageDialog(this, "회원 정보가 삭제되었습니다!");
			} else {
				JOptionPane.showMessageDialog(this, "입력한 ID는 없습니다", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}

	/**
	 * 로그인 데이터 조회 id 입력
	 */
	public void loginData() {
		String userId = JOptionPane.showInputDialog("로그인 정보를 확인할 ID를 입력하세요");
		// this.userId = userId;
		if (userId != null && !userId.isEmpty()) {
			if (userDAO.searchLogin(userId) != null) {
				loginDataGet(userId);
			} else {
				JOptionPane.showMessageDialog(this, "입력한 ID는 없습니다", "경고", JOptionPane.WARNING_MESSAGE);
				return;
			}
		}
	}

	/**
	 * 입력받은 id의 로그인 기록 조회
	 * 
	 * @param userId
	 */
	public void loginDataGet(String userId) {
		List<Login> loginUsers = userDAO.searchLogin(userId);
		// Login 객체의 리스트를 String의 리스트로 변환
		List<String> loginInfo = new ArrayList<>();
		for (Login login : loginUsers) {
			// 예시로, 여기서는 login 객체의 userId와 loginDate를 사용하여 문자열을 생성합니다.
			// 실제 Login 클래스의 구조에 맞게 조정해야 합니다.
			String info = "ID: " + login.getUserId() + ", Date: " + login.getDate();
			loginInfo.add(info);
		}
		JList<String> loginList = new JList<>(loginInfo.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(loginList);
		getContentPane().removeAll();
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		// 변경사항을 적용하기 위해 GUI를 새로 그림
		getContentPane().revalidate();
		getContentPane().repaint();

	}

}
