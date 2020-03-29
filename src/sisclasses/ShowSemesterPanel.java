package sisclasses;

import java.awt.Font;
import java.awt.event.*;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Scanner;

import javax.swing.*;

public class ShowSemesterPanel extends JPanel implements ActionListener {
	private static String ssemesterLabel = "Semester: "; //label of showing the semesters
	private static String scourseLabel = "Course: "; 
	private JButton showBtn;
	private JLabel semesterLabel;
	private JTextField semesterTextLabel;
	private JTextArea semesterArea;
	private Transcript transcript;
	
	public ShowSemesterPanel() {
		setLayout(null);
		transcript = new Transcript();
		showBtn = new JButton("Show");
		semesterLabel = new JLabel("Semester: ");
		semesterTextLabel = new JTextField(20);
		semesterArea = new JTextArea(5,20);
		showBtn.setBounds(800,340,100,30);
		semesterArea.setBounds(0,0,500,900);
		semesterArea.setEditable(false);
		semesterLabel.setBounds(800,280,100,20);
		semesterTextLabel.setBounds(800,310,100,20); //gui elements and their features are set
		add(semesterArea);
		JScrollPane scrollPane = new JScrollPane(semesterArea);
		scrollPane.setBounds(5,5,780,500);
		add(scrollPane);
		add(semesterLabel);
		add(semesterTextLabel);
		add(showBtn);
		
		showBtn.addActionListener(this);
	}
	

	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == showBtn) {
			semesterArea.setText(""); //semesterArea gets cleared
			this.transcript.getSemesterList().clear(); //semester list is cleared
			loadFromFile(); //the file is loaded
			displaySemester(this.transcript.getSemester(semesterTextLabel.getText())); //semester is displayed by the name 
		}	
	}
	
	public void displaySemester(int semesterIndex) { //method for displaying the semester
		if (semesterIndex >= 0) {
			Semester semester = this.transcript.getSemester(semesterIndex);
			semesterArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
			semesterArea.append(semester.getName()+"\n");
			semesterArea.append(String.format("%-10s%-10s%-10s%-10s\n","Code","Credit","Letter","Score")); //column headers
			for (int i=0; i<semester.getSize();i++) {
				Course course = semester.getCourse(i);
				semesterArea.append(String.format("%-10s%-10s%-10s%-10s\n", course.getCourseCode(),course.getCredit(),course.getLetterGrade(),
						EnterGradesPanel.formatDouble(course.getTotalScore(),1))); //course code, credit, letter and score is displayed in a format
			}		
			double previousScore = this.transcript.getPreviousScore(semesterIndex); //previous score is calculated by getPreviousScore method
			int previousCredit = this.transcript.getPreviousCredit(semesterIndex); //previous credit is calculated by getPreviousCredit method
			double totalScore = previousScore + semester.getTotalScore(); //total score is calculated by addition of previous score and the total score of the semester
			int totalCredit = previousCredit + semester.getTotalCredit(); //total credit is calculated by addition of previous credit and the total credit of the semester
			semesterArea.append("\nPrevious Score: " + EnterGradesPanel.formatDouble(previousScore,1) +"\n"); 
			semesterArea.append("Previous Credit: "+previousCredit +"\n");
			semesterArea.append("Semester Average: "+EnterGradesPanel.formatDouble(semester.getAverageScore(),2)+"\n");
			semesterArea.append("Semester Credit: "+semester.getTotalCredit() + "\n");
			semesterArea.append("Semester Score: "+EnterGradesPanel.formatDouble(semester.getTotalScore(),1)+"\n");
			semesterArea.append("Total Score: "+EnterGradesPanel.formatDouble(totalScore,1)+"\n");
			semesterArea.append("Total Credit: "+totalCredit+"\n");
			semesterArea.append("GPA: "+EnterGradesPanel.formatDouble(totalScore / totalCredit , 2)+"\n");
			semesterArea.append("Academic success: ");
			if ((semester.getAverageScore()) >= 3.5) {
				semesterArea.append("High Honor"+"\n");
			}
			
			else if ((semester.getAverageScore()) >= 3.0) {
				semesterArea.append("Honor"+"\n");
			}
			
			else if ((semester.getAverageScore()) >= 2.0) {
				semesterArea.append("Successful"+"\n");
			}
			else if ((semester.getAverageScore() >= 1.8 && (semester.getAverageScore() < 2.0 ))) {
				semesterArea.append("Conditional success"+"\n");
			}
			else {
				semesterArea.append("Fail"+"\n");
			}
			semesterArea.append("--------------------------------------------------------------\n"); 
		} //calculated fields of the semester are displayed 
		else {
			JOptionPane.showMessageDialog(this, "Semester does not exist."); //shows a message if there is not a semester named like it is
		}
	}
	
	public void loadFromFile() { //method for loading the file to the memory
		File file =new File("Transcript.txt");
		
		try {
			Scanner scanner = new Scanner(new FileInputStream(file));
			Semester semester = new Semester();
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.startsWith(ShowSemesterPanel.ssemesterLabel)) { //if the line starts with "Semester: " label,
					line = line.substring(ShowSemesterPanel.ssemesterLabel.length()); //semester name is get by substring method
					semester = new Semester(line);
					this.transcript.addSemester(semester); //semester is added to the transcript by its name
				}
				else if (line.startsWith(ShowSemesterPanel.scourseLabel)) { //if the line starts with "Course: " label,
					line = line.substring(ShowSemesterPanel.scourseLabel.length()); //course code, credit and letter grade is get by substring method
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
}
