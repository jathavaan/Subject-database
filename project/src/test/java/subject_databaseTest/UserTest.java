package subject_databaseTest;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.ArrayList;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subject_database.model.User;

public class UserTest {
	
	User user = new User();
	
	@BeforeEach
	public void setup() {
		user = new User();
	}
	
	/**
	 * Tester generelle tilfeller (ting som flere metoder har til felles)
	 * @throws FileNotFoundException 
	 */
	
	@Test
	@DisplayName("Korrekt instans av et objekt ikke kaster feil, tester også om "
			+ "getter-metodene fungerer")
	public void testUserCreation() throws FileNotFoundException {
		user.setUserId(999);
		user.setFirstname("Jathavaan");
		user.setSurname("Shankarr");
		user.setDob(LocalDate.parse("2001-07-12"));
		user.setUsername("jathavaan12");
		user.setPassword("Jathavaan12");
		user.setUserSubjects("TDT4100", 'A');
		user.setAdmin(false);
		
		ArrayList<ArrayList<String>> subjects = new ArrayList<ArrayList<String>>();
		ArrayList<String> subject = new ArrayList<String>(Arrays.asList("TDT4100", "Objektorientert programmering", "A"));
		subjects.add(subject);
		
		Assertions.assertEquals(999, user.getUserId());
		Assertions.assertEquals("Jathavaan", user.getFirstname());
		Assertions.assertEquals("Shankarr", user.getSurname());
		Assertions.assertEquals(LocalDate.parse("2001-07-12"), user.getDob());
		Assertions.assertEquals("jathavaan12", user.getUsername());
		Assertions.assertEquals("Jathavaan12", user.getPassword());
		Assertions.assertEquals(subjects, user.getUserSubjects());
		Assertions.assertTrue(!user.getAdmin());
	}
	
	@Test
	@DisplayName("Sjekker om programmet kaster feilmelding ved input som vil "
			+ "ødelegge filstrukturen, og ikke kaste når den får gyldig input")
	public void testFileBuild() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFirstname(",=="));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFirstname(",=="));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setSurname(",__"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("=="));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFieldOfStudy("__"));
		try {
			user.setPassword("Jathavaan12");
			Assertions.assertTrue(true);
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}

	}
	
	/**
	 * Tester mer spesifikke tilfeller
	 */
	
	/**
	 * Tester for setUserId
	 */
	
	@Test
	@DisplayName("Tester om IEA kastes når bruker ID er mindre enn 0")
	public void testNegativeUserId() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUserId(-4));
	}
	
	@Test
	@DisplayName("Tester om det genereres bruker ID")
	public void testUniqueGeneratedUserId() {
		try {
			user.generateUserId();
		} catch (Exception e) {
			Assertions.assertFalse(true);
		}
	}
	
	/**
	 * Tester for setFirstname
	 */
	
	@Test
	@DisplayName("Tester om IAE kastes når firstname er \"null\" eller blank")
	public void testFirstnameNullAndBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFirstname(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFirstname(""));
	}
	
	@Test
	@DisplayName("Tester om fornavn (og etternavn) kaster IAE dersom det ikke "
			+ "er et gyldig navn")
	public void testName() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFirstname("j"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFirstname("Jathavaan-S"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setFirstname("J-Shankarr"));
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setSurname("j"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setSurname("Jathavaan-S"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setSurname("J-Shankarr"));
	}
	
	/**
	 * Tester for setSurname
	 */
	
	@Test
	@DisplayName("Tester om IAE og IAE kastes når surname er \"null\" eller blank")
	public void testSurnameNullAndBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setSurname(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setSurname(""));
	}
	
	/**
	 * Tester for setDob
	 */
	
	@Test
	@DisplayName("Tester om IAE og ISE kastes når dob er \"null\" eller blank")
	public void testDobNullAndBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setDob(null));
	}
	
	@Test
	@DisplayName("Tester om ISE kastes dersom dob er i framtiden")
	public void testDobInTheFuture() {
		Assertions.assertThrows(IllegalStateException.class, () -> user.setDob(LocalDate.parse("3001-07-12")));
	}
	
	/**
	 * Tester for setUsername
	 */
	
	@Test
	@DisplayName("Tester om æøå fungerer")
	public void testNorwegianLetters() {
		try {
			user.setUsername("Jathavån12");
			Assertions.assertTrue(true);
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Tester om IAE kastes dersom brukernavn inneholder andre tegn "
			+ "enn alfanumeriske tegn")
	public void testNotOnlyAlphanumericCharacters() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("Jathavaan12?"));
	}
	
	@Test
	@DisplayName("Tester om brukernavn kan inneholde \"-\", \"_\", \".\"")
	public void testNonAlphanumericalValues() {
		try {
			user.setUsername("Ja-th_av.aan");
			Assertions.assertTrue(true);
		} catch (IllegalArgumentException e) {
			Assertions.assertTrue(false);
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Tester om det kastes IAE dersom \"-\", \"_\", \".\" oppstår "
			+ "etter hverandre")
	public void testConsecutiveNonAlphanumericalCharacters() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("Jatha__vaan"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("Jatha--vaan"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("Jatha..vaan"));
	}
	
	@Test
	@DisplayName("Tester om IAE kastes dersom \"-\", \"_\", \".\" befinner seg på starten")
	public void testNonAlphanumericalCharactersAtStartAndEnd() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername(".Jathavaan"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("-Jathavaan"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("_Jathavaan"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("Jathavaan."));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("Jathavaan-"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("Jathavaan_"));
	}
	
	@Test
	@DisplayName("Tester om det kastes IAE dersom brukernavn er for kort eller "
			+ "for langt")
	public void testUsernameLength() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("ja12"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUsername("jathavaan1235678901212"));
	}
	
	/**
	 * Tester for setPassword
	 */
	
	@Test
	@DisplayName("Tester om IAE kastes dersom passord er \"null\" eller blank")
	public void testPasswordNullAndBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setPassword(null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setPassword(""));
	}
	
	@Test
	@DisplayName("Tester om IAE kastes dersom passord er for kort")
	public void testPasswordLength() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setPassword("a1"));
	}
	
	@Test
	@DisplayName("Tester om IAE kastes dersom passord er ikke inneholder minst "
			+ "ett tall og en bokstav")
	public void testPasswordContainsAtleastOneLetterAndNumber() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setPassword("1234567890"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setPassword("abcdefghijklmno"));
	}
	
	/**
	 * Tester for setUserSubjects
	 */
	
	@Test
	@DisplayName("Tester om IAE kastes dersom subjectCode er \"null\" "
			+ "eller blank")
	public void testSubjectCodeNullAndBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUserSubjects(null, 'A'));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUserSubjects("", 'A'));
	}
	
	@Test
	@DisplayName("Tester om det kastes IAE dersom bruker prøver å legge til et "
			+ "fag som ikke er lagt til")
	public void testAddNonExistingSubject() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUserSubjects("TDT4009", '0'));
	}
	
	@Test
	@DisplayName("Tester om alle hendelser av grade (både små og store "
			+ "bokstaver) har korrekt oppførsel")
	public void testGrade() {
		try {
			user.setUserSubjects("TDT4100", 'A');
			user.setUserSubjects("TDT4100", 'B');
			user.setUserSubjects("TDT4100", 'C');
			user.setUserSubjects("TDT4100", 'D');
			user.setUserSubjects("TDT4100", 'E');
			user.setUserSubjects("TDT4100", 'F');
			user.setUserSubjects("TDT4100", '0');
			user.setUserSubjects("TDT4100", 'P');
			
			user.setUserSubjects("TDT4100", 'a');
			user.setUserSubjects("TDT4100", 'b');
			user.setUserSubjects("TDT4100", 'c');
			user.setUserSubjects("TDT4100", 'd');
			user.setUserSubjects("TDT4100", 'e');
			user.setUserSubjects("TDT4100", 'f');
			user.setUserSubjects("TDT4100", 'p');
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Tester om det kastes IAE dersom en ugyldig grade legges inn")
	public void testInvalidGrade() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUserSubjects("TDT4100", 'q'));
		Assertions.assertThrows(IllegalArgumentException.class, () -> user.setUserSubjects("TDT4100", 'Q'));
	}
}
