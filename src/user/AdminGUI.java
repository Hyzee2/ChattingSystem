package user;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class AdminGUI extends JFrame {

	private Component adminnameLabel;
	private UserDAO userDAO;
	private JButton loginButton;
	private String userId;
	private List<Login> loginUsers;



	public AdminGUI() throws SQLException {
		setTitle("Admin page");
		setSize(400, 600);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		this.userDAO = new UserDAO();
		
		JPanel panel = new JPanel();
		panel.setLayout(null); // Layout Manager 제거

		
		int labelWidth = 200;
		int labelHeight = 20;
		int labelX = 75;
		int labelY = 400;
		adminnameLabel = new JLabel("관리자 페이지 입니다. ");
		adminnameLabel.setBounds(135, labelY, labelWidth, labelHeight);
		panel.add(adminnameLabel);
		
		loginButton = new JButton("Login 조회"); // 버튼 생성
		loginButton.setBounds(labelX, labelY + labelHeight + 10, 120, labelHeight);
		loginButton.addActionListener(new ActionListener() { // 기능구현 가능한 리스너 붙이기
			@Override
			public void actionPerformed(ActionEvent e) {
				loginData(); // 기능 메소드
			}
		});
		panel.add(loginButton); // 버튼 붙이기
		
		getContentPane().add(panel);
		setVisible(true);
	}
	
	public void loginData() {
		String userId = JOptionPane.showInputDialog("로그인 정보를 확인할 ID를 입력하세요");
        //this.userId = userId;
		if (userId != null && !userId.isEmpty()) {
        	if(userDAO.searchLogin(userId)!=null) {
        		loginDataGet(userId);
        	}else {
        		JOptionPane.showMessageDialog(this, "입력한 ID는 없습니다", "경고", JOptionPane.WARNING_MESSAGE);
    			return;
        	}
        }
	}
	
	public void loginDataGet(String userId) {
		List<Login> loginUsers = userDAO.searchLogin(userId);
		// Login 객체의 리스트를 String의 리스트로 변환
	    List<String> loginInfo = new ArrayList<>();
	    for (Login login : loginUsers) {
	        // 예시로, 여기서는 login 객체의 userId와 loginDate를 사용하여 문자열을 생성합니다.
	        // 실제 Login 클래스의 구조에 맞게 조정해야 합니다.
	        String info = "ID: " + login.getUserId() + ", Date: " + login.getDate();
	        loginInfo.add(info);
	    }
		JList<String> loginList = new JList<>(loginInfo.toArray(new String[0]));
		JScrollPane scrollPane = new JScrollPane(loginList);
		getContentPane().removeAll();
	    getContentPane().add(scrollPane, BorderLayout.CENTER);
	    // 변경사항을 적용하기 위해 GUI를 새로 그림
	    getContentPane().revalidate();
	    getContentPane().repaint();

	}
	
}
