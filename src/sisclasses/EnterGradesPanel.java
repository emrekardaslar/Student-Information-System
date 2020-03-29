package sisclasses;

import java.awt.*;
import java.awt.event.*;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Scanner;

import javax.swing.*;

public class EnterGradesPanel extends JPanel implements ActionListener{
	private static String ssemesterLabel = "Semester: "; //label of showing the semesters
	private static String scourseLabel = "Course: "; 
	private JLabel courseLabel, gradeLabel, creditLabel, semesterLabel, infoLabel;
	private JTextField courseTextLabel, creditTextLabel, semesterTextLabel;
	private JTextArea semesterTextArea;
	private JButton enterBtn, saveBtn;
	private Transcript transcript;
	private static DecimalFormat df1 = new DecimalFormat("#.#"); //format for scores of courses
	private static DecimalFormat df2 = new DecimalFormat("#.##"); //format for semester average and GPA
	private JComboBox<String> letterGrades;
	
	public EnterGradesPanel() {
		setLayout(null);
		String[] validGrades = { //valid grades for courses
				"A","A-",
				"B+","B","B-",
				"C+","C","C-",
				"D+","D","F1","F2"
		};
		letterGrades = new JComboBox<String>();
		for (int i=0; i<validGrades.length;i++) {
			letterGrades.addItem(validGrades[i]);
		} //user can select the grade from combobox
		
		letterGrades.setBounds(0,140,100,20);
		letterGrades.setSelectedItem(null);
		add(letterGrades);
		
		this.transcript = new Transcript();
		enterBtn = new JButton("Add"); 
		semesterLabel=new JLabel("Semester: ");
		courseLabel = new JLabel("Course Code: ");
		gradeLabel = new JLabel("Letter Grade: ");
		creditLabel = new JLabel("Credit: ");
		infoLabel = new JLabel("Adding Courses to Semester: ");
		semesterTextLabel = new JTextField(10);
		courseTextLabel = new JTextField(6);
		creditTextLabel = new JTextField(2);
		semesterTextArea = new JTextArea(5,20);
		saveBtn = new JButton("Save");
		semesterLabel.setBounds(0,0,100,20);
		semesterTextLabel.setBounds(0,20,100,20); //gui elements and their features are set
		
		add(semesterLabel);
		add(semesterTextLabel);
		
		courseLabel.setBounds(0,40,100,20);
		courseTextLabel.setBounds(0,60,100,20);
		
		add(courseLabel);
		add(courseTextLabel);
		
		creditLabel.setBounds(0,80,100,20);
		creditTextLabel.setBounds(0,100,100,20);
		
		add(creditLabel);
		add(creditTextLabel);
		
		gradeLabel.setBounds(0,120,100,20);	
		add(gradeLabel);

		
		enterBtn.setBounds(0,170,100,30); 
		add(enterBtn);
		infoLabel.setBounds(200,0,1000,20);
		add(infoLabel);
		semesterTextArea.setBounds(200,20,200,250); 
		add(semesterTextArea);
		
		JScrollPane scrollPane = new JScrollPane(semesterTextArea); //scroll bar is set for the semesterTextArea
		scrollPane.setBounds(200,20,500,500);
		add(scrollPane); //scroll bar is added
		
		saveBtn.setBounds(0,210,100,30);
		add(saveBtn);
		semesterTextArea.append(String.format("%-10s%-10s%-10s\n", "Code","Credit","Letter"));
		semesterTextArea.setEditable(false); //to prevent user from changing the semesterTextArea
			
		semesterTextArea.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 12));
		enterBtn.addActionListener(this);
		saveBtn.addActionListener(this);
		loadFromFile(); //to append new semesters to existing file
	}
	
	public void actionPerformed(ActionEvent e) {
			boolean validCredit = false, validGrade=false, validCourseCode=false;
			int courseCredit=0;
			String semesterName = semesterTextLabel.getText();
			String courseCode = courseTextLabel.getText();
			String letterGrade = letterGrades.getItemAt(letterGrades.getSelectedIndex());
	
			if (e.getSource()==enterBtn) {
				try {
					courseCredit = Integer.parseInt(creditTextLabel.getText());
				} 
				catch(NumberFormatException  ex) {
					JOptionPane.showMessageDialog(this, "Please enter an integer for credit");
				}
				validCourseCode = checkCourseCode(courseTextLabel.getText());
				validCredit = checkCourseCredit(creditTextLabel.getText());
				validGrade = checkLetterGrade(letterGrade);
				
				if (!validGrade) {
					JOptionPane.showMessageDialog(this, "Please select a valid letter grade");
				}
				
				if (!validCourseCode) {
					JOptionPane.showMessageDialog(this, "Please enter the course code");
				}

				if (validGrade && validCredit && validCourseCode) {
					semesterTextArea.append(String.format("%-10s%-10s%-10s\n", courseCode, courseCredit,letterGrade));
				}
			}
			
			if (e.getSource() == saveBtn) { //Saves the semester into the transcript
				if (semesterTextLabel.getText().equals("")) {
					JOptionPane.showMessageDialog(this, "Please enter the semester name");
				}
				else if (semesterTextArea.getText().equals(String.format("%-10s%-10s%-10s\n", "Code","Credit","Letter"))) {
					JOptionPane.showMessageDialog(this, "Please add at least 1 course");
				}

				else {	
					int confirm = JOptionPane.showConfirmDialog(null, "Save the semester", "You want to save the semester ?", 
	                            JOptionPane.YES_NO_OPTION); //Asks user to confirm saving method
					
					if (confirm == JOptionPane.YES_OPTION) {
						createSemester(semesterName);
						try {
							File file =new File("Transcript.txt");
					    	if(!file.exists()){ //Creates if the file does not exists (file is created inside the project folder)
					    	   file.createNewFile();
					    	}
					    	
							FileWriter fw = new FileWriter(file); 
							BufferedWriter writer = new BufferedWriter(fw);
							
							for (int i=0;i<this.transcript.getSize();i++) {
								Semester semester = this.transcript.getSemester(i);
								writer.write("Semester: " + semester.getName()+"\n"); //writes the semester name
								
								for (int j=0;j<this.transcript.getSemester(i).getSize();j++) {
									Course course = semester.getCourse(j);
									writer.write("Course: " +course.getCourseCode() + " " + course.getCredit() + " " + course.getLetterGrade()+"\n");
								} //grade information is written to the file, it is assumed that grade information covers course code, credit and letter grade
							}
							writer.close();
						}
						catch(IOException ioex) {
							JOptionPane.showMessageDialog(this, "Cannot open file");
						}			
					}
				}
			}	
	}
	
	public void loadFromFile() { //method for loading the file to the memory
		File file =new File("Transcript.txt");
		
		try {
			Scanner scanner = new Scanner(new FileInputStream(file));
			Semester semester = new Semester();
			while(scanner.hasNextLine()) {
				String line = scanner.nextLine().trim();
				if (line.startsWith(EnterGradesPanel.ssemesterLabel)) { //if the line starts with "Semester: " label,
					line = line.substring(EnterGradesPanel.ssemesterLabel.length()); //semester name is get by substring method
					semester = new Semester(line);
					this.transcript.addSemester(semester); //semester is added to the transcript by its name
				}
				else if (line.startsWith(EnterGradesPanel.scourseLabel)) { //if the line starts with "Course: " label,
					line = line.substring(EnterGradesPanel.scourseLabel.length()); //course code, credit and letter grade is get by substring method
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
	
	public void createSemester(String semesterName) { //create semester 
		Semester semester = new Semester(semesterName);
		String text = semesterTextArea.getText(); //gets the text from semesterTextArea
		String[] line = text.split("\\n");	//splits the text line by line
		for (int i = 1; i<line.length;i++) {
			String[] courseArr = line[i].split("[ ]+");
			Course course = new Course(courseArr[0],Integer.parseInt(courseArr[1]),courseArr[2]); //a new course is created by its constructor
			semester.addCourse(course); //the course is added to the semester
		}
		this.transcript.addSemester(semester); //the semester is added to the transcript
		semesterTextArea.setText(""); //after the operation, the semesterTextArea's text is cleared
		semesterTextArea.append(String.format("%-10s%-10s%-10s\n", "Code","Credit","Letter")); //formatted label is added back to the semesterTextArea
		
	}

	public static String formatDouble(double d,int decimal) { //a formatter which formats a double number by decimals
		if (decimal==1)
			return EnterGradesPanel.df1.format(d);
		else
			return EnterGradesPanel.df2.format(d);
	}
	
	public boolean checkLetterGrade(String letterGrade) {
		boolean done=false;
		if (letterGrade == null) {
			return done;
		}
		else {
			done=true;
		}
		return done;
	}
	
	public boolean checkCourseCode(String courseCode) {
		boolean done=false;
		if (courseCode.equals("")) {
			return done;
		}
		else {
			done=true;
		}
		return done;
	}
	
	public boolean checkCourseCredit(String courseCredit) {
		try {
			Integer.parseInt(courseCredit);
			return true;
		} catch(NumberFormatException  ex) {
			return false;
		}
	}

	public JButton getSaveBtn() {
		return saveBtn;
	}

	public void setSaveBtn(JButton saveBtn) {
		this.saveBtn = saveBtn;
	}

	public static DecimalFormat getDf1() {
		return df1;
	}

	public static void setDf1(DecimalFormat df1) {
		EnterGradesPanel.df1 = df1;
	}

	public static DecimalFormat getDf2() {
		return df2;
	}

	public static void setDf2(DecimalFormat df2) {
		EnterGradesPanel.df2 = df2;
	}

	public JLabel getCourseLabel() {
		return courseLabel;
	}

	public void setCourseLabel(JLabel courseLabel) {
		this.courseLabel = courseLabel;
	}

	public JLabel getGradeLabel() {
		return gradeLabel;
	}

	public void setGradeLabel(JLabel gradeLabel) {
		this.gradeLabel = gradeLabel;
	}

	public JLabel getCreditLabel() {
		return creditLabel;
	}

	public void setCreditLabel(JLabel creditLabel) {
		this.creditLabel = creditLabel;
	}

	public JLabel getSemesterLabel() {
		return semesterLabel;
	}

	public void setSemesterLabel(JLabel semesterLabel) {
		this.semesterLabel = semesterLabel;
	}

	public JTextField getCourseTextLabel() {
		return courseTextLabel;
	}

	public void setCourseTextLabel(JTextField courseTextLabel) {
		this.courseTextLabel = courseTextLabel;
	}

	public JTextField getCreditTextLabel() {
		return creditTextLabel;
	}

	public void setCreditTextLabel(JTextField creditTextLabel) {
		this.creditTextLabel = creditTextLabel;
	}

	public JTextField getSemesterTextLabel() {
		return semesterTextLabel;
	}

	public void setSemesterTextLabel(JTextField semesterTextLabel) {
		this.semesterTextLabel = semesterTextLabel;
	}

	public JTextArea getSemesterTextArea() {
		return semesterTextArea;
	}

	public void setSemesterTextArea(JTextArea semesterTextArea) {
		this.semesterTextArea = semesterTextArea;
	}

	public JButton getEnterBtn() {
		return enterBtn;
	}

	public void setEnterBtn(JButton enterBtn) {
		this.enterBtn = enterBtn;
	}

	public Transcript getTranscript() {
		return transcript;
	}

	public void setTranscript(Transcript transcript) {
		this.transcript = transcript;
	}

	public JLabel getInfoLabel() {
		return infoLabel;
	}

	public void setInfoLabel(JLabel infoLabel) {
		this.infoLabel = infoLabel;
	}

	public JComboBox<String> getLetterGrades() {
		return letterGrades;
	}

	public void setLetterGrades(JComboBox<String> letterGrades) {
		this.letterGrades = letterGrades;
	}
}