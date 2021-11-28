package subject_database.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AverageGradeCalculator {
	private double grade;
	
	/**
	 * Returnerer aktuell karakter
	 * Følger GPA systemet for karakter
	 * @return
	 */
	public String getGrade() {
		double grade = this.grade;
		if (0 < grade && grade < 0.5) {
			return "E";
		} else if (0.5 <= grade && grade < 1.5) {
			return "E";
		} else if (1.5 <= grade && grade < 2.5) {
			return "D";
		} else if (2.5 <= grade && grade < 3.5) {
			return "C";
		} else if (3.5 <= grade && grade < 4.5) {
			return "B";
		} else if (4.5 <= grade && grade <= 5) {
			return "A";
		}
		return "N/A";
	}

	/**
	 * 
	 * @param userId
	 * @throws FileNotFoundException 
	 */
	public void setGrade(int userId) throws FileNotFoundException {
		EditUserFile file = new EditUserFile();
		ArrayList<ArrayList<String>> subjectTable = file.userSubjectTable(userId);
		if (subjectTable.size() == 0) {
			this.grade = 0;
		} else {
			ArrayList<Integer> gradeNums = new ArrayList<Integer>();
			ArrayList<Integer> passedGrade = new ArrayList<Integer>();
			for (ArrayList<String> subject : subjectTable) {
				String grade = subject.get(2);
				if (grade.equals("A")) {
					gradeNums.add(5);
				} else if (grade.equals("B")) {
					gradeNums.add(4);
				} else if (grade.equals("C")) {
					gradeNums.add(3);
				} else if (grade.equals("D")) {
					gradeNums.add(2);
				} else if (grade.equals("E")) {
					gradeNums.add(1);
				} else if (grade.equals("P")) {
					passedGrade.add(0);
				}
			}
			int gradeSum = 0;
			if (passedGrade.size() == subjectTable.size()) {
				this.grade = 0;
			} else {
				for (int grade : gradeNums) {
					gradeSum += grade;
				}
				this.grade = (double) gradeSum / (double) gradeNums.size();		
			}
		}
	}
}
