package backup;

public class Profile {
	 private User user;
	    private String profilePicture;
	    private String statusMessage;

	    public void setProfilePicture(String profilePicture) {
			this.profilePicture = profilePicture;
		}

		public void setStatusMessage(String statusMessage) {
			this.statusMessage = statusMessage;
		}

		public Profile(User user) {
	        this.user = user;
	    }

	    // Getter 및 Setter 메서드 생략
}
