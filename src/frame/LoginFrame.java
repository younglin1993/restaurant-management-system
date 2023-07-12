package frame;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.border.EmptyBorder;

import model.RestaurantDao;
import project01.*;

import javax.swing.JLabel;
import javax.swing.JOptionPane;

import javax.swing.JButton;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;

public class LoginFrame extends JFrame {
	 
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	private JTextField txtUsername;
	private JPasswordField txtPassword;
	
	public LoginFrame() {
		
		setTitle("登入");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 372, 173);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));

		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnLogin = new JButton("登入");
		btnLogin.addActionListener(e -> {
			btnLoginActionPerformed(e);
		});
		btnLogin.setBounds(151, 98, 87, 23);
		contentPane.add(btnLogin);
		
		JButton btnCancel = new JButton("取消");
		btnCancel.addActionListener(e -> {
			dispose();
		});
		btnCancel.setBounds(248, 98, 87, 23);
		contentPane.add(btnCancel);
		
		JLabel lblNewLabel = new JLabel("使用者名稱");
		lblNewLabel.setFont(new Font("新細明體", Font.PLAIN, 14));
		lblNewLabel.setBounds(10, 10, 79, 23);
		contentPane.add(lblNewLabel);
		
		txtUsername = new JTextField();
		txtUsername.setBounds(100, 10, 235, 23);
		contentPane.add(txtUsername);
		txtUsername.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("密碼");
		lblNewLabel_1.setFont(new Font("新細明體", Font.PLAIN, 14));
		lblNewLabel_1.setBounds(10, 43, 68, 23);
		contentPane.add(lblNewLabel_1);
		
		txtPassword = new JPasswordField();
		txtPassword.setColumns(10);
		txtPassword.setBounds(100, 43, 235, 23);
		contentPane.add(txtPassword);
	}
	
	public boolean checkLogin(String username, char[] password) {
		return username.equals(Main.username) && String.valueOf(password).equals(Main.password);
	}
	
	private void btnLoginActionPerformed(ActionEvent e) {
	    String username = txtUsername.getText();
	    char[] password = txtPassword.getPassword();
	    if (checkLogin(username, password)) {
			dispose();
			RestaurantDao restaurantDao = new RestaurantDao();
			if (restaurantDao.readTable("restaurant")) {
				MainFrame mf = new MainFrame();
				mf.setVisible(true);
			} else {
				restaurantDao.createTable("restaurant");
				MainFrame mf = new MainFrame();
				mf.setVisible(true);
			}
	    } else {
	    	JOptionPane.showMessageDialog(null, "登入失敗", "錯誤", JOptionPane.ERROR_MESSAGE);
	    } 
	}

}
