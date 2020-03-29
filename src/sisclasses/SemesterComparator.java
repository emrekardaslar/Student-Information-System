package sisclasses;
import java.util.Comparator;

public class SemesterComparator implements Comparator<Semester> {
    @Override
    public int compare(Semester s1, Semester s2) {
    	String[] s1Name = s1.getName().split(" ");
    	String[] s2Name = s2.getName().split(" "); 	
    	try {
    		if (s1Name[0].equals(s2Name[0])) {
    			//System.out.println("Same years are entered.");
        		return -1 * (s1.getName().compareTo(s2.getName()));
        	}
    	} catch(Exception exc) {
    		System.out.println("Semester name is shorter than expected. ");
    	}
		
        return s1.getName().compareTo(s2.getName()); //semesters are sorted by their name alphabetically
    }
}