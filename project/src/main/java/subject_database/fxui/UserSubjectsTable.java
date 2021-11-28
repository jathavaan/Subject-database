package subject_database.fxui;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import subject_database.model.EditUserFile;

public class UserSubjectsTable {
	private SimpleStringProperty userSubjectCode, userSubjectName, userGrade;
	
	public String getUserSubjectCode() {
		return userSubjectCode.get();
	}
	
	public void setUserSubjectCode(String subjectCode) {
		this.userSubjectCode = new SimpleStringProperty(subjectCode);
	}
	
	public String getUserSubjectName() {
		return userSubjectName.get();
	}
	
	public void setUserSubjectName(String subjectName) {
		this.userSubjectName = new SimpleStringProperty(subjectName);
	}
	
	public String getUserGrade() {
		return userGrade.get();
	}
	
	public void setUserGrade(String userGrade) {
		this.userGrade = new SimpleStringProperty(userGrade);
	}
	
	public ObservableList<UserSubjectsTable> getUserSubjects(int userId) throws FileNotFoundException {
		EditUserFile user = new EditUserFile();
		ArrayList<String> userList = user.getUserOnId(userId);
		ArrayList<ArrayList<String>> userSubjectsTable = new ArrayList<ArrayList<String>>();
		ObservableList<UserSubjectsTable> userSubjects = FXCollections.observableArrayList();
		if (user.hasSubjects(userList)) {			
			String[] userSubjectsStringList = userList.get(7).split(",__");
			for (String subject : userSubjectsStringList) {
				subject = subject.replace("[", "").replace("]", "");
				ArrayList<String> subjectInfoList = new ArrayList<String>(Arrays.asList(subject.split(",==")));
				userSubjectsTable.add(subjectInfoList);
			}
			for (ArrayList<String> subject : userSubjectsTable) {
				if (subject.size() != 0) {				
					UserSubjectsTable finalTable = new UserSubjectsTable();
					
					finalTable.setUserSubjectCode(subject.get(0).replace("==", ""));
					finalTable.setUserSubjectName(subject.get(1));
					finalTable.setUserGrade(subject.get(2));
					
					userSubjects.add(finalTable);
				}
			}
		}
		return userSubjects;
	}
}
