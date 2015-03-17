package extraFeatures;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import main.MainFrame;
/**
 * Login frame - prevents unauthorized access to system
 * @author TMH
 *
 */
public class Login extends JFrame{
	private static String currentDirectory = new File("").getAbsolutePath();
	private final long serialVersionUID = 1L;
	
	public Login(){
		super("Please login");
		initUi();
	}
	
	
	private void initUi(){
		Font font = new Font("Calibri",Font.BOLD,20);
		
		JPanel main = new JPanel(new BorderLayout());
		JPanel combine = new JPanel();
		JPanel userPanel = new JPanel(); 
		JPanel passPanel = new JPanel();
		JPanel south = new JPanel(new FlowLayout());
		JLabel lblUser = new JLabel("Username:");
		lblUser.setFont(font);
		JLabel lblPass = new JLabel("Password:");
		lblPass.setFont(font);
		JTextField username = new JTextField(15);
		
		JPasswordField  password = new JPasswordField(15);
		JButton btnLogin = new JButton("Login");
		username.setText("admin");
		btnLogin.addActionListener(new ActionListener(){
			String userDetails = "";
			@Override
			public void actionPerformed(ActionEvent arg0) {
				BufferedReader br;
				try {
					br = new BufferedReader(new FileReader(currentDirectory + "/login.txt"));

					StringBuilder sb = new StringBuilder();
			        String line = br.readLine();

			        while (line != null) {
			            sb.append(line);
			            sb.append(System.lineSeparator());
			            line = br.readLine();
			        }
			        userDetails = sb.toString();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
      
				  String str = userDetails;

			      ArrayList<String> details = null;
			      details = new ArrayList(Arrays.asList(str.trim().split("\\s*,\\s*")));
				
				if (username.getText().equals("") || password.getPassword().length == 0){
					JOptionPane.showMessageDialog(null,"please enter a useranme or password");
				} else {
					String passText = new String(password.getPassword());				
					char[] correctPassword = details.get(1).toCharArray();
					if ((username.getText().equals(details.get(0)) && passText.equals(details.get(1)))){
						dispose();
						JFrame frame = new MainFrame();
					} else {
						JOptionPane.showMessageDialog(null,"useranme or password is wrong");
					}
				}
				
			}
			
		});
		userPanel.add(lblUser);
		userPanel.add(username);
		
		passPanel.add(lblPass);
		passPanel.add(password);
		
		combine.add(userPanel);
		combine.add(passPanel);
	    south.add(btnLogin);
		main.add(combine, BorderLayout.CENTER);
		main.add(south,BorderLayout.SOUTH);
		add(main);
		
		//Allows enter to press login button
		username.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				btnLogin.doClick();
				
			}
			
		});
		password.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				btnLogin.doClick();
				
			}
			
		});
		
		//Adds lock image
		try {
			URL imgURL = getClass().getResource("/Lock-Icon.png");
		    ImageIcon ii = new ImageIcon(imgURL);
		    Image newimg = ii.getImage().getScaledInstance(90, 90,
					java.awt.Image.SCALE_SMOOTH);
			add(new JLabel(new ImageIcon(newimg)),BorderLayout.NORTH);
		} catch (Exception e) {
			System.out.println("Error reading file");
		}
		
		setSize(300,250);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}
}
