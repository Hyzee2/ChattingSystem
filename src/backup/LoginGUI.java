package backup;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

public class LoginGUI extends JFrame {
	
	  private JTextField userIDField;
	    private JPasswordField passwordField;
	    private JButton loginButton;
	    private JButton signUpButton;
	    private List<User> users;

	    public LoginGUI() {
	        setTitle("Login");
	        setSize(400, 600);
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

	        users = new ArrayList<>();
	        
	        JPanel panel = new JPanel();
	        panel.setLayout(new GridLayout(4, 1));

	        JLabel userIDLabel = new JLabel("ID:");
	        panel.add(userIDLabel);

	        userIDField = new JTextField();
	        panel.add(userIDField);

	        JLabel passwordLabel = new JLabel("Password:");
	        panel.add(passwordLabel);

	        passwordField = new JPasswordField();
	        panel.add(passwordField);

	        loginButton = new JButton("Login");
	        loginButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                login();
	            }
	        });
	        panel.add(loginButton);

	        signUpButton = new JButton("회원가입하기");
	        signUpButton.addActionListener(new ActionListener() {
	            @Override
	            public void actionPerformed(ActionEvent e) {
	                signUp();
	            }
	        });
	        panel.add(signUpButton);

	        getContentPane().add(panel);
	        setVisible(true);
	    }

	    private void login() {
	    	String userID = userIDField.getText();
	        String password = new String(passwordField.getPassword());

	        // 사용자가 입력한 userID와 password를 이용하여 회원 정보를 찾음
	        User foundUser = null;
	        for (User user : users) {
	            if (user.getUserId().equals(userID) && user.getPassword().equals(password)) {
	                foundUser = user;
	                break;
	            }
	        }

	        if (foundUser != null) {
	            // 로그인 성공 시 User 객체를 생성하여 UserGUI로 이동
	            UserGUI userGUI = new UserGUI(foundUser, true); // 로그인한 본인의 프로필 화면
	            userGUI.setVisible(true);
	            dispose(); // 로그인 화면 닫기
	        } else {
	            JOptionPane.showMessageDialog(this, "id 혹은 pw가 일치하지 않습니다.");
	        }
	    }

	    private void signUp() {
	    	String userId = JOptionPane.showInputDialog(this, "User ID 입력:");
	        String password = JOptionPane.showInputDialog(this, "Password 입력:");
	        String nickname = JOptionPane.showInputDialog(this, "닉네임 입력:");
	        String email = JOptionPane.showInputDialog(this, "Email 입력:");

	        User user = new User(userId, password, nickname, email);
	        users.add(user);
	        JOptionPane.showMessageDialog(this, "회원 정보 입력이 성공하였습니다!");
	    }

	    public static void main(String[] args) {
	        SwingUtilities.invokeLater(new Runnable() {
	            public void run() {
	                new LoginGUI();
	            }
	        });
	    }
}

