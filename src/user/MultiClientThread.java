package user;

import javax.swing.SwingUtilities;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

public class MultiClientThread extends Thread {
	private MultiChatRoomGUI multiChatRoomGUI;
	String[] receivedMsg = null;
	String senderId;

	public MultiClientThread(MultiChatRoomGUI multiChatRoomGUI) {
		this.multiChatRoomGUI = multiChatRoomGUI;
		//this.senderId = user.getUserId();
	}
	

	public void run() {
		String message = null;
		boolean isStop = false;

		while (!isStop) {
			try {
				message = (String) multiChatRoomGUI.getOis().readObject();
				
				receivedMsg = message.split("#");
				
				this.senderId = receivedMsg[0];
			} catch (Exception e) {
				e.printStackTrace();
				isStop = true;
			}

			// 메시지 처리 로직
			if (receivedMsg.length >= 2) { // 배열 길이 확인 추가
				System.out.println(receivedMsg[0] + "," + receivedMsg[1]); // 사용자 id + 채팅 내용
				try {
					if (receivedMsg[1].equals("exit")) { // 만약 채팅 내용이 exit 이면
						if (receivedMsg[0].equals(multiChatRoomGUI.getCurrentUser().getUserId())) { // 현재 사용자의 userId와
																									// 사용자의 id가 일치하면
							multiChatRoomGUI.exitApplication(); // 채팅방 종료
							
						} else { // 만약 exit를 입력한 id와 현재 사용자의 id가 다른경우면 (다른 사용자가 exit라고 입력한 경우 )
									// exit 메시지 처리
							try {
								SwingUtilities.invokeLater(() -> {
									try {
										multiChatRoomGUI.displayMessage(receivedMsg[0] + "님이 종료 하셨습니다.", receivedMsg[0]);
									} catch (BadLocationException e) {
										// TODO Auto-generated catch block
										e.printStackTrace();
									} // JTextArea 대신 JTextPane 사용
								});
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					} else {
						// 일반 메시지 처리
						SwingUtilities.invokeLater(() -> {
							
							try {
		                        // 모든 메시지를 동일하게 "사용자ID: 메시지" 형식으로 표시
		                        multiChatRoomGUI.displayMessage(receivedMsg[1], receivedMsg[0]);
		                    } catch (BadLocationException e) {
		                        e.printStackTrace();
		                    }
//							SimpleAttributeSet attrs = new SimpleAttributeSet();
//							if (receivedMsg[0].equals(multiChatRoomGUI.getCurrentUser().getUserId())) { // 사용자의 id와 메시지를
//																										// 입력한 id가 동일하다면
//								StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_RIGHT);
//								
//								try {
//									multiChatRoomGUI.displayMessage(receivedMsg[1], senderId);
//								} catch (BadLocationException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//							} else {
//								StyleConstants.setAlignment(attrs, StyleConstants.ALIGN_LEFT);
//								
//								try {
//									multiChatRoomGUI.displayMessage(receivedMsg[1], senderId);
//								} catch (BadLocationException e) {
//									// TODO Auto-generated catch block
//									e.printStackTrace();
//								}
//
//							} 
							
							
						});
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
}
