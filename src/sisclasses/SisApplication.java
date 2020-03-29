package sisclasses;

import java.awt.Dimension;

import javax.swing.JFrame;
import javax.swing.JTabbedPane;

public class SisApplication {

	public static void main(String[] args) {
		JFrame frame = new JFrame("Student Information System"); //frame is created
		frame.setLocation(350, 200);
		frame.setPreferredSize(new Dimension(1000, 800));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		JTabbedPane tp = new JTabbedPane();
		tp.addTab("Enter Grades", new EnterGradesPanel()); //tab panel for entering grades
		tp.addTab("Transcript", new TranscriptPanel()); //tab panel for displaying transcript
		tp.addTab("Show Semester", new ShowSemesterPanel()); //tab panel for displaying a semester
		frame.getContentPane().add(tp); //tabs are added to the frame
		frame.pack(); //sizes are set
		frame.setVisible(true);
	}
}
