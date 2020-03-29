package sisclasses;

public class Course {
	private String courseCode;
	private int credit;
	private String letterGrade;  //each course have a course code, credit and letter grade
	
	
	public Course() { //default constructor for the class
		this.courseCode="";
		this.credit=0;
		this.letterGrade="F1";
	}
	
	public Course(String courseCode, int credit, String letterGrade) { //constructor for the class
		this.courseCode=courseCode;
		this.credit=credit;
		this.letterGrade=letterGrade;
	}
	
	public double findCorrespondingScore(String letterGrade) {  //the letter grades are transformed to scores in order to do calculations
		switch(letterGrade) {
			case "A":
				return 4.0;
			case "A-":
				return 3.7;
			case "B+":
				return 3.3;
			case "B":
				return 3.0;
			case "B-":
				return 2.7;
			case "C+":
				return 2.3;
			case "C":
				return 2;
			case "C-":
				return 1.7;
			case "D+":
				return 1.3;
			case "D":
				return 1;
			case "F1":
				return 0;
			case "F2":
				return 0;		
		}
		return 0;
	}
	
	public double getScore() { //method for getting the score of a course
		return this.findCorrespondingScore(this.letterGrade);
	}
	
	public double getTotalScore() { //method for calculating the total score 
		return this.getScore() * this.getCredit(); //total score is multiplication of course credit and score
	}
	//getters and setters
	public String getCourseCode() {
		return courseCode;
	}
	public void setCourseCode(String courseCode) {
		this.courseCode = courseCode;
	}
	public int getCredit() {
		return credit;
	}
	public void setCredit(int credit) {
		this.credit = credit;
	}
	public String getLetterGrade() {
		return letterGrade;
	}
	public void setLetterGrade(String letterGrade) {
		this.letterGrade = letterGrade;
	}	
}
