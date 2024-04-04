package user;

import java.util.ArrayList;
import java.util.List;

import backup.MessageReceiver;

public class ChatRoom { 
	
	private int roomId;
    private List<User> participants;
    private List<MessageReceiver> messageReceivers; // 메시지 수신자 리스트 추가
    private User currentUser; // ChatRoomGUI에서 설정할 현재 사용자

    public int getRoomId() {
		return roomId;
	}

	public void setRoomId(int roomId) {
		this.roomId = roomId;
	}

	public User getCurrentUser() {
		return currentUser;
	}

	public void setCurrentUser(User currentUser) {
		this.currentUser = currentUser;
	}

	public ChatRoom(int roomId) {
        this.roomId = roomId;
        this.participants = new ArrayList<>();
        this.messageReceivers = new ArrayList<>(); // 초기화
    }

    public void addParticipant(User user) {
        participants.add(user);
        if (user instanceof MessageReceiver) { // User가 MessageReceiver를 구현하는 경우
            messageReceivers.add((MessageReceiver) user);
        }
    }

    // 채팅방에 메시지를 받는 기능을 구현
    public void receiveMessage(User sender, String messageContent) {
        // 메시지 객체 생성
        Message message = new Message(sender, messageContent);

        // 채팅방의 모든 참여자에게 메시지 전달 (메시지 보낸 사람 제외)
        for (MessageReceiver receiver : messageReceivers) {
            if (!sender.equals(receiver)) { // 메시지 보낸 사람을 제외한 모든 수신자에게 메시지 전달
                // 메시지 수신 처리, 예: receiver.receiveMessage(message);
                // 여기서는 receiver가 User 객체이므로, 다운캐스팅 또는 인터페이스 설계 변경이 필요할 수 있음
                // 예시 구현은 아래 참조
            	// 메시지 보낸 사람과 수신자가 같은지 여부에 따라 'isSelf' 플래그 설정
    	        boolean isSelf = receiver.equals(sender);
                receiver.displayMessage(formatMessage(sender, messageContent, isSelf), false);
            }
        }
    }
    
    // 메시지 형식을 결정하는 보조 메서드
    private String formatMessage(User sender, String messageContent, boolean isSelf) {
        if (isSelf) {
            return messageContent + " : [나]";
        } else {
            return "[" + sender.getUsername() + "]: " + messageContent;
        }
    }
    
    public void sendMessage(User sender, String messageContent) {
    	 Message message = new Message(sender, messageContent);
    	    for (MessageReceiver receiver : messageReceivers) {
    	    	// 모든 참여자에게 메시지와 함께 'isSelf' 플래그를 전달
    	        // 메시지 보낸 사람과 수신자가 같은지 여부에 따라 'isSelf' 플래그 설정
    	        boolean isSelf = receiver.equals(sender);
    	        // 형식화된 메시지 생성
    	        String formattedMessage = formatMessage(sender, messageContent, isSelf);
    	        // 메시지 수신자에게 메시지 전달
    	        receiver.displayMessage(formattedMessage, isSelf);
    	    }
    }
    
}
