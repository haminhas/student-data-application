package praCourseWork2;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
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

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;

import org.jfree.data.xy.XYSeries;

import com.itextpdf.text.DocumentException;

import extraFeatures.PDFGenerator;
import studentdata.Connector;
import studentdata.DataTable;
import websiteRelated.WebviewFrame;

public class StudentFrame extends JFrame {
	private ArrayList<Student> students;
	private ArrayList<Assessment> assesments;
	private JList list;
	private JTabbedPane tabbedPane;
	private boolean examLoaded;
	private boolean anonLoaded;
	private File settingsFile; // Holds directory of settings file. The file
								// itself may or may not exist
	ArrayList<String> emails;
	ArrayList<String> durations;
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private DisplayPopUpFrame display = null;

	public StudentFrame() {
		// Initialises frame and sets title to team name
		super("PRA Coursework - TMH");
		examLoaded = false;
		anonLoaded = false;
		findSettingsFile();
		InitUI();

	}

	public void InitUI() {
		setSize(700, 500);// MR:added size
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setLocationRelativeTo(null);// MR:added location
		tabbedPane = new JTabbedPane();
		JPanel panel = new JPanel();// panel to contain other components
		// addJTable();
		JMenuBar menu = new JMenuBar();

		JMenu file = new JMenu("File");
		JMenu data = new JMenu("Data");
		JMenu extra = new JMenu("Extra");

		JMenuItem settings = new JMenuItem("Email Settings");
		settings.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				
				new EmailSettingsFrame(settingsFile);
				
			}

		});
		
		JMenuItem pdf = new JMenuItem("Generate PDF");
		extra.add(pdf);
		pdf.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				list.getSelectedValue();
				if(list.getSelectedValue() != null){
					String studentName = (String) list.getSelectedValue().toString();
					Student s = findStudent(studentName,students);
					try {
						new PDFGenerator().createPdf(s);
					} catch (DocumentException | IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					JOptionPane.showMessageDialog(null, "PDF succesfully generated for "+s.getName()+" and saved to desktop");
				}else{
					JOptionPane.showMessageDialog(null, "Please select a student from the list");
				}
				
				
				
			}
			
		});

		data.add(settings);

		
		JMenuItem load = new JMenuItem("Load anonymous marking codes");
		JMenuItem loadExam = new JMenuItem("Load exam results");

		JMenuItem compareAverage = new JMenuItem("Compare to Average");
		AverageListener avgListener = new AverageListener();
		compareAverage.addActionListener(avgListener);

		JMenuItem emailStudent = new JMenuItem("Email to Students");
		emailStudent.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if (anonLoaded == true && examLoaded == true){
					JFrame email = new Email(students, settingsFile);
				} else {
					JOptionPane.showMessageDialog(null, "Anonymous Markings Codes file needs to be"
							+ " uploaded \nExam marks file needs to be uploaded ");
				}

				
			}
		});
		data.add(emailStudent);
		data.add(compareAverage);

		JMenuItem fetchPart = new JMenuItem("Fetch Participation");
		fetchPart.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				emails = new ArrayList<String>();
				durations = new ArrayList<String>();
				JTextField moduleField = new JTextField(6);
			      JTextField urlField = new JTextField(20);

			      JPanel messagePanel = new JPanel();
			      messagePanel.setLayout(new BoxLayout(messagePanel,BoxLayout.PAGE_AXIS));
			      messagePanel.add(new JLabel("Module:"));
			      messagePanel.add(moduleField);
//			      messagePanel.add(Box.createHorizontalStrut(15)); // a spacer
			      messagePanel.add(new JLabel("URL:"));
			      messagePanel.add(urlField);

			      int response = JOptionPane.showConfirmDialog(null, messagePanel, 
			               "Please Enter module Code and the URL", JOptionPane.OK_CANCEL_OPTION);
			      
			      if (response == JOptionPane.OK_OPTION) {
			         System.out.println("Module code is: " + moduleField.getText());
			         System.out.println("URL is : " + urlField.getText());
			         while(moduleField.getText().equals("") && urlField.getText().equals("")){
				    	  response = JOptionPane.showConfirmDialog(null, messagePanel, 
					               "Please Enter module Code and the URL", JOptionPane.OK_CANCEL_OPTION);
				      }
			         	WebviewFrame wb = new WebviewFrame(urlField.getText());
						wb.btnFetch.addActionListener(new ActionListener() {
	
							@Override
							public void actionPerformed(ActionEvent e) {
								wb.browser.readDocument();
								emails = wb.getEmails();
								durations = wb.getDurations();
	
								System.out.println("Emails size is "
										+ emails.size() + " and duration size is "
										+ durations.size());
								for (int i = 0; i < emails.size(); i++) {
									for (Student s : students) {
										if (s.email.equals(emails.get(i))) {
											System.out.println("Found email of "
													+ emails.get(i)
													+ " with duration "
													+ durations.get(i));
											s.addParticipation(moduleField.getText()+ " "+durations.get(i)+" ago");
										}
									}
								}
								JOptionPane.showMessageDialog(null, emails.size()+" participation records were succesfully imported");
								wb.dispose();
							}
						});
			      }
			}

		});
		data.add(fetchPart);

		// Initiliases assesment arrayList
		assesments = new ArrayList<Assessment>();

		LoadListener loadListen = new LoadListener();
		// loadExam.addActionListener(loadListen);
		load.addActionListener(loadListen);

		loadExam.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				JFileChooser choosy = new JFileChooser();
				String assessment = "";
				File f = new File("C://Users//Saif//workspace");
				choosy.setCurrentDirectory(f);

				// Creates filter so user can only select CSV file
				FileNameExtensionFilter filter = new FileNameExtensionFilter(
						"CSV Files", "csv");
				choosy.setFileFilter(filter);

				// Checks if a file has been opened
				int returnValue = choosy.showOpenDialog(StudentFrame.this);
				if (returnValue == JFileChooser.APPROVE_OPTION) {
					// Sets file to chosen file
					File file = choosy.getSelectedFile();
					try {
						BufferedReader bf = new BufferedReader(new FileReader(
								file));
						// Finds corresponding column indexes
						// Reads first line to get column headings
						String line = bf.readLine();
						String[] linesplit = line.split(",");

						int moduleCol = 0;
						int assCol = 0;
						int candCol = 0;
						int markCol = 0;
						int gradeCol = 0;
						for (int i = 0; i < linesplit.length; i++) {
							System.out.println(linesplit[i]);
							if (linesplit[i].equals("\"#Module\"")
									|| linesplit[i].equals("#Module")) {
								moduleCol = i;
							} else if (linesplit[i].equals("\"#Ass#\"")
									|| linesplit[i].equals("#Ass#")) {
								assCol = i;
							} else if (linesplit[i].equals("\"#Cand Key\"")
									|| linesplit[i].equals("#Cand Key")) {
								candCol = i;
							} else if (linesplit[i].equals("\"Mark\"")
									|| linesplit[i].equals("Mark")) {
								markCol = i;
							} else if (linesplit[i].equals("\"Grade\"")
									|| linesplit[i].equals("Grade")) {
								gradeCol = i;
							}
							;
						}

						// Adds records to assessments
						while ((line = bf.readLine()) != null) {
							linesplit = line.split(",");
							String ass = linesplit[assCol].replaceAll("\"", "");
							Result temp = new Result(linesplit[moduleCol], ass,
									linesplit[candCol], Integer
											.parseInt(linesplit[markCol]),
									linesplit[gradeCol]);
							assessment = temp.getModuleCode();
							// First checks if Assessment array is empty
							if (assesments.isEmpty()) {
								Assessment t1 = new Assessment();
								t1.addResult(temp);
								assesments.add(t1);
								// Now checks if there is already an assessment
								// object
								// With same assessment number
								// If not make new assessment object
								// Then add record
							} else if (!checkAllAss(temp.getAssessment())) {
								Assessment t1 = new Assessment();
								t1.addResult(temp);
								assesments.add(t1);
							} else {
								// Since there is existing assessment object
								// Finds it, and adds record
								for (int i = 0; i < assesments.size(); i++) {
									if (assesments.get(i).results.get(0)
											.getAssessment()
											.equals(temp.getAssessment())) {
										assesments.get(i).addResult(temp);
									}
								}
							}

						}
						// De-annonymises records
						deAnnonymise();
						tabbedPane();
						examLoaded = true;

					} catch (FileNotFoundException p) {
						System.out.println("File not found");
					} catch (IOException g) {
						System.out.println("Error");
					}

				}

			}

		});
		// tabbedPane.setTabPlacement(JTabbedPane.TOP);
		// tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
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
		
		//Adds menu items
		menu.add(file);
		menu.add(data);
		menu.add(extra);

		// Adds the list as a ScrollPane so there is a scrollBar
		add(new JScrollPane(list), BorderLayout.WEST);
		add(panel, BorderLayout.NORTH);
		add(tabbedPane, BorderLayout.CENTER);
		validate();
		setVisible(true);

	}

	public void tabbedPane() {
		String name = "";
		int count = 0;
		// loops through each assesment and creates a tab and a table for that
		// assessment
		for (Assessment a : assesments) {
			name = (a.getModuleCode(a.getIndex(count))).replaceAll("\"", "")
					+ " " + a.getAssessment(a.getIndex(count));
			count++;
			tabbedPane.addTab(name, addJTable(a));
		}
		assesments.clear();
	}

	/**
	 * Checks through all Assessment Objects Within Assessment ArrayList If
	 * finds existing one, that matches string returns true
	 * 
	 * @param s
	 *            - assessment code
	 * @return
	 */
	public boolean checkAllAss(String s) {
		for (Assessment t : assesments) {
			if (t.results.get(0).getAssessment().equals(s)) {
				// If does have assessment already
				return true;
			}
		}
		return false;
	}

	/**
	 * matches anonymous marking codes in records with students in arraylist If
	 * found, replaces with student numbers
	 */
	public void deAnnonymise() {
		System.out.println("Starting deannonymising...");
		for (Assessment a : assesments) {
			for (Result t : a.results) {
				String candKey = t.getCandKey();
				candKey = candKey.replaceAll("\"", "");

				// Checks if candKey is actually student number
				// If it's coursework, it will enter this if statement
				if (candKey.substring(candKey.length() - 2,
						candKey.length() - 1).equals("/")) {
					System.out.println("Coursework");
					// Removes the end /1 or /2 after student number
					candKey = candKey.substring(0, candKey.length() - 2);
					candKey = candKey.replaceAll("#", "");
					t.candKey = candKey;

					for (Student s : students) {
						candKey = candKey.replaceAll("#", "");
						if (candKey.equals(s.studentNumber + "")) {
							// Finds student with matching student numbers
							// System.out.println("Found Student "+s.studentNumber+" who matches on JTable with sNumber "+candKey);
							String modCode = t.getModuleCode().replaceAll("\"",
									"");
							s.addMarks(modCode + " " + t.getAssessment(),
									t.mark);
						}
					}
				} else
					for (Student s : students) {
						candKey = candKey.replaceAll("#", "");
						if (candKey.equals(s.aMC)) {
							// Finds student with matching anonymous marking
							// code
							// Replaces it with student number
							t.candKey = s.getStudentNumber();
							s.addMarks(
									t.getModuleCode() + " " + t.getAssessment(),
									t.mark);
						}
					}

			}
		}

	}

	public JScrollPane addJTable(Assessment ass) {
		JTable table;
		DefaultTableModel model = new DefaultTableModel() {
			@Override
			public boolean isCellEditable(int row, int column) {
				// Disables all cells from being editable
				return false;
			}
		};
		table = new JTable(model);

		// Assigns column headings
		model.addColumn("Module Code");
		model.addColumn("Ass");
		model.addColumn("Cand Key");
		model.addColumn("Mark");
		model.addColumn("Grade");

		// Sets cell selection to single
		// So only one cell is selected
		// Also retrieves data when column 2 is clicked
		table.setCellSelectionEnabled(true);
		ListSelectionModel cellSelectionModel = table.getSelectionModel();
		cellSelectionModel
				.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

		cellSelectionModel
				.addListSelectionListener(new ListSelectionListener() {
					public void valueChanged(ListSelectionEvent e) {
						int row = table.getSelectedRow();
						int column = table.getSelectedColumn();
						// Checks if column is candidate key column/student
						// number
						if (column == 2) {
							// Create Display PopUp
							String selectedItem = (String) table.getValueAt(
									row, column);
							if (!selectedItem.substring(0, 1).equals("#")) {
								System.out.println("Create Display PopUp");
								showDisplayPopUp(selectedItem);
							} else {
								System.out
										.println("Not valid student Number/Anonymous marking code present");
							}

						}
					}

				});

		System.out.println("Making JTable");
		// Fetches first assessment and adds to table
		// for (Assessment t : assessments) {
		for (Result r : ass.results) {
			model.addRow(new Object[] { r.getModuleCode().replaceAll("\"", ""),
					r.getAssessment(), r.getCandKey().replaceAll("\"", ""),
					r.getMark(), r.getGrade() });
		}
		// break;
		// }

		table.setPreferredScrollableViewportSize(new Dimension(200, 300));
		table.setFillsViewportHeight(true);
		table.setGridColor(Color.GRAY);
		JScrollPane scrollPane = new JScrollPane(table);
		add(scrollPane, BorderLayout.CENTER);
		repaint();
		revalidate();
		return scrollPane;

	}

	public JList createJList(ArrayList<Student> students) {

		DefaultListModel defListMod = new DefaultListModel();// create a list of
																// items that
																// are editable.
																// original list
		// does not allow you to do that. this allows more flexibility

		// goes through arraylist of Student objects, calls toString and adds
		// the Strings to DefaultListModel (DLM)
		for (Student s : students) {
			defListMod.addElement(s.toString());
		}

		list = new JList(defListMod);// creates a new JList using the DLM
		MouseListener mouseListener = new MouseAdapter() {
			public void mouseClicked(MouseEvent e) {
				// Student findStudent = null;
				if(e.getClickCount() == 2){
				String selectedItem = (String) list.getSelectedValue()
						.toString();
				showDisplayPopUp(selectedItem);
				}
			}

		};
		list.addMouseListener(mouseListener);
		list.setFont(new Font("Arial", Font.PLAIN, 20));
		return list;
	}

	public void showDisplayPopUp(String data) {
		Student findStudent = null;
		findStudent = findStudent(data, students);
		if (display != null) {
			if (display.name.getText().equals(findStudent.name)) {
				// Debugging purposes
				System.out.println("Disposed");
				display.dispose();
			}
		}
		display = new DisplayPopUpFrame(findStudent);

	}

	public Student findStudent(String check, ArrayList<Student> studentArrayList) {
		Student found = null;

		for (int i = 0; i < studentArrayList.size(); i++) {
			// Checks if searching using student Number or toString
			if (!check.substring(check.length() - 1, check.length())
					.equals(")")) {
				if (studentArrayList.get(i).getStudentNumber().equals(check)) {
					found = studentArrayList.get(i);
				}
			} else {
				if (studentArrayList.get(i).toString().equals(check)) {
					found = studentArrayList.get(i);
				}

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

	private class AverageListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {

			if (examLoaded == true && anonLoaded == true) {

				JScrollPane currentScrollPane = (JScrollPane) tabbedPane
						.getComponentAt(tabbedPane.getSelectedIndex());
				JViewport viewport = currentScrollPane.getViewport();
				JTable currentTable = (JTable) viewport.getView();

				XYSeries data = new XYSeries("Student");// Will hold our data,
														// or plot points

				int numOfRecords = currentTable.getRowCount();

				// Loops through the records, gets the appropriate student
				// object from the arraylist,
				// gets the average of the student and plots it with their mark.
				for (int i = 0; i < numOfRecords; i++) {
					String tempCandKey = (String) currentTable.getValueAt(i, 2);
					System.out.println(tempCandKey);
					Student tempStu = findStudent(tempCandKey, students);// The
																			// student
																			// of
																			// a
																			// specific
																			// row

					// if the tempStu is not null, then gets their mark from the
					// table
					// and then plots the tempStu's average against their
					// current mark
					if (!(tempStu == null)) {
						System.out.println(tempStu.toString());
						int stuMarkInt = (Integer) currentTable
								.getValueAt(i, 3);
						// double stuMark = (double) stuMarkInt;
						// System.out.println(stuMark);
						tempStu.calcAverage();
						data.add(tempStu.average, stuMarkInt);
					} else {
						System.out.println("Error error error");
					}
				}

				String modCode = (String) currentTable.getValueAt(0, 0);
				System.out.println("Making chart...");
				ScatterPlot scatter = new ScatterPlot("Graph",
						"Comparison of Average in Assessment", modCode, data);

			} else if (anonLoaded == true && examLoaded == false) {
				JOptionPane
						.showMessageDialog(rootPane,
								"You need to load an exam results file, before you can create the chart");
			} else if (anonLoaded == false && examLoaded == false) {
				JOptionPane
						.showMessageDialog(
								rootPane,
								"You need to first load an anonymous marking code file, then an exam results file");
			}

		}

	}

	private class LoadListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			JFileChooser choosy = new JFileChooser();

			File f = new File("C://Users");
			choosy.setCurrentDirectory(f);

			// Creates filter so user can only select CSV file
			FileNameExtensionFilter filter = new FileNameExtensionFilter(
					"CSV Files", "csv");
			choosy.setFileFilter(filter);

			// choosy.showOpenDialog(StudentFrame.this);//sets position of
			// dialog box to default(centre) of the screen
			// //alternatively, we can change parameter to "StudentFrame.this".
			// This means that dialog box will appear
			// //wherever the main frame is.

			int returnValue = choosy.showOpenDialog(StudentFrame.this);
			if (returnValue == JFileChooser.APPROVE_OPTION) {
				// Just some code to help with debugging later
				File file = choosy.getSelectedFile();
				int succesImport = 0;
				int totalImports = 0;
				try {
					BufferedReader bf = new BufferedReader(new FileReader(file));
					while (bf.ready()) {
						String[] line = bf.readLine().split(",");
						for (Student s : students) {
							int temp = Integer.parseInt(line[0]);
							if (temp == s.studentNumber) {
								s.setAMC(line[1]);
								succesImport++;
							}
						}

						totalImports++;
					}
					int failedImports = totalImports - succesImport;
					String results = "Annonymous marking codes imported. "
							+ succesImport
							+ " codes were \nfor known students; "
							+ failedImports + " were or unknown students";
					JOptionPane.showMessageDialog(StudentFrame.this, results);

					anonLoaded = true;

				} catch (FileNotFoundException p) {
					System.out.println("File not found");
				} catch (IOException g) {
					System.out.println("Error");
				}

				System.out.println("You have chosen "
						+ choosy.getSelectedFile().getName()
						+ " to be imported");

			}

		}

	}

	public Student findUsingAnon(String anon) {
		anon = anon.replaceAll("\"", "");
		anon = anon.replaceAll("#", "");
		Student found = null;
		for (Student s : students) {
			if (anon.equals(s.aMC)) {
				found = s;
			}
		}

		return found;
	}

	public void findSettingsFile() {

		String OS = System.getProperty("os.name").toLowerCase();

		if (OS.contains("windows")) {
			String user = System.getProperty("user.name");
			String filePathStr = "C:\\Users\\" + user + "\\Documents";
			System.out.println(filePathStr);
			filePathStr += "\\settings.ini";
			System.out.println(filePathStr);

			File f = new File(filePathStr);

			if (f.exists() && !f.isDirectory()) {
				System.out.println("Settings.ini exists");
				settingsFile = f;
			} else {
				System.out.println("Settings.ini doesn't exist yet");
			}

		} else if (OS.contains("mac")) {
			String user = System.getProperty("user.name");
			String filePathStr = "/Users/" + user + "/Desktop";
			System.out.println(filePathStr);
			filePathStr += "/settings.ini";
			System.out.println(filePathStr);

			File f = new File(filePathStr);

			if (f.exists() && !f.isDirectory()) {
				System.out.println("Settings.ini exists");
				settingsFile = f;
			} else {
				System.out.println("Settings.ini doesn't exist yet");
			}
		}
	}

}
