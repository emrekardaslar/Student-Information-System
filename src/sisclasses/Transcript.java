package sisclasses;
import java.util.ArrayList;
import java.util.Collections;

public class Transcript {
	private ArrayList<Semester> semesterList; //each transcript have a list of semester

	public Transcript() { //default constructor for the class
		this.semesterList = new ArrayList<Semester>();
	}
	
	public void addSemester(Semester semester) { //method for adding a semester to the semester list of the class
		this.semesterList.add(semester);
		Collections.sort(this.semesterList, new SemesterComparator()); //semesters are sorted by a custom method
		
	}
	
	public int getSize() { //method for getting the size of semester list
		return this.semesterList.size();
	}
	
	public Semester getSemester(int i) { //method for getting the semester from specified index
		return this.semesterList.get(i);
	}
	
	public int getSemester(String name) { //method for getting semester by its name
		for (int i=0;i<this.getSize();i++) {
			Semester semester = this.getSemester(i);
			if (semester.getName().equals(name)) {
				return i;
			}
		}
		return -1; //if the semester cannot be get, method returns -1
	}
	
	public double getPreviousScore(int index) { //method for getting the previous score by the index
		double totalScore = 0.0;
		for (int i=0;i<index;i++) {
			totalScore += this.getSemester(i).getTotalScore();
		}
		return totalScore; //previous score is found by adding the total scores of older semesters since we sort the semesters in the semester list.
	}
	
	public int getPreviousCredit(int index) { //method for getting the previous credit by the index
		int totalCredit = 0;
		for (int i=0;i<index;i++) {
			totalCredit += this.getSemester(i).getTotalCredit();
		}
		return totalCredit; //previous credit is found by adding the total credits of older semesters since we sort the semesters in the semester list.
	}
	
	//getters and setters
	public ArrayList<Semester> getSemesterList() {
		return semesterList;
	}
	
	public void setSemesterList(ArrayList<Semester> semesterList) {
		this.semesterList = semesterList;
	}
}
