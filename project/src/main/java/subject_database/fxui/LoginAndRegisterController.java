package subject_database.fxui;

import java.io.FileNotFoundException;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import subject_database.model.EditUserFile;
import subject_database.model.User;
import subject_database.model.UserLogin;

public class LoginAndRegisterController {	
	@FXML private TextField logInUsername;
	@FXML private TextField loginUserId;
	@FXML private PasswordField logInPassword;
	@FXML private TextArea logInOutput;
	
	EditUserFile userFile = new EditUserFile();
	private int globalUserId;
	
	/**
	 * Gir programmet muligheten til bruke ID som "nøkkel" til å kunne hente ut
	 * verdier for gitte ID
	 * @param id
	 */
	public void setGlobalUserId(int id) {
		this.globalUserId = id;
	}
	
	/**
	 * Returnerer globalUserId
	 * @return
	 */
	public int getGlobalUserId() {
		return globalUserId;
	}
	
	/**
	 * Når man trykker på logg inn knappen vil denne metoden kjøre
	 * Sjekker om inputparameterene fra brukeren samsvarer med userId, username,
	 * password.
	 * Styrer om brukeren blir sendt til admin side eller brukerside
	 * @param event
	 * @throws IOException
	 */
	@FXML
	public void onUserLogin(ActionEvent event) throws IOException {
		String userId = loginUserId.getText();
		String username = logInUsername.getText();
		String password = logInPassword.getText();
		UserLogin userLogin = new UserLogin();
		try {
			if (userLogin.adminUsernameAndPasswordCheck(userId, username, password)) {
				userFile.writeToUserIdFile(Integer.valueOf(userId));
				
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("adminHomePage.fxml"));
				Parent currentScene = loader.load();
				
				Scene nextScene = new Scene(currentScene);
				
				AdminController adminController = loader.getController();
				adminController.setGlobalUserId(getGlobalUserId());
				
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
				
				window.setScene(nextScene);
				window.setFullScreen(false);
				window.show();					
			} else if (userLogin.usernameAndPasswordCheck(userId, username, password)) {
				userFile.writeToUserIdFile(Integer.valueOf(userId));
				
				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(getClass().getResource("homePage.fxml"));
				Parent currentScene = loader.load();
				
				Scene nextScene = new Scene(currentScene);
				
				
				Stage window = (Stage)((Node)event.getSource()).getScene().getWindow();
				
				window.setScene(nextScene);
				window.setFullScreen(false);
				window.show();
			} else {
				logInOutput.setText("Could not find matching user ID, username and "
						+ "password");
			}
		} catch (FileNotFoundException e) {
			logInOutput.setText(e.getMessage());
		} catch (IllegalArgumentException e) {
			logInOutput.setText(e.getMessage());
		} catch (Exception e) {
			logInOutput.setText("Something went wrong, try again later.");
		}
	}
	
	@FXML private TextField registerFirstname, registerSurname, 
							registerFieldOfStudy, registerUsername;
	@FXML private PasswordField registerPassword;
	@FXML private DatePicker registerDob;
	@FXML private TextArea registerOutput;
	
	/**
	 * Lager en bruker
	 * Alle input parametere blir sjekket gjennom reglene for validering til 
	 * programmet
	 * Skriver ut brukernavn og bruker ID til text area eller evt. hva som er 
	 * invalid med det brukeren skriver inn
	 */
	@FXML
	public void onUserRegister() {
		try {
			User user = new User();
			
			user.setUserId(user.generateUserId());
			user.setFirstname(registerFirstname.getText());
			user.setSurname(registerSurname.getText());
			user.setDob(registerDob.getValue());
			user.setFieldOfStudy(registerFieldOfStudy.getText());
			user.setUsername(registerUsername.getText());
			user.setPassword(registerPassword.getText());	
			user.setAdmin(false);
			
			userFile.addToFile(user);
			registerOutput.setText("Your username is: " + user.getUsername() +
					"\nYour user-ID is: " + String.valueOf(
							user.getUserId()));
		} catch (IllegalArgumentException e) {
			registerOutput.setText(e.getMessage());
		} catch (IllegalStateException e) {
			registerOutput.setText(e.getMessage());
		} catch (FileNotFoundException e) {
			registerOutput.setText(e.getMessage());
		} catch (Exception e) {
			registerOutput.setText("Something went wrong, try again later");
		}
	}
}
