package user;

import java.awt.*;
import java.time.LocalDate;
import java.awt.event.*;
import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class MultiChatRoomGUI extends JFrame implements ActionListener {
	// 멤버 변수 선언
	private JFrame jframe;
	private JTextPane chatPane; // 채팅내용 보이는 화면창
	private StyledDocument doc;
	private JTextField messageField; // 채팅내용 입력 텍스트 필드
	private JButton sendButton, exitButton, searchButton; // 전송 버튼, 종료 버튼, 검색 버튼

	private User currentUser; // 현재 로그인한 사용자의 (id, name, password) User 객체
	private List<String> selectedMembers; // 채팅방 참여자들 리스트
	private int roomId; // 채팅방의 고유번호
	private String roomname; // 채팅방 이름
	private List<Message> messages; // senderId + message를 List에 담음
	private List<String> senderIds; // senderId만 List에 담음
	private String senderId; // message를 보낸 사람의 id
	private User user; // 현재 로그인한 사용자
	private LocalDate selectedDate; // 사용자가 선택한 날짜

	private MessageDAO messageDAO;
	private ChatroomDAO chatroomDAO;
	private UserDAO userDAO;

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
//	private boolean isSelf;

	// 생성자
	public MultiChatRoomGUI(User user, List<String> selectedMembers, String selectedRoomname, boolean isSelf)
			throws SQLException, IOException, BadLocationException {
		// DAO 및 사용자 정보 초기화
		initializeDAOs();

		this.currentUser = user;
		this.roomname = selectedRoomname;

		// 서버에서 senderId를 찾아오기
		this.roomId = chatroomDAO.searchRoomId(selectedRoomname);

		this.senderIds = messageDAO.searchSender(roomId);

		this.messages = messageDAO.searchMessage(roomId);

		// 네트워크 설정 및 GUI 초기화
		initializeNetwork("127.0.0.1"); // 127.0.0.1
		initializeGUI();
		loadMessages(selectedRoomname); // 메시지 로딩

	}

	// DAO 초기화 메소드
	private void initializeDAOs() throws SQLException {
		this.messageDAO = new MessageDAO();
		this.chatroomDAO = new ChatroomDAO();
		this.userDAO = new UserDAO();
	}

	// 네트워크 초기화 메소드
	private void initializeNetwork(String ip) throws IOException {
		socket = new Socket(ip, 8050);
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		Thread t = new Thread(new MultiClientThread(this));
		t.start();
	}

	// GUI 초기화 메소드
	private void initializeGUI() {
		jframe = new JFrame("Multi Chatting - " + roomname);
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		jframe.setSize(400, 600);
		jframe.setLayout(new BorderLayout());

		chatPane = new JTextPane();
		chatPane.setEditable(false);
		doc = chatPane.getStyledDocument();
		JScrollPane scrollPane = new JScrollPane(chatPane);
		jframe.add(scrollPane, BorderLayout.CENTER);

		messageField = new JTextField();
		sendButton = new JButton("전송");
		exitButton = new JButton("종료");
		searchButton = new JButton("검색");

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(messageField, BorderLayout.CENTER);
		bottomPanel.add(sendButton, BorderLayout.EAST);
		jframe.add(bottomPanel, BorderLayout.SOUTH);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		topPanel.add(exitButton);
		topPanel.add(searchButton);
		jframe.add(topPanel, BorderLayout.NORTH);

		sendButton.addActionListener(this);
		searchButton.addActionListener(e -> showDateMSG());
		exitButton.addActionListener(e -> exitApplication());
		messageField.addActionListener(e -> {
			try {
				sendMessage();
			} catch (BadLocationException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

		jframe.setVisible(true);
	}

	// ActionPerformed 메소드
	// 메세지 전송
	@Override
	public void actionPerformed(ActionEvent e) {
		try {
			sendMessage();
		} catch (BadLocationException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	// 메시지 전송 메소드
	private void sendMessage() throws BadLocationException {
		String message = messageField.getText();
		String senderId = "";
		if (!message.isEmpty()) {
			try {
				senderId = this.currentUser.getUserId();
				sendServer(senderId, roomname);
				oos.writeObject(currentUser.getUserId() + "#" + message);
				// messageField.setText("");
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
//		displayMessage(message, senderId);
	}

	// 애플리케이션 종료 메소드
	public void exitApplication() {
		try {
			oos.writeObject(currentUser.getUserId() + "#exit");
		} catch (IOException e) {
			e.printStackTrace();
		}
		jframe.setVisible(false); // 창을 안보이게 하고 시스템은 종료가 안됨
//		System.exit(0); // 아예 시스템 종료 
	}

	// 날짜별 대화내용 검색 메소드
	public void showDateMSG() {
		List<LocalDate> dateList = messageDAO.searchDate(roomId);
		// 날짜 리스트를 String 배열로 변환
		String[] dates = new String[dateList.size()];
		for (int i = 0; i < dateList.size(); i++) {
			dates[i] = dateList.get(i).toString();
		}
		// JOptionPane을 사용하여 날짜 리스트 보여주기
		String selectedDateStr = (String) JOptionPane.showInputDialog(null, "날짜를 선택하세요", "날짜 선택",
				JOptionPane.QUESTION_MESSAGE, null, dates, dates[0]);
		if (selectedDateStr != null && !selectedDateStr.isEmpty()) {
			selectedDate = LocalDate.parse(selectedDateStr); // 선택된 날짜를 LocalDate로 변환하여 저장
			showMessages(); // 선택된 날짜에 해당하는 메시지를 보여주는 메소드 호출
		}
	}

	public void showMessages() {
		if (selectedDate != null) {
			List<Message> messages = messageDAO.getMessage(selectedDate, roomId);

			JDialog dialog = new JDialog(jframe, "Messages", true);
			dialog.setLayout(new BorderLayout());
			JTextArea textArea = new JTextArea(20, 50);
			textArea.setEditable(false);

			// 메시지 내용을 textArea에 추가
			for (Message message : messages) {
				textArea.append(message.getSenderId() + ": " + message.getContent() + "\n");
			}

			dialog.add(new JScrollPane(textArea), BorderLayout.CENTER);
			dialog.pack();
			dialog.setLocationRelativeTo(jframe);
			dialog.setVisible(true);
		} else {
			JOptionPane.showMessageDialog(jframe, "날짜가 선택되지 않았습니다.", "경고", JOptionPane.WARNING_MESSAGE);
		}

	}

	public int loadMessages(String selectedRoomname) throws BadLocationException { // 채팅방 db에서 대화내용 불러오기

		for (Message message : messages) {
			String senderId = message.getSenderId();

			String content = message.getContent();

			// 메시지를 채팅 창에 표시하는 로직 구현
			displayMessage(content, senderId);
		}
		return roomId; // 찾은 채팅방
	}

	/**
	 * 채팅 메세지 내용 보여주기
	 * 
	 * @param message 채팅 글 내용
	 * @param isSelf: true: 나, false: 다른사람
	 * @throws BadLocationException
	 */
	public void displayMessage(String message, String senderId) throws BadLocationException {

		// 단순히 메시지를 보여주는 로직
		if (doc != null) {
			// 메시지 형식을 "senderId: message"로 설정
			String formattedMessage = senderId + ": " + message + "\n";
			// 메시지와 함께 줄바꿈 문자 추가
			doc.insertString(doc.getLength(), formattedMessage, null); // 스타일 속성을 null로 설정하여 기본 스타일 적용
		}
//		String targetSenderId = "";
//		
//		SimpleAttributeSet attrs = new SimpleAttributeSet();
//		
//		for (int i =  i < senderIds.size(); i++) { // senderId 리스트들의 크기만큼 반복문 실행 
//			targetSenderId = senderIds.get(i); // targetId에 넣어준다 
//			if (targetSenderId.equals(currentUser.getUserId())) { // 현재 로그인한 id와 targetId가 동일하면 본인이 보낸 메시지인 것 
//				StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT); // 오른쪽 정렬 
//				message = message + " : 나";
//			} else {
//				StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT); // 왼쪽 정렬  
//				message = targetSenderId + " : " + message;
//			}
//			if (doc != null) {
//				doc.insertString(doc.getLength(), message + "\n", attrs);
//				doc.setParagraphAttributes(doc.getLength() - 1, 1, attrs, false);
//			}
//		}
//		return this.senderId = targetSenderId;
	}

	private void sendServer(String senderId, String selectedRoomname) throws SQLException { // 사용자가 채팅창에서 입력한 값을 db로 보내는
																							// 메소드
		String messageText = messageField.getText();
		System.out.println(messageText);
		int roomId = chatroomDAO.searchRoomId(selectedRoomname);
		// db에 채팅 데이터를 insert 하는 로직
		messageDAO.inputMessage(messageText, roomId, senderId);
		// 메시지 필드 초기화
		messageField.setText("");

		// chatroom.sendMessage(currentUser, messageText);

	}

	public User getCurrentUser() {
		return currentUser;
	}

	public String getSenderId() {
		return senderId;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public JTextPane getJTextPane() {
		return chatPane;
	}

}
