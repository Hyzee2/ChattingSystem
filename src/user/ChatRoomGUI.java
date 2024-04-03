package user;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class ChatRoomGUI extends JFrame {

	private JTextPane chatPane;
	private JTextField messageField;
	private JButton sendButton;
	private JLabel titleLabel; // 채팅방 제목

	private User currentUser;
	private int roomId;
	
	private MessageDAO messageDAO;

	public ChatRoomGUI(User user, int roomId) {
		this.currentUser = user;
		this.roomId = roomId; 
		
		// 채팅방 대화 내용을 불러오는 로직 구현
		
		initializeUI();
		loadMessages();
		
		// TestData에서 채팅방 대화 내용 불러오기
	    List<String> messages = TestData.getChatMessages(roomId);
	    for (String message : messages) {
	        boolean isSelf = message.startsWith(user.getUserId() + ":");
	        displayMessage(message, isSelf);
	    }
		
		setTitle("Chat Room");
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		chatroom = new ChatRoom(roomId); // roomId를 사용하여 ChatRoom 초기화
		chatroom.addParticipant(user); // 현재 사용자를 참여자로 추가

		JPanel panel = new JPanel();
		panel.setLayout(null);

		chatroom = new ChatRoom(roomId); // roomId를 사용하여 ChatRoom 초기화
		chatroom.addParticipant(user); // 현재 사용자를 참여자로 추가

		// 채팅방 제목 설정
		titleLabel = new JLabel();
		titleLabel.setText(user.getUsername() + " 님과의 대화방");
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
					messageField.setText(""); // 메시지 필드 클리어
				}
			}
		});
		panel.add(sendButton);

		// 대화 내용 검색 버튼 설정
		searchButton = new JButton("검색");
		searchButton.setBounds(10 + titleLabel.getWidth() - 80, 10, 60, 30); // titleLabel의 x좌표 + titleLabel의 폭 - 버튼의 폭
		panel.add(searchButton);
		panel.add(searchButton);

		getContentPane().add(panel);
		setVisible(true);
	}

	public void loadMessages() {
		List<String> messages = messageDAO.searchMessage(currentUser.getRoomId()); // 데이터베이스에서 user의 채팅방 번호를 찾아서 메시지를 불러옴
		for (String message : messages) {
	        // 메시지를 채팅 창에 표시하는 로직 구현
	    }
	}
	
	@Override
	public void displayMessage(String message, boolean isSelf) {
		SwingUtilities.invokeLater(() -> {
			try {
				SimpleAttributeSet attrs = new SimpleAttributeSet();
				if (isSelf) {
					// 현재 사용자에 의한 메시지: 오른쪽 정렬
					StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
				} else {
					// 다른 사용자에 의한 메시지: 왼쪽 정렬
					StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
				}
				int len = doc.getLength(); // 문서의 현재 길이 가져오기
				doc.insertString(doc.getLength(), message + "\n", attrs);
				doc.setParagraphAttributes(doc.getLength() - 1, 1, attrs, false);

			} catch (BadLocationException e) {
				e.printStackTrace();
			}
		});
	}

	@Override
	public void displayMessage(String message) {
		// TODO Auto-generated method stub

	}

	private void sendMessage() {
		String messageText = messageField.getText();
		if (!messageText.isEmpty()) {
			chatroom.sendMessage(currentUser, messageText);
			messageField.setText("");
		}
	}

	public static void main(String[] args) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				// 테스트용 User 객체 생성
				User user = new User("khj", "123", "admin", "test@example.com");
				// 채팅방 ID 설정 (예제로 "room1"을 사용)
				String roomId = "room1";
				// ChatRoomGUI 인스턴스 생성 시, User 객체와 roomId 전달
				new ChatRoomGUI(user, roomId);
			}
		});
	}

}
