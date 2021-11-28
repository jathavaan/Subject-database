package subject_database.fxui;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import subject_database.model.EditUserFile;

public class UserTable {
	private int userId;
	private SimpleStringProperty firstname, surname, username, fieldOfStudy;
	private LocalDate dob;
	private boolean admin;
	
	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getFirstname() {
		return firstname.get();
	}

	public void setFirstname(String firstname) {
		this.firstname = new SimpleStringProperty(firstname);
	}

	public String getSurname() {
		return surname.get();
	}

	public void setSurname(String surname) {
		this.surname = new SimpleStringProperty(surname);
	}

	public String getUsername() {
		return username.get();
	}

	public void setUsername(String username) {
		this.username = new SimpleStringProperty(username);
	}

	public String getFieldOfStudy() {
		return fieldOfStudy.get();
	}

	public void setFieldOfStudy(String fieldOfStudy) {
		this.fieldOfStudy = new SimpleStringProperty(fieldOfStudy);
	}

	public LocalDate getDob() {
		return dob;
	}

	public void setDob(LocalDate dob) {
		this.dob = dob;
	}

	public boolean getAdmin() {
		return admin;
	}

	public void setAdmin(boolean admin) {
		this.admin = admin;
	}
	
	public ObservableList<UserTable> getUsers() throws FileNotFoundException {
		EditUserFile arrayList = new EditUserFile();
		ArrayList<String> userInfoList = new ArrayList<String>(arrayList.getDataFromFile());
		ObservableList<UserTable> users = FXCollections.observableArrayList();
		
		for (int i = 0; i < userInfoList.size(); i++) {
			String[] userInfoString = userInfoList.get(i).replace("[", "").replace("]", "").split(", ");
			int userId = Integer.valueOf(userInfoString[0]);
			String username = userInfoString[5];
			String firstname = userInfoString[1];
			String surname = userInfoString[2];
			LocalDate dob = LocalDate.parse(userInfoString[3]);
			String fieldOfStudy = userInfoString[4];
			boolean admin = Boolean.valueOf(userInfoString[8]);
			
			UserTable user = new UserTable();
			user.setUserId(userId);
			user.setUsername(username);
			user.setFirstname(firstname);
			user.setSurname(surname);
			user.setDob(dob);
			user.setFieldOfStudy(fieldOfStudy);
			user.setAdmin(admin);
			
			users.add(user);
		}
		return users;
	}
}