package praCourseWork2;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.filechooser.FileNameExtensionFilter;

import studentdata.Connector;
import studentdata.DataTable;

public class StudentFrame extends JFrame {
	private ArrayList<Student> students; 


	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	 private DisplayPopUpFrame display = null;


	public StudentFrame() {
		// Initialises frame and sets title to team name
		super("PRA Coursework - TMH");
		InitUI();

	}
	
	public void InitUI(){
		setSize(700, 500);// MR:added size
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);// MR:added location

		JPanel panel = new JPanel();// panel to contain other components

		JMenuBar menu = new JMenuBar();
		JMenu file = new JMenu("File");
		menu.add(file);
		JMenuItem load = new JMenuItem("Load anonymous marking codes");
		JMenuItem loadExam = new JMenuItem("Load exam results");
		
		
		LoadListener loadListen = new LoadListener();
		loadExam.addActionListener(loadListen);
		load.addActionListener(loadListen);
		
		file.add(loadExam);
		file.add(load);
		menu.add(file);
		this.setJMenuBar(menu);
		setJMenuBar(menu);

		students = new ArrayList<Student>();
		// Fetches all student details from the server and adds to the student
		// ArrayList
		fetchStudentData(students);

		JList list = createJList(students);

		JTextField search = new JTextField(22);
		search.addKeyListener(new KeyAdapter() {
			public void keyReleased(KeyEvent e) {
				// clear the list
				DefaultListModel listModel = (DefaultListModel) list.getModel();
				listModel.removeAllElements();
				// get whatever user types into text field
				String buffer = search.getText();
				// store all matching students in serachStudent arraylist
				for (Student i : students) {
					if (i.getName().toLowerCase()
							.contains(buffer.toLowerCase())
							|| i.getStudentNumber().contains(buffer)) {
						listModel.addElement(i);
					}
				}
			}
		});
		list.setFixedCellHeight(30);// cell formatting
		list.setFixedCellWidth(260);// same thing

		// Sets top panel with search to borderLayout, so search JTextField
		// Stretches through the top dynamically
		panel.setLayout(new BorderLayout());
		panel.add(search, BorderLayout.WEST);

		// Adds the list as a ScrollPane so there is a scrollBar
		add(new JScrollPane(list), BorderLayout.WEST);
		add(panel, BorderLayout.NORTH);

		setVisible(true);
		
	}

	public JList createJList(ArrayList<Student> students){
		
	     DefaultListModel defListMod  = new DefaultListModel();//create a list of items that are editable. original list
		//does not allow you to do that. this allows more flexibility
		
		//goes through arraylist of Student objects, calls toString and adds the Strings to DefaultListModel (DLM)
		 for (Student s : students){
			 defListMod.addElement(s.toString());
		 }
		 
		 JList list = new JList(defListMod);//creates a new JList using the DLM
		 MouseListener mouseListener = new MouseAdapter(){
			 public void mouseClicked(MouseEvent e) {
				 Student findStudent = null;
				 String selectedItem = (String) list.getSelectedValue().toString();
				 findStudent  = findStudent(selectedItem,students);
				 System.out.println("AMC code is: "+findStudent.aMC);
				 if(display != null){
				 if(display.name.getText().equals(findStudent.name)){
					 //Debugging purposes
					 System.out.println("Disposed");
					 display.dispose();
				 }
				 }
				 display = new DisplayPopUpFrame(findStudent);
			 }
			 
		 };
		 list.addMouseListener(mouseListener);
		 list.setFont(new Font("Arial",Font.PLAIN,20));
		 return list;
	}

	public Student findStudent(String name, ArrayList<Student> studentArrayList) {
		Student found = null;
		for (int i = 0; i < studentArrayList.size(); i++) {
			if (studentArrayList.get(i).toString().equals(name)) {
				found = studentArrayList.get(i);
			}
		}
		return found;

	}

	public void fetchStudentData(ArrayList<Student> students) {
		// Create a Connector object and open the connection to the server
		Connector server = new Connector();
		boolean success = server.connect("TMH",

		"944ff2da7cd193c64ec9459a42f38786");

		if (success == false) {
			System.out
					.println("Fatal error: could not open connection to server");
			System.exit(1);
		}

		DataTable data = server.getData();

		int rowCount = data.getRowCount();
		ArrayList<String> studentDetails = new ArrayList<String>();
		String tempWord = "";
		// Loops through all data from server and puts into a studentDetail
		// arrayList
		for (int row = 0; row < rowCount; ++row) {
			for (int col = 0; col < 4; ++col) {
				if (col > 0) {
					tempWord += ",";
				}
				tempWord += data.getCell(row, col);
			}
			studentDetails.add(tempWord);
			// Makes tempWord blank again
			tempWord = "";
		}
		for (int i = 0; i < studentDetails.size(); i++) {
			String temp = studentDetails.get(i);
			// Splits the student details according to where the comma is
			String[] studentDetails1 = temp.split(",");
			int studentNumber = Integer.parseInt(studentDetails1[2]);

			Student temp1 = new Student(studentDetails1[0], studentDetails1[1],
					studentNumber, studentDetails1[3]);
			students.add(temp1);

		}

	}
	
	private class LoadListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser choosy = new JFileChooser();
			
			File f = new File("C://Users//Saif//workspace");
			choosy.setCurrentDirectory(f);
			
			//Creates filter so user can only select CSV file
			FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files","csv");
			choosy.setFileFilter(filter);

			
//			choosy.showOpenDialog(StudentFrame.this);//sets position of dialog box to default(centre) of the screen
//			//alternatively, we can change parameter to "StudentFrame.this". This means that dialog box will appear
//			//wherever the main frame is. 
			
			int returnValue = choosy.showOpenDialog(StudentFrame.this);
			if(returnValue == JFileChooser.APPROVE_OPTION){
				//Just some code to help with debugging later
				File file = choosy.getSelectedFile();
				int succesImport = 0;
				int totalImports = 0;
				try {
					BufferedReader bf = new BufferedReader(new FileReader(file));
					while(bf.ready()){
						String[] line = bf.readLine().split(",");
						for(Student s:students){
							int temp = Integer.parseInt(line[0]);
							if(temp == s.studentNumber){
								s.setAMC(line[1]);
								succesImport++;
							}
						}

						totalImports++;
					}
					int failedImports = totalImports - succesImport;
					String results = "Annonymous marking codes imported. "+succesImport+" codes were \nfor known students; "+failedImports+" were or unknown students";
					JOptionPane.showMessageDialog(StudentFrame.this,
						    results);
					
					
				} catch (FileNotFoundException p) {
					System.out.println("File not found");
				} catch (IOException g){
					System.out.println("Error");
				}
				
				System.out.println("You have chosen "+choosy.getSelectedFile().getName()+" to be imported");
				
				
			}
				
		}
		
	}
	
	
	
	

}
