package subject_database.fxui;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
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
import subject_database.model.AverageGradeCalculator;
import subject_database.model.EditUserFile;
import javafx.fxml.Initializable;

public class UserController implements Initializable {
	EditUserFile userFile = new EditUserFile();
	AverageGradeCalculator averageGrade = new AverageGradeCalculator();
	
	@FXML private TableView<UserSubjectsTable> userSubjectsTable = new TableView<UserSubjectsTable>();
	@FXML private TableColumn<UserSubjectsTable, String> subjectNameColumn = new TableColumn<UserSubjectsTable, String>();
	@FXML private TableColumn<UserSubjectsTable, String> subjectCodeColumn = new TableColumn<UserSubjectsTable, String>();
	@FXML private TableColumn<UserSubjectsTable, String> gradeColumn = new TableColumn<UserSubjectsTable, String>();
	@FXML private Label nameLabel, dobLabel, 
						fieldOfStudyLabel, usernameLabel, averageGradeLabel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		try {
			ArrayList<String> userList = userFile.getUserOnId(userFile.getLastId());
			
			nameLabel.setText(userList.get(1) + " " + userList.get(2));
			dobLabel.setText(userList.get(3));
			fieldOfStudyLabel.setText(userList.get(4));
			usernameLabel.setText(userList.get(5) + " | " + userList.get(0));
			
			averageGrade.setGrade(userFile.getLastId());
			averageGradeLabel.setText("Average grade:\t" + averageGrade.getGrade());
			
			subjectCodeColumn.setCellValueFactory(new PropertyValueFactory<UserSubjectsTable, String>("userSubjectCode"));
			subjectNameColumn.setCellValueFactory(new PropertyValueFactory<UserSubjectsTable, String>("userSubjectName"));
			gradeColumn.setCellValueFactory(new PropertyValueFactory<UserSubjectsTable, String>("userGrade"));
			
			UserSubjectsTable userSubjectsTable = new UserSubjectsTable();
			this.userSubjectsTable.setItems(userSubjectsTable.getUserSubjects(userFile.getLastId()));			
		} catch (FileNotFoundException e) {
			userOutput.setText(e.getMessage());
		} catch (Exception e) {
			userOutput.setText("Something went wrong, try again later.");
		}
	}
	
	@FXML private TextField userAddSubjectCode;
	@FXML private TextField userAddSubjectGrade;
	@FXML private TextArea userOutput;
	
	/**
	 * Lar bruker legge til fag til profilen sin
	 */
	public void onUserAddSubject() {
		try {
			String subjectCode = userAddSubjectCode.getText();
			String grade = userAddSubjectGrade.getText();
			
			userFile.addUserSubjects(userFile.getLastId(), subjectCode, grade);
			initialize(null, null);
			userOutput.setText("Added " + subjectCode.toUpperCase() + " to your subject list.");
		} catch (IllegalArgumentException e) {
			userOutput.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			userOutput.setText(e.getMessage());
		} catch (Exception e) {
			userOutput.setText("Something went wrong, try again later.");
		}
	}
	
	public void onUserEditSubject() {
		try {
			String subjectCode = userAddSubjectCode.getText();
			String grade = userAddSubjectGrade.getText();
			
			userFile.editUserSubject(userFile.getLastId(), subjectCode, grade);
			initialize(null, null);
			userOutput.setText("Changed grade of " + subjectCode.toUpperCase());
		} catch (IllegalArgumentException e) {
			userOutput.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			userOutput.setText(e.getMessage());
		} catch (Exception e) {
			userOutput.setText("Something went wrong, try again later.");
		}
	}
	
	public void onUserDeleteSubject() {
		try {
			String subjectCode = userAddSubjectCode.getText();
			
			userFile.deleteUserSubject(userFile.getLastId(), subjectCode);
			initialize(null, null);
			userOutput.setText("Deleted " + subjectCode.toUpperCase() + ".");
		} catch (IllegalArgumentException e) {
			userOutput.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			userOutput.setText(e.getMessage());
		} catch (Exception e) {
			userOutput.setText("Something went wrong, try again later.");
		}
	}
	
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
