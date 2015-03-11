package praCourseWork2;
import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

public class EmailSettingsFrame extends JFrame {
	private String[] connecSecu;
	private String[] authenMeth;
	
	JTextField serverNameField;
	JSpinner portSpinner;
	JComboBox connectionBox;
	JComboBox authenticationBox;
	JTextField userField;
	JButton ok;
	JButton cancel;

	private static final long serialVersionUID = 1L;

	public EmailSettingsFrame() {
		super("SMTP Server");
		initUi();

	}
	
	/**
	 * Made a quick method 
	 * that allows you to quickly define 
	 * the connection security options
	 * and authentication methods
	 */

	public void setString() {
		connecSecu = new String[1];
		connecSecu[0] = "StartTLS";

		authenMeth = new String[1];
		authenMeth[0] = "StartTLS";

	}

	public void initUi() {
		//Sets all required String[]
		setString();
		
		//Create a box Panel - To store everything
		JPanel box = new JPanel();
		box.setLayout(new BoxLayout(box, BoxLayout.PAGE_AXIS));
		add(box, BorderLayout.NORTH);

		// Create a font for all labels
		// Try different fonts if you wish
		//All of them will change if you change this one
		Font font = new Font("Arial", Font.PLAIN, 20);
		
		//Settings Label
		JLabel settings = new JLabel("Settings");
		settings.setFont(new Font("Arial", Font.BOLD, 30));
		settings.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		JPanel settingsPanel = new JPanel(new BorderLayout());
		settingsPanel.add(settings, BorderLayout.WEST);
		box.add(settingsPanel);

		//Settings Options
		//////Server Name
		JPanel serverGrid = new JPanel(new GridLayout(1, 0));
		JLabel serverNameLabel = new JLabel("Server Name:");
		serverNameLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
		serverNameLabel.setFont(font);
		serverNameField = new JTextField();

		serverGrid.add(serverNameLabel);
		serverGrid.add(serverNameField);
		box.add(serverGrid);
		
		/////Port
		JPanel portGrid = new JPanel(new GridLayout(1, 0));
		JPanel portGrid2 = new JPanel(new GridLayout(1,0));
		JLabel portLabel = new JLabel("Port:");
		portLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
		portLabel.setFont(font);
		// Spinner Model
		Integer value = new Integer(0);
		Integer min = new Integer(0);
		Integer max = new Integer(100);
		Integer step = new Integer(1);
		SpinnerModel model = new SpinnerNumberModel(value, min, max, step);
	    portSpinner = new JSpinner(model);
	    
	    JLabel defaultPort = new JLabel("Default:  587");
		defaultPort.setBorder(BorderFactory.createEmptyBorder(0, 587, 0, 0));
	    
	    portGrid2.add(portSpinner);
	    portGrid2.add(defaultPort);
		
		portGrid.add(portLabel);
		portGrid.add(portGrid2);
		box.add(portGrid);

		//Security and Authentication
		JLabel security = new JLabel("Security and Authentication");
		security.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		security.setFont(new Font("Arial", Font.BOLD, 30));
		JPanel securityPanel = new JPanel(new BorderLayout());
		securityPanel.add(security, BorderLayout.WEST);
		box.add(securityPanel);

		/////Connection security
		JPanel connectionGrid = new JPanel(new GridLayout(1, 0));
		JLabel connectionLabel = new JLabel("Connection Security:");
		connectionLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
		connectionLabel.setFont(font);
		connectionBox = new JComboBox(connecSecu);

		//connectionGrid.add(connectionLabel);
		//connectionGrid.add(connectionBox);
		box.add(connectionGrid);

		//////Authentication Method
		JPanel authenticationGrid = new JPanel(new GridLayout(1, 0));
		JLabel authenticationLabel = new JLabel("Authentication Method:");
		authenticationLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
		authenticationLabel.setFont(font);
		authenticationBox = new JComboBox(authenMeth);

		authenticationGrid.add(authenticationLabel);
		authenticationGrid.add(authenticationBox);
		box.add(authenticationGrid);

		/////User Name fields
		JPanel userGrid = new JPanel(new GridLayout(1, 0));
		JLabel userLabel = new JLabel("User Name:");
		userLabel.setBorder(BorderFactory.createEmptyBorder(0, 40, 0, 0));
		userLabel.setFont(font);
		userField = new JTextField();

		userGrid.add(userLabel);
		userGrid.add(userField);
		box.add(userGrid);

		// Bottom buttons
		cancel = new JButton("Cancel");
		ok = new JButton("OK");
		ok.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			
			
		});
		
		JPanel buttonHolder = new JPanel();
		buttonHolder.setAlignmentX(RIGHT_ALIGNMENT);
		buttonHolder.add(cancel);
		buttonHolder.add(ok);
		JPanel buttonBorder = new JPanel(new BorderLayout());
		buttonBorder.add(buttonHolder, BorderLayout.EAST);
		add(buttonBorder, BorderLayout.SOUTH);

		// Default JFrame settings
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
		setSize(600, 300);
		// pack();

	}

	public static void main(String[] args) {
		EmailSettingsFrame frame = new EmailSettingsFrame();
	}

}
