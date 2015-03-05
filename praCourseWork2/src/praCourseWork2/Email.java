package praCourseWork2;

import java.awt.BorderLayout;
import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.ListCellRenderer;

public class Email extends JFrame {
	private JPanel main;
	private JList list;
	private JPanel west;
	private JPanel buttons;
	private JPanel listPanel;

	public Email(ArrayList<Student> students){
		JButton selectAll = new JButton("select all");
		JButton selectNone = new JButton("select none");
		
		main = new JPanel(new BorderLayout());
		buttons = new JPanel();
		listPanel = new JPanel();
		west = new JPanel(new BorderLayout());

		setSize(550, 300);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		list = new JList(students.toArray());
		list.setCellRenderer(new CheckboxListCellRenderer());

		buttons.add(selectNone);
		buttons.add(selectAll);
		
		listPanel.add(new JScrollPane(list));
		
		west.add(buttons,BorderLayout.NORTH);
		west.add(listPanel, BorderLayout.CENTER);
		
		main.add(west, BorderLayout.WEST);
		setVisible(true);
		add(main);
		}
	
	public class CheckboxListCellRenderer extends JCheckBox implements ListCellRenderer {

	    public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {

	    	// need this in order to actually select the JCheckBox
	        setSelected(isSelected);
	        setEnabled(list.isEnabled());
	        // need this in order to display the student with checkbox
	        setText(value == null ? "" : value.toString());  
	        // make the background and Foreground the same as the list
	        setBackground(list.getBackground());
	        setForeground(list.getForeground());
	        return this;
	    }     
	}
}

