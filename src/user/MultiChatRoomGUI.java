package user;

import java.awt.*;
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

public class MultiChatRoomGUI implements ActionListener {
	// 멤버 변수 선언
	private JFrame jframe;
	private JTextPane chatPane;
	private StyledDocument doc;
	private JTextField messageField;
	private JButton sendButton, exitButton;
	private User currentUser;
	private int roomId;
	private String roomname;
	private MessageDAO messageDAO;
	private ChatroomDAO chatroomDAO;
	private UserDAO userDAO;
	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private List<String> messages;
	private List<String> senderIds;
	private boolean isSelf;
	private String senderId;

	// 생성자
	public MultiChatRoomGUI(User user, String selectedRoomname, boolean isSelf)
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
		initializeNetwork("192.168.0.71");
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

		JPanel bottomPanel = new JPanel(new BorderLayout());
		bottomPanel.add(messageField, BorderLayout.CENTER);
		bottomPanel.add(sendButton, BorderLayout.EAST);
		jframe.add(bottomPanel, BorderLayout.SOUTH);

		JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		topPanel.add(exitButton);
		jframe.add(topPanel, BorderLayout.NORTH);

		sendButton.addActionListener(this);
		exitButton.addActionListener(e -> exitApplication());
		messageField.addActionListener(e -> sendMessage());

		jframe.setVisible(true);
	}

	// ActionPerformed 메소드
	// 메세지 전송
	@Override
	public void actionPerformed(ActionEvent e) {
		sendMessage();
	}

	// 메시지 전송 메소드
	private void sendMessage() {
		String message = messageField.getText();
		if (!message.isEmpty()) {
			try {
				String senderId = this.currentUser.getUserId();
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
	}

	// 애플리케이션 종료 메소드
	public void exitApplication() {
		try {
			oos.writeObject(currentUser.getUserId() + "#exit");
		} catch (IOException e) {
			e.printStackTrace();
		}
		jframe.setVisible(false);
//		System.exit(0);
	}

	public int loadMessages(String selectedRoomname) throws BadLocationException { // 채팅방 대화내용 불러오기

		// this.messages = messageDAO.searchMessage(roomId); // 데이터베이스에서 user의 채팅방 번호를
		// 찾아서 메시지를 불러옴

		for (String message : messages) {
			// 메시지를 채팅 창에 표시하는 로직 구현
			displayMessage(message, senderId);
		}
		return roomId; // 찾은 채팅방
	}

	/**
	 * 채팅 글 쓰기
	 * 
	 * @param message 채팅 글 내용
	 * @param isSelf: true: 나, false: 다른사람
	 * @throws BadLocationException
	 */
	public String displayMessage(String message, String senderId) throws BadLocationException {
		String targetSenderId = "";
		
		SimpleAttributeSet attrs = new SimpleAttributeSet();
		for (int i = 0; i < senderIds.size(); i++) {
			targetSenderId = senderIds.get(i);
			if (targetSenderId.equals(currentUser.getUserId())) {
				StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
				message = message + " : 나";
			} else {
				StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
				message = targetSenderId + " : " + message;
			}
			if (doc != null) {
				doc.insertString(doc.getLength(), message + "\n", attrs);
				doc.setParagraphAttributes(doc.getLength() - 1, 1, attrs, false);
			}
		}
		return this.senderId = targetSenderId;
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
