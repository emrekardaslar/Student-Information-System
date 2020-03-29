package sisclasses;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;

public class TranscriptPanel extends JPanel implements ActionListener {
	private static String semesterLabel = "Semester: "; //label of showing the semesters
	private static String courseLabel = "Course: "; 
	private Transcript transcript;
	private static DecimalFormat df1 = new DecimalFormat("#.#"); //format for scores of courses
	private static DecimalFormat df2 = new DecimalFormat("#.##"); //format for semester average and GPA
	private JTextArea transcriptArea;
	private JButton showBtn;
	
	public TranscriptPanel() {
		setLayout(null);
		transcript = new Transcript();
		showBtn = new JButton("Show");
		transcriptArea = new JTextArea(4,20);
		showBtn.setBounds(800,340,100,30);
		transcriptArea.setBounds(0,0,500,900);
		transcriptArea.setEditable(false); //gui elements and their features are set
		add(showBtn);
		add(transcriptArea);
		JScrollPane scrollPane = new JScrollPane(transcriptArea);
		scrollPane.setBounds(5,5,780,500);
		add(scrollPane);
		showBtn.addActionListener(this);
	}
	
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == showBtn) {
			transcriptArea.setText("");
			this.transcript.getSemesterList().clear();
			loadFromFile();			
			for (int i=0;i<this.transcript.getSize();i++) {
				this.displaySemester(i);
			}
		}
	}
	
	public void displaySemester(int semesterIndex) { //method for displaying the semester
		if (semesterIndex >= 0) {
			Semester semester = this.transcript.getSemester(semesterIndex);
			transcriptArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			transcriptArea.append(semester.getName()+"\n");
			transcriptArea.append(String.format("%-10s%-10s%-10s%-10s\n","Code","Credit","Letter","Score")); //column headers
			for (int i=0; i<semester.getSize();i++) {
				Course course = semester.getCourse(i);
				transcriptArea.append(String.format("%-10s%-10s%-10s%-10s\n", course.getCourseCode(),course.getCredit(),course.getLetterGrade(),
						EnterGradesPanel.formatDouble(course.getTotalScore(),1))); //course code, credit, letter and score is displayed in a format
			}		
			double previousScore = this.transcript.getPreviousScore(semesterIndex); //previous score is calculated by getPreviousScore method
			int previousCredit = this.transcript.getPreviousCredit(semesterIndex); //previous credit is calculated by getPreviousCredit method
			double totalScore = previousScore + semester.getTotalScore(); //total score is calculated by addition of previous score and the total score of the semester
			int totalCredit = previousCredit + semester.getTotalCredit(); //total credit is calculated by addition of previous credit and the total credit of the semester
			transcriptArea.append("\nPrevious Score: " + EnterGradesPanel.formatDouble(previousScore,1) +"\n"); 
			transcriptArea.append("Previous Credit: "+previousCredit +"\n");
			transcriptArea.append("Semester Average: "+EnterGradesPanel.formatDouble(semester.getAverageScore(),2)+"\n");
			transcriptArea.append("Semester Credit: "+semester.getTotalCredit() + "\n");
			transcriptArea.append("Semester Score: "+EnterGradesPanel.formatDouble(semester.getTotalScore(),1)+"\n");
			transcriptArea.append("Total Score: "+EnterGradesPanel.formatDouble(totalScore,1)+"\n");
			transcriptArea.append("Total Credit: "+totalCredit+"\n");
			transcriptArea.append("GPA: "+EnterGradesPanel.formatDouble(totalScore / totalCredit , 2)+"\n");
			transcriptArea.append("Academic success: ");
			if ((semester.getAverageScore()) >= 3.5) {
				transcriptArea.append("High Honor"+"\n");
			}
			
			else if ((semester.getAverageScore()) >= 3.0) {
				transcriptArea.append("Honor"+"\n");
			}
			
			else if ((semester.getAverageScore()) >= 2.0) {
				transcriptArea.append("Successful"+"\n");
			}
			else if ((semester.getAverageScore()) < 2.0 && semester.getAverageScore() >= 1.79) {
				transcriptArea.append("Conditional success"+"\n");
			}
			else {
				transcriptArea.append("Fail"+"\n");
			}
			
			transcriptArea.append("--------------------------------------------------------------\n"); 
		} //calculated fields of the semester are displayed 
	}
	public void loadFromFile() { //method for loading the file to the memory
		File file =new File("Transcript.txt");
		try {
			Scanner scanner = new Scanner(new FileInputStream(file));
			Semester semester = new Semester();
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.startsWith(TranscriptPanel.semesterLabel)) { //if the line starts with "Semester: " label,
					line = line.substring(TranscriptPanel.semesterLabel.length()); //semester name is get by substring method
					semester = new Semester(line);
					this.transcript.addSemester(semester); //semester is added to the transcript by its name
				}
				else if (line.startsWith(TranscriptPanel.courseLabel)) { //if the line starts with "Course: " label,
					line = line.substring(TranscriptPanel.courseLabel.length()); //course code, credit and letter grade is get by substring method
					String[] courseArr = line.split(" "); //course code, credit and letter grade are split by empty spaces and assigned into an array
					Course course = new Course(courseArr[0],Integer.parseInt(courseArr[1]),courseArr[2]); //new course is created by its constructor
					semester.addCourse(course); //the course is added to the semester by course code, credit and letter grade
				}
			}
			scanner.close();
		} 
		catch (FileNotFoundException e) {
			JOptionPane.showMessageDialog(this, "Please add semesters to display transcript"); //if there is not any file created, message will be displayed
		}
	}

	public static String formatDouble(double d,int decimal) { //a formatter which formats a double number by decimals
		if (decimal==1)
			return TranscriptPanel.df1.format(d);
		else
			return TranscriptPanel.df2.format(d);
	}	
	
}
