package subject_databaseTest;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subject_database.model.EditUserFile;

public class EditUserFileTest {
	EditUserFile userFile = new EditUserFile();
	
	@Test
	@DisplayName("Legger til fag")
	public void testAddUserSubjects() throws FileNotFoundException {
		userFile.deleteUserSubject(61, "TDT4100");
		try {
			userFile.addUserSubjects(61, "TDT4100", "A");
			Assertions.assertTrue(true);
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Legger til fag som allerede finnes")
	public void testExistingSubject() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> userFile.addUserSubjects(61, "TDT4100", "B"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> userFile.addUserSubjects(61, "tdt4100", ""));
	}
	
	@Test
	@DisplayName("Sletter fag")
	public void testDeleteSubject() {
		try {
			userFile.deleteUserSubject(61, "TDT4100");
			userFile.addUserSubjects(61, "TDT4100", "");
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Sletter et fag som ikke finnes")
	public void deleteNonExistingSubject() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> userFile.deleteUserSubject(61, "TDT0000"));
	}
	
	@Test
	@DisplayName("Redigerer et fag")
	public void testEditSubject() {
		try {
			userFile.editUserSubject(61, "TDT4100", "C");
			Assertions.assertTrue(true);
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Rediger ikke eksisterende fag")
	public void testEditNonExistingSubject() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> userFile.editUserSubject(61, "TDT0000", "A"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> userFile.editUserSubject(61, "tdt000", "a"));
	}
}
