package student;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.WindowConstants;
import javax.swing.border.LineBorder;
/**
 * Popup frame that displays student information
 * @author TMH
 *
 */
public class DisplayPopUpFrame extends JFrame {
	private static final long serialVersionUID = 1L;
	private JLabel name,emailAddress,studentNumber,tutor,results;
	private JPanel main,bottom;
	private ArrayList<String> marks,participation;
	
	/**
	 * Uses student object to extract student information and constructs UI
	 * @param student - Student Object
	 */
	public DisplayPopUpFrame(Student student) {
		super(student.getName() + " - Information Card");
		// Initializes all required fields with constructor arguments
		name = new JLabel(student.getName());
		emailAddress = new JLabel(student.getEmail());
		studentNumber = new JLabel("Student No. :   "+ student.getStudentNumber());
		tutor = new JLabel("Tutor:     " + student.getTutor());
		marks = student.getAssessMarks();
		participation = student.getLastAccessArray();

		// Calls Popup method to make required JPanel
		makePopUp();

	}
	
	private void makePopUp() {
		// Initializes required JPanels
		main = new JPanel();
		bottom = new JPanel();

		// Sets JPanel to BorderLayout
		main.setLayout(new BorderLayout());

		// Sets font size
		Font h1 = new Font("Calibri", Font.PLAIN, 40);
		Font h2 = new Font("Calibri", Font.PLAIN, 25);
		name.setFont(h1);
		emailAddress.setFont(new Font("Calibri",Font.ITALIC,35));
		studentNumber.setFont(h2);
		tutor.setFont(h2);

		// Aligns all JLabels
		name.setHorizontalAlignment(SwingConstants.CENTER);
		emailAddress.setHorizontalAlignment(SwingConstants.CENTER);
		studentNumber.setHorizontalAlignment(SwingConstants.LEFT);
		tutor.setHorizontalAlignment(SwingConstants.LEFT);

		// Adds student Number and tutor email to bottom JPanel
		bottom.setLayout(new BoxLayout(bottom, BoxLayout.PAGE_AXIS));
		JPanel sNumberPanel = new JPanel(new BorderLayout());
		studentNumber.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		sNumberPanel.add(this.studentNumber, BorderLayout.WEST);
		JPanel tutorPanel = new JPanel(new BorderLayout());
		this.tutor.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
		tutorPanel.add(this.tutor, BorderLayout.WEST);
		bottom.add(sNumberPanel);
		bottom.add(tutorPanel);

		// Gets participation data and adds to popup frame
		for (String s : participation) {
			JLabel temp = new JLabel("Last Access: " + s);
			temp.setBorder(BorderFactory.createEmptyBorder(0, 20, 0, 0));
			temp.setFont(h2);
			JPanel tempHolder = new JPanel(new BorderLayout());
			tempHolder.add(temp, BorderLayout.CENTER);
			bottom.add(tempHolder);
		}
		//If there is results for the Student
		if (!marks.isEmpty()) {
			// Sets font of results label and adds it to the window
			results = new JLabel("Results:", SwingConstants.CENTER);
			results.setFont(this.results.getFont().deriveFont(20.0f));
			JPanel tResult = new JPanel(new BorderLayout());
			tResult.add(results, BorderLayout.CENTER);
			bottom.add(tResult);

			//Loops through each of the marks and adds them to the bottom of the panel
			for (String m : marks) {
				JLabel temp = new JLabel(m);
				temp.setFont(h2);
				temp.setHorizontalAlignment(SwingConstants.CENTER);
				JPanel tempHolder = new JPanel(new BorderLayout());
				tempHolder.add(temp, BorderLayout.CENTER);
				bottom.add(tempHolder);
			}
		}

		// Adds all labels to the main Panel
		main.add(this.name, BorderLayout.NORTH);
		main.add(this.emailAddress, BorderLayout.CENTER);
		main.add(bottom, BorderLayout.SOUTH);

		// Adds main panel to Main JFrame
		add(main);

		// Required JFrame
		getRootPane().setBorder(new LineBorder(Color.BLACK, 5));
		setBackground(Color.white);
		setSize(650, 350);
		setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		setLocationRelativeTo(null);
		setVisible(true);

	}
	
	/**
	 * return - name of student in display pop up
	 */
	public String getName(){
		return name.getText();
	}

}
