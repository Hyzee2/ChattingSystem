package user;

import javax.swing.SwingUtilities;

public class MultiClientThread extends Thread {
	private ChatRoomGUI chatRoomGui;
	String[] receivedMsg = null;
	
    public MultiClientThread(ChatRoomGUI chatRoomGui){
    	this.chatRoomGui = chatRoomGui;
    }
    public void run(){
        String message = null;
        boolean isStop = false;
        
        while(!isStop){
            try{
                message = (String)chatRoomGui.getOis().readObject();
                receivedMsg = message.split("#");
            }catch(Exception e){
                e.printStackTrace();
                isStop = true;
            }

            // 메시지 처리 로직
            if(receivedMsg.length >= 2) { // 배열 길이 확인 추가
                System.out.println(receivedMsg[0]+","+receivedMsg[1]);
                if(receivedMsg[1].equals("exit")){
                    if(receivedMsg[0].equals(chatRoomGui.getCurrentUser().getUserId())){ // chatRoomGui.getId() 대신 사용
                    	chatRoomGui.exit();
                    }else{
                        // exit 메시지 처리
                        SwingUtilities.invokeLater(() -> {
                            chatRoomGui.displayMessage(receivedMsg[0] + "님이 종료 하셨습니다.", false); // JTextArea 대신 JTextPane 사용
                        });
                    }
                }else{
                    // 일반 메시지 처리
                    SwingUtilities.invokeLater(() -> {
                        chatRoomGui.displayMessage(receivedMsg[0] + " : " + receivedMsg[1], false); // JTextArea 대신 JTextPane 사용
                    });
                }
            }
        }
    }
}
