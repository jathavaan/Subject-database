package subject_database.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public abstract class UserSubjects {
	
	private ArrayList<ArrayList<String>> userSubjects = new ArrayList<ArrayList<String>>();
	
	public ArrayList<ArrayList<String>> getUserSubjects() {
		return userSubjects;
	}
	
	public void setUserSubjects(String subjectCode, char grade) throws FileNotFoundException {
		breaksFileBuild(subjectCode);
		if (subjectCode == null) {
			throw new IllegalArgumentException("Subjectcode cannot be \"null\".");
		}
		if(subjectCode.isBlank()) {
			throw new IllegalArgumentException("Subjectcode cannot be empty");
		}
		
		if (!existingSubjectCode(subjectCode.toUpperCase())) {
			throw new IllegalArgumentException("The following subjectcode: " 
					+ subjectCode + " has not been added, please contact an "
					+ "admin.");
		} else {
			subjectCode = subjectCode.strip();			
		}
		
		if (String.valueOf(grade).isBlank()) {
			grade = '0';
		}
		grade = Character.toUpperCase(grade);
		if (!validGrade(grade)) {
			throw new IllegalArgumentException("Grade has to be a char [A, B, "
					+ "C, D, E, F], if the subject was on the from pass/fail, "
					+ "use P for passed.");
		}
		
		EditSubjectFile subjects = new EditSubjectFile();
		HashMap<String, String> subjectMap = subjects.readFile();
		String subjectName = subjectMap.get(subjectCode.toUpperCase());
		ArrayList<String> subject = new ArrayList<String>
				(Arrays.asList(subjectCode.toUpperCase(), subjectName, String.valueOf
											(Character.toUpperCase(grade))));
		this.userSubjects.add(subject);
	}
	
	private boolean validGrade(char grade) {
		Character[] validGradesList = {'A', 'B', 'C', 'D', 'E', 'F', 'P', '0'};
		for (Character compareGrade : validGradesList) {
			if (compareGrade == grade) {
				return true;
			}
		}
		return false;
	}
	
	private boolean existingSubjectCode(String subjectCode) throws FileNotFoundException {
		EditSubjectFile subjectFile = new EditSubjectFile();
		HashMap<String, String> existingSubjects = subjectFile.readFile();
		return existingSubjects.containsKey(subjectCode);
	}
	
	private void breaksFileBuild(String string) {
		if (string != null && !string.isBlank()) {
			if (string.contains(", ") || string.contains(",==") || string.contains(",__") || string.contains("__") || string.contains("==")) {
				throw new IllegalArgumentException("Input cannot contain \",__\", "
						+ "\",==\", \"==\". \"__\".");
			}
		}
	}
}
