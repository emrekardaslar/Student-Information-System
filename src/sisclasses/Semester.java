package sisclasses;
import java.util.ArrayList;

public class Semester {
	private String name;
	private ArrayList<Course> courseList; //each semester have a name and list of course
	
	public Semester() {  //default constructor for the class
		this.name="";
		this.courseList = new ArrayList<Course>();
	}
	
	public Semester(String name) {  //constructor for the class
		this.name=name;
		this.courseList = new ArrayList<Course>();
	}
	
	public void addCourse(Course course) { //method for adding course to the list of course
		this.courseList.add(course);   
	}
	
	public double getTotalScore() { //method for obtaining total score of a list of course
		double totalScore=0;
		for(int i=0;i<this.courseList.size();i++) {
			totalScore += courseList.get(i).getTotalScore();
		}	
		return totalScore;
	}
	
	public int getTotalCredit() { //method for obtaining total credit of a list of course
		int totalCredit=0;
		for(int i=0;i<this.courseList.size();i++) {
			totalCredit += courseList.get(i).getCredit();
		}	
		return totalCredit;
	}
	
	public double getAverageScore() {  //method for getting the average score by dividing the total score to total credit
		return (this.getTotalScore() / this.getTotalCredit());
	}
	//getters and setters
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public ArrayList<Course> getCourseList() {
		return courseList;
	}
	public void setCourseList(ArrayList<Course> courseList) {
		this.courseList = courseList;
	}
	
	public int getSize() {
		return this.courseList.size();
	}
	
	public Course getCourse(int i) {
		return this.courseList.get(i);
	}
}
