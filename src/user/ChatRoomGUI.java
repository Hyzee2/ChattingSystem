package user;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.List;

public class ChatRoomGUI extends JFrame {

	private JTextPane chatPane; // 채팅 영역
	private JTextField messageField; // 메시지 입력
	private JButton sendButton; // 메시지 전송
	private JLabel titleLabel; // 채팅방 제목
	private JButton searchButton; // 대화내용 일자별 검색
	private StyledDocument doc;

	private User currentUser;
	private String senderId;
	private boolean isSelf;
	private int roomId;
	private String roomname;

	private MessageDAO messageDAO;
	private ChatroomDAO chatroomDAO;
	private UserDAO userDAO;
	private ChatRoom chatroom;

	private Socket socket;
	private ObjectInputStream ois;
	private ObjectOutputStream oos;
	private String id;

	public User getCurrentUser() {
		return currentUser;
	}

	public JTextPane getChatPane() {
		return chatPane;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public ObjectInputStream getOis() {
		return ois;
	}

	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}

	public ObjectOutputStream getOos() {
		return oos;
	}

	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}

	public String getId() {
		return id;
	}

	public ChatRoomGUI(User user, String selectedRoomname, boolean isSelf) throws SQLException {
		this.currentUser = user;
		this.isSelf = isSelf;
		this.roomname = selectedRoomname;
		this.messageDAO = new MessageDAO();
		this.chatroomDAO = new ChatroomDAO();
		this.userDAO = new UserDAO();

		this.senderId = this.currentUser.getUserId();

		try {
			initNetwork();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} // 서버와의 연결
		loadMessages(selectedRoomname); // 채팅방에 대화내용 불러오기

		setTitle("Chat Room");
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatroom = new ChatRoom(roomId); // roomId를 사용하여 ChatRoom 초기화
		chatroom.addParticipant(currentUser); // 현재 사용자를 참여자로 추가

		JPanel panel = new JPanel();
		panel.setLayout(null);

//		chatroom = new ChatRoom(roomId); // roomId를 사용하여 ChatRoom 초기화
//		chatroom.addParticipant(user); // 현재 사용자를 참여자로 추가

		// 채팅방 제목 설정
		titleLabel = new JLabel();
		titleLabel.setText(selectedRoomname);
		titleLabel.setBounds(10, 10, 380, 30);
		titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
		panel.add(titleLabel);

		// 채팅 영역 설정
		chatPane = new JTextPane();
		doc = chatPane.getStyledDocument();

		JScrollPane scrollPane = new JScrollPane(chatPane);
		scrollPane.setBounds(10, 50, 365, 400);
		chatPane.setEditable(false);

		panel.add(scrollPane);

		// 메시지 입력 필드 설정
		messageField = new JTextField();
		messageField.setBounds(10, 470, 280, 30);
		messageField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				sendButton.doClick(); // sendButton의 클릭 이벤트를 프로그래매틱하게 발생시킴
			}
		});
		panel.add(messageField);

		// 메시지 전송 버튼 설정
		sendButton = new JButton("전송");
		sendButton.setBounds(295, 470, 80, 30);
		sendButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String messageText = messageField.getText();
				if (!messageText.isEmpty()) {
					// 현재 사용자가 보낸 메시지를 채팅 영역에 표시
					displayMessage(messageText + " : 나", true); // 수정: 'true'는 메시지가 자신에 의해 보내졌음을 의미
					// ChatRoom을 통해 메시지 전송
					chatroom.sendMessage(currentUser, messageText);
					try {
						sendChatting(roomId, senderId);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					messageField.setText(""); // 메시지 필드 클리어
				}
			}
		});
		panel.add(sendButton);

		// 대화 내용 검색 버튼 설정 (select)
		searchButton = new JButton("검색");
		searchButton.setBounds(10 + titleLabel.getWidth() - 80, 10, 60, 30); // titleLabel의 x좌표 + titleLabel의 폭 - 버튼의 폭
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// 메시지 일자별로 내용 조회하는 로직 (리스트 형태로 보여줌) 
			}
		});
		panel.add(searchButton);
		panel.add(searchButton);

		getContentPane().add(panel); // 최종적으로 panel을 JTextPane에 붙여준다.
		setVisible(true);
	}

	public int loadMessages(String selectedRoomname) { // 채팅방 대화내용 불러오기
		// 생성자에서 받은 roomname으로 roomId를 찾기
		int targetRoomId = chatroomDAO.searchRoomId(selectedRoomname);

		List<String> messages = messageDAO.searchMessage(targetRoomId); // 데이터베이스에서 user의 채팅방 번호를 찾아서 메시지를 불러옴
		for (String message : messages) {
			// 메시지를 채팅 창에 표시하는 로직 구현
			displayMessage(message, isSelf);
		}
		return this.roomId = targetRoomId; // 찾은 채팅방
	}

	public void displayMessage(String message, boolean isSelf) {
		// MessageDAO에서 senderId들 조회해서 가져오기
		List<String> senderIds = messageDAO.searchSender(roomId);
		// 내 아이디만 조회
		String isSelfId = currentUser.getUserId();

		// if(for-each구문으로 비교) isSelf = true;
		SwingUtilities.invokeLater(() -> {
			try {
				SimpleAttributeSet attrs = new SimpleAttributeSet();
				for (String senderId : senderIds) {
					if (senderId.equals(isSelfId)) {

						// 현재 사용자에 의한 메시지: 오른쪽 정렬
						StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
					} else {
						// 다른 사용자에 의한 메시지: 왼쪽 정렬
						StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
					}
				}

				int len = doc.getLength(); // 문서의 현재 길이 가져오기
				doc.insertString(doc.getLength(), message + "\n", attrs);
				doc.setParagraphAttributes(doc.getLength() - 1, 1, attrs, false);

			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		});
	}

	private void sendChatting(int roomId, String senderId) throws SQLException { // 사용자가 채팅창에서 입력한 값을 보내는 메소드
		String messageText = messageField.getText();

		if (!messageText.isEmpty()) {
			chatroom.sendMessage(currentUser, messageText);
			messageField.setText("");
		}
		// db에 채팅 데이터를 insert 하는 로직
		messageDAO.inputMessage(messageText, roomId, senderId);
		
	}

	public void initNetwork() throws IOException {
		socket = new Socket("127.0.0.1", 8050); //127.0.0.1  //192.168.0.71
		System.out.println("connected...");
		oos = new ObjectOutputStream(socket.getOutputStream());
		ois = new ObjectInputStream(socket.getInputStream());
		MultiClientThread ct = new MultiClientThread(this); // ChatRoomGUI를 받기 위해 this를 사용한다.
		Thread t = new Thread(ct);
		t.start();
	}

	public void exit() {
		System.exit(0);
	}

}
