package graph;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;

public class JPGListener implements ActionListener{
	
	private JFreeChart chart;
	private String modCode;
	/**
	 * Initiliazes listener with scatter graph and module code
	 * @param freeChart - Scatter plot
	 * @param module - Module Code
	 */
	public JPGListener(JFreeChart freeChart, String module){
		this.chart = freeChart;
		this.modCode = module;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		JMenuItem temp = (JMenuItem) e.getSource();
		
		JFileChooser chooser = new JFileChooser(){
			
			public void approveSelection(){//Here, the behaviour of the approve button of the JFileChooser is altered
			
			File filePath = super.getSelectedFile();
			
			String pathStr = filePath.getPath();
			
			if(!pathStr.endsWith(".jpg")){//if file path doesn't have ".jpg"...
				pathStr += ".jpg"; //...appends ".jpg" to the end
				filePath = new File(pathStr); //Updates filePath with new path
			}
			
			if (filePath.exists() && getDialogType() == SAVE_DIALOG){//If there exists a file with the same name and path
				int decision = JOptionPane.showConfirmDialog(this, "This file already exists. Would you like to overwrite?", "File Exists", 
						JOptionPane.YES_NO_CANCEL_OPTION);
				switch (decision){
				case JOptionPane.YES_OPTION:
						try {
							ChartUtilities.saveChartAsPNG(filePath, chart, 600, 400);
							JOptionPane.showMessageDialog(this, "Chart saved as JPG file", "Success", JOptionPane.INFORMATION_MESSAGE);
							this.cancelSelection();
						
						} catch (IOException e1) {
							JOptionPane.showMessageDialog(this, "Failed to export chart", "Error", JOptionPane.ERROR_MESSAGE);
						}
					super.approveSelection();//Goes ahead with the file saving
				case JOptionPane.NO_OPTION:
					return;
				case JOptionPane.CLOSED_OPTION:
					return;
				case JOptionPane.CANCEL_OPTION:
					cancelSelection();
					return;
				
				}
			}
			
			try {
					ChartUtilities.saveChartAsPNG(filePath, chart, 600, 400);
					JOptionPane.showMessageDialog(null, "Chart saved as JPG file", "Success", JOptionPane.INFORMATION_MESSAGE);
					this.cancelSelection();
				
			} catch (IOException e1) {
				JOptionPane.showMessageDialog(null, "Failed to export chart", "Error", JOptionPane.ERROR_MESSAGE);
			}
			
			
			}
			
		};
		
		//Sets the text within the file name textfield to the module code, concatenates the file type to the end
		chooser.setSelectedFile(new File(modCode+"." + temp.getText().toLowerCase()));
		
		chooser.showSaveDialog(null);
	}
	
}
