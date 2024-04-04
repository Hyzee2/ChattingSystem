package user;
import java.util.ArrayList;
import java.util.List;

public class WaitingRoom {
	private List<roomname> roomlist = new ArrayList<>(); // 채팅방 목록

    public WaitingRoom() {
    	roomlist = new ArrayList<>();
    }

    public void addChatRoom(ChatRoom chatRoom) {
    	roomlist.add(chatRoom);
        // TODO: MySQL DB에 채팅방 정보 추가
    }

    public void removeChatRoom(String roomId) {
    	roomlist.removeIf(room -> room.getRoomId().equals(roomId));
        // TODO: MySQL DB에서 채팅방 정보 삭제
    }

    public List<ChatRoom> getChatRooms() {
        return roomlist;
    }

    // 채팅방 ID로 채팅방 찾기
    public ChatRoom findChatRoomById(String roomId) {
    	for (ChatRoom room : chatRooms) {
            if (room.getRoomId().equals(roomId)) {
                return room;
            }
        }
        return null; // 찾지 못했다면 null 반환
    }

    // 기타 필요한 메서드 추가 가능
}
