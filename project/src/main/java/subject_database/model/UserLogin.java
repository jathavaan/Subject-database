package subject_database.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class UserLogin {
	EditUserFile userInfo = new EditUserFile();
	
	/**
	 * Henter ut brukernavn og passord på bruker ID
	 * @param userId
	 * @return
	 * @throws FileNotFoundException 
	 */
	public String[] getUsernameAndPassword(String userId) throws FileNotFoundException {
		ArrayList<String> allUserInfo = userInfo.getDataFromFile();
		 if (!validUserId(userId)) {
			 throw new IllegalArgumentException("Could not find a match for your "
					 + "user ID.");
		 }			 
		for (int i = 0; i < allUserInfo.size(); i++) {
			String[] userInfo = allUserInfo.get(i).replace("[", "").replace("]", "").split(", ");
			if (userId.equals(userInfo[0])) {
				String[] output = {userInfo[5], userInfo[6], userInfo[8]};
				return output;			
			}
		}
		return null;
	}
	
	/**
	 * Sjekker om oppgitte brukernavn, passord, og bruker ID stemmer med hverandre
	 * Sjekker også om bruker er admin
	 * @param userId
	 * @param username
	 * @param password
	 * @return
	 * @throws FileNotFoundException 
	 */
	public boolean adminUsernameAndPasswordCheck(String userId, String username,
											String password) throws FileNotFoundException {
		if (userId.isBlank() || userId == null) {
			throw new IllegalArgumentException("User ID cannot be empty");
		}
		if (username.isBlank() || username == null) {
			throw new IllegalArgumentException("Username cannot be empty");
		}
		if (password.isBlank() || password == null) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		String[] usernameAndPasswordList = getUsernameAndPassword(userId);
		if (usernameAndPasswordList != null && 
				usernameAndPasswordList[0].equals(username) && 
				usernameAndPasswordList[1].equals(password) &&
				usernameAndPasswordList[2].equals("true")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Sjekker om oppgitte brukernavn, passord, og bruker ID stemmer med hverandre
	 * Sjekker også om bruker ikke er admin
	 * @param userId
	 * @param username
	 * @param password
	 * @return
	 * @throws FileNotFoundException 
	 */
	public boolean usernameAndPasswordCheck(String userId, String username,
			String password) throws FileNotFoundException {
		if (userId.isBlank() || userId == null) {
			throw new IllegalArgumentException("User ID cannot be empty");
		}
		if (username.isBlank() || username == null) {
			throw new IllegalArgumentException("Username cannot be empty");
		}
		if (password.isBlank() || password == null) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		String[] usernameAndPasswordList = getUsernameAndPassword(userId);
		if (usernameAndPasswordList != null && 
			usernameAndPasswordList[0].equals(username) && 
			usernameAndPasswordList[1].equals(password) &&
			usernameAndPasswordList[2].equals("false")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Valideringsmetoder
	 * @throws FileNotFoundException 
	 */
	public boolean validUserId(String userIdString) throws FileNotFoundException {
		ArrayList<String> allUserInfo = userInfo.getDataFromFile();
		try {			
			for (int i = 0; i < allUserInfo.size(); i++) {
				String[] userInfo = allUserInfo.get(i).replace("[", "").replace("]", "").split(", ");
				String compareUserIdString = userInfo[0];
				if (userIdString.equals(compareUserIdString)) {
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
