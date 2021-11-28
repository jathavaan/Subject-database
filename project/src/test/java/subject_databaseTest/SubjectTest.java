package subject_databaseTest;

import java.io.FileNotFoundException;
import java.util.HashMap;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import subject_database.model.Subject;

public class SubjectTest {
	Subject subject = new Subject();
	
	@BeforeEach
	public void setup() {
		subject = new Subject();
	}
	
	/**
	 * Tester setSubjects med korrekt instans av alt (skal ikke kaste feilmelding
	 */
	
	@Test
	@DisplayName("Test med korrekt instans av subject code og subject name")
	public void testSetSubjects() {
		try {
			subject.setSubjects("TDT0000", "Test for fag");
			Assertions.assertTrue(true);
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Tester om IAE kastes dersom subjectcode og subjectname"
			+ " er blank eller \"null\".")
	public void testSubjectNullAndBlank() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects(null, "OOP"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100", null));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("", "OOP"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100", ""));
	}
	
	@Test
	@DisplayName("Tester om invalide strenger blir fanget opp for både subject "
			+ "code og subject name")
	public void testInvalidString() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100,==", "Objektorientert programmering"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100,__", "Objektorientert programmering"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100==", "Objektorientert programmering"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100__", "Objektorientert programmering"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100", "Objektorientert,==programmering"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100", "Objektorientert,__programmering"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100", "Objektorientert==programmering"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100", "Objektorientert__programmering"));
		
		try {
			subject.setSubjects("TDT0000", "Objektorientert - programmering");
			Assertions.assertTrue(true);
		} catch (Exception e) {
			Assertions.assertTrue(false);
		}
	}
	
	@Test
	@DisplayName("Tester om subjectCode kaster IAE når den ikke har en tallkode")
	public void testNoSubjectCodeNumber() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT", "OOP"));
	}
	
	@Test
	@DisplayName("Tester om det kastes IAE dersom tallkoden er mindre enn 4 siffer")
	public void testNumberCodeLessThanFourNumbers() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT41", "OOP"));
	}
	
	@Test
	@DisplayName("Tester om en ikke-eksisterende bokstavkode kaster feilmld")
	public void testInvalidSubjectLetterCode() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TQE4110", "OOP"));
	}
	
	@Test
	@DisplayName("Tester om subject code blir gjort om til store bokstaver "
			+ "dersom det skrives inn med små bokstaver")
	public void testConvertSubjectCodeToUpperCase() throws FileNotFoundException {
		HashMap<String, String> map = new HashMap<String, String>();
		map.put("TDT0000", "testfag");
		subject.setSubjects("tdt0000", "testfag");
		Assertions.assertEquals(map, subject.getSubjects());
	}
	
	@Test
	@DisplayName("Tester om programmet kaster feil dersom det kommer bokstaver "
			+ "etter tallkoden")
	public void testLetterAfterLetterCode() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100T", "OOP"));
	}
	
	@Test
	@DisplayName("Tester om det blir kastet feil dersom faget allerede er registrert")
	public void testAddExistingSubject() {
		Assertions.assertThrows(IllegalArgumentException.class, () -> subject.setSubjects("TDT4100", "OOP"));
	}
}
