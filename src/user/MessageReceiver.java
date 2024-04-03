package user;

public interface MessageReceiver {
	
	void displayMessage(String message);

	void displayMessage(String message, boolean isSelf);
	
}
