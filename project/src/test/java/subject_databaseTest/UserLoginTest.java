package subject_databaseTest;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subject_database.model.UserLogin;

public class UserLoginTest {
	UserLogin userLogin = new UserLogin();
	
	@Test
	@DisplayName("Tester om det blir hentet ut riktig bruker info")
	public void testGetUsernameAndPassword() throws FileNotFoundException {
		String[] correctOutput = {"admin", "admin123", "true"};
		String[] actualOutput = userLogin.getUsernameAndPassword("0");
		
		for (int i = 0; i < correctOutput.length; i++) {
			Assertions.assertEquals(correctOutput[i], actualOutput[i]);
		}
	}
	
	@Test
	@DisplayName("Tester om det brukeren får tilgang med rett og feil "
			+ "brukernavn og passord; admin")
	public void testAdminUsernameAndPasswordCheck() throws FileNotFoundException {
		Assertions.assertFalse(userLogin.adminUsernameAndPasswordCheck("0", "admin", "wrongPassword"));
		Assertions.assertFalse(userLogin.adminUsernameAndPasswordCheck("0", "wrongUsername", "admin123"));
		Assertions.assertFalse(userLogin.adminUsernameAndPasswordCheck("1", "admin", "admin123"));
		
		Assertions.assertFalse(userLogin.adminUsernameAndPasswordCheck("1", "notAdmin", "notAdmin123"));
		
		Assertions.assertTrue(userLogin.adminUsernameAndPasswordCheck("0", "admin", "admin123"));
	}
	
	@Test
	public void testUsernameAndPasswordCheck() throws FileNotFoundException {
		Assertions.assertFalse(userLogin.usernameAndPasswordCheck("1", "wrongUsername", "notAdmin123"));
		Assertions.assertFalse(userLogin.usernameAndPasswordCheck("1", "notAdmin", "wrongPassword"));
		Assertions.assertFalse(userLogin.usernameAndPasswordCheck("0", "notAdmin", "notAdmin123"));
		
		Assertions.assertFalse(userLogin.usernameAndPasswordCheck("0", "admin", "admin123"));
		
		Assertions.assertTrue(userLogin.usernameAndPasswordCheck("1", "notAdmin", "notAdmin123"));
	}
}
