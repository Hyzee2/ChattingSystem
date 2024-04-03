package user;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom {
	
	private String roomId;
    private List<User> participants;
    private List<MessageReceiver> messageReceivers; // 메시지 수신자 리스트 추가
    private User currentUser; // ChatRoomGUI에서 설정할 현재 사용자

    public String getRoomId() {
		return roomId;
	}

	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}

	public ChatRoom(String roomId) {
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
        for (User participant : participants) {
            if (!participant.equals(sender)) {
                // 실제 애플리케이션에서는 여기서 각 참여자의 GUI를 업데이트하거나,
                // 참여자에게 메시지를 전달하는 로직을 구현합니다.
                // 예시: participant.receiveMessage(message);
            }
        }
    }
    
    public void setCurrentUser(User user) {
        this.currentUser = user;
    }
    
    public void sendMessage(User sender, String messageContent) {
    	 // 현재 사용자에 의해 보낸 메시지인지 확인
        boolean isSelf = sender.equals(this.currentUser);

        // 모든 참여자에게 메시지 전달
        for (MessageReceiver receiver : messageReceivers) {
            // 메시지와 전송자 정보를 결합하여 전달
            String fullMessage = isSelf ? "[나]: " + messageContent : "[" + sender.getNickname() + "]: " + messageContent;
            receiver.displayMessage(fullMessage, isSelf);
        }
    }
    
    // 다른 필요한 메소드들도 추가할 수 있음
}
