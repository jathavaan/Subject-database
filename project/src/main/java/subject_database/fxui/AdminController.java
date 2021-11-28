package subject_database.fxui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import subject_database.model.EditSubjectFile;
import subject_database.model.EditUserFile;
import subject_database.model.Subject;
import javafx.fxml.Initializable;

public class AdminController implements Initializable {
	private int globalUserId;
	
	private EditUserFile userFile = new EditUserFile();
	private EditSubjectFile subjectFile = new EditSubjectFile();
	private UserTable userTable = new UserTable();
	private SubjectTable subjectTable = new SubjectTable();
	
	@FXML private TextField userIdForAdminEdit, adminAddSubjectCode, 
							adminAddSubjectName, adminRemoveSubject;	
	@FXML private TextArea adminEditsUser, adminSubjectOutput;
	@FXML private TableView<UserTable> allUsersTable = new TableView<UserTable>();
	@FXML private TableColumn<UserTable, Integer> userIdColumn = new TableColumn<UserTable, Integer>();
	@FXML private TableColumn<UserTable, String> usernameColumn = new TableColumn<UserTable, String>();
	@FXML private TableColumn<UserTable, String> firstnameColumn = new TableColumn<UserTable, String>();
	@FXML private TableColumn<UserTable, String> surnameColumn = new TableColumn<UserTable, String>();
	@FXML private TableColumn<UserTable, LocalDate> dobColumn = new TableColumn<UserTable, LocalDate>();
	@FXML private TableColumn<UserTable, String> fieldOfStudyColumn = new TableColumn<UserTable, String>();
	@FXML private TableColumn<UserTable, Boolean> adminColumn = new TableColumn<UserTable, Boolean>();
	
	@FXML private TableView<SubjectTable> allSubjectsTable = new TableView<SubjectTable>();
	@FXML private TableColumn<SubjectTable, String> adminSubjectCodeColumn = new TableColumn<SubjectTable, String>();
	@FXML private TableColumn<SubjectTable, String> adminSubjectNameColumn = new TableColumn<SubjectTable, String>();
	
	@FXML private Label nameLabel, dobLabel, usernameLabel, fieldOfStudyLabel;
	
	public int getGlobalUserId() {
		return globalUserId;
	}

	public void setGlobalUserId(int userId) {
		this.globalUserId = userId;
	}

	/**
	* Initialiserer alle tabeller som brukes i appen
	*/
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {			
			ArrayList<String> userList = userFile.getUserOnId(userFile.getLastId());
			
			nameLabel.setText(userList.get(1) + " " + userList.get(2));
			dobLabel.setText(userList.get(3));
			fieldOfStudyLabel.setText(userList.get(4));
			usernameLabel.setText(userList.get(5) + " | " + userList.get(0));
			
			// Tabell over alle brukere
			userIdColumn.setCellValueFactory(new PropertyValueFactory<UserTable, Integer>("userId"));
			usernameColumn.setCellValueFactory(new PropertyValueFactory<UserTable, String>("username"));
			firstnameColumn.setCellValueFactory(new PropertyValueFactory<UserTable, String>("firstname"));
			surnameColumn.setCellValueFactory(new PropertyValueFactory<UserTable, String>("surname"));
			dobColumn.setCellValueFactory(new PropertyValueFactory<UserTable, LocalDate>("dob"));
			fieldOfStudyColumn.setCellValueFactory(new PropertyValueFactory<UserTable, String>("fieldOfStudy"));
			adminColumn.setCellValueFactory(new PropertyValueFactory<UserTable, Boolean>("admin"));
			
			this.allUsersTable.setItems(userTable.getUsers());
			
			// Tabell over alle fag
			adminSubjectCodeColumn.setCellValueFactory(new PropertyValueFactory<SubjectTable, String>("tableSubjectCode"));
			adminSubjectNameColumn.setCellValueFactory(new PropertyValueFactory<SubjectTable, String>("tableSubjectName"));
			
			this.allSubjectsTable.setItems(subjectTable.getSubjects());
		} catch (FileNotFoundException e) {
			adminSubjectOutput.setText(e.getMessage());
			adminEditsUser.setText(e.getMessage());
		} catch (Exception e) {
			adminSubjectOutput.setText("Something went wrong, try again later.");
			adminEditsUser.setText("Something went wrong, try again later.");
		}
	}
	
	//Subject tab
	/**
	* Lar admin legge til fag som brukere kan velge
	*/
	@FXML
	public void onAdminAddSubject() {
		String subjectCode = adminAddSubjectCode.getText().strip();
		String subjectName = adminAddSubjectName.getText().strip();
		Subject subject = new Subject();
		try {
			subject.setSubjects(subjectCode, subjectName);
			subjectFile.addToFile(subject);
			adminSubjectOutput.setText("Added " + subjectCode.toUpperCase() + " " + subjectName + ".");
			initialize(null, null);
		} catch (IllegalArgumentException e) {
			adminSubjectOutput.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			adminSubjectOutput.setText(e.getMessage());
		} catch (Exception e) {
			adminSubjectOutput.setText("Something went wrong, try again later");
		}
	}
	
	/**
	* Lar admin fjerne fag bruker kan velge
	*/
	@FXML
	public void onAdminDeleteSubject() {
		try {
			String subjectCode = adminRemoveSubject.getText();
			EditSubjectFile deleteSubject = new EditSubjectFile();
			deleteSubject.deleteSubject(subjectCode);
			adminSubjectOutput.setText("Deleted " + subjectCode.toUpperCase() 
										+ " from database.");
			initialize(null, null);
		} catch (IllegalArgumentException e) {
			adminSubjectOutput.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			adminSubjectOutput.setText(e.getMessage());
		} catch (Exception e) {
			adminSubjectOutput.setText("Something went wrong, try again later");
		}
	}
	
	//User tab
	/**
	* Lar en admin sette en annen bruker til admin
	*/
	@FXML
	public void onSetAdmin() {
		try {			
			EditUserFile setAdmin = new EditUserFile();
			String userId = userIdForAdminEdit.getText();
			setAdmin.editAdmin(userId, "true");
			adminEditsUser.setText("User " + userId + " has been granted admin "
			+ "permissions.");
			initialize(null, null);
		} catch (IllegalArgumentException e) {
			adminEditsUser.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			adminEditsUser.setText(e.getMessage());
		} catch (Exception e) {
			adminEditsUser.setText("Something went wrong, try again later");
		}
	}
	
	/**
	* Lar admin fjerne en annen bruker som admin
	*/
	@FXML
	public void onRemoveAdmin() {
		try {			
			EditUserFile removeAdmin = new EditUserFile();
			String userId = userIdForAdminEdit.getText();
			removeAdmin.editAdmin(userId, "false");
			adminEditsUser.setText("User " + userId + " has lost admin rights.");
			initialize(null, null);
		} catch (IllegalArgumentException e) {
			adminEditsUser.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			adminEditsUser.setText(e.getMessage());
		} catch (Exception e) {
			adminEditsUser.setText("Something went wrong, try again later");
		}
	}
	
	/**
	* Lar admin slette en bruker fra systemet
	*/
	@FXML
	public void onAdminDeleteUser() {
		try {			
			EditUserFile file = new EditUserFile();
			file.deleteUser(userIdForAdminEdit.getText());
			adminEditsUser.setText("Deleted user " + userIdForAdminEdit.getText());
			initialize(null, null);
		} catch (IllegalArgumentException e) {
			adminEditsUser.setText(e.getMessage());
		} catch (Exception e) {
			adminEditsUser.setText("Something went wrong, try again later");
		}
	}
	
	/**
	 * 
	 * @param event
	 * @throws IOException
	 */
	public void onLogOut(ActionEvent event) throws IOException {
		Parent currentScene = FXMLLoader.load(getClass()
				.getResource("loginAndRegister.fxml"));
		Scene nextScene = new Scene(currentScene);
		
		Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
		
		window.setScene(nextScene);
		window.setFullScreen(false);
		window.show();
	}
}
