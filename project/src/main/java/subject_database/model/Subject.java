package subject_database.model;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Subject {	
	private HashMap<String, String> subjects = new HashMap<String, String>();
	
	/**
	 * Kjører validering av fagkode og fagnavn, dersom det ikke blir kastet noen
	 * feilmeldinger så vil det blit lagt til et HashMap hvor fagkode er key og
	 * fagnavn er value
	 * @param subjectCode
	 * @param subjectName
	 * @throws FileNotFoundException 
	 */
	public void setSubjects(String subjectCode, String subjectName) throws FileNotFoundException {
		if (subjectCode == null || subjectName == null) {
			throw new IllegalArgumentException("Subject code and subject name cannot"
					+ "be \"null\".");
		}
		
		if (subjectCode.isBlank() || subjectName.isBlank()) {
			throw new IllegalArgumentException("Subject code and subject name "
					+ "cannot be blank.");
		}
		breaksFileBuild(subjectCode);
		breaksFileBuild(subjectName);
		String firstInt = "-1"; // setter lik -1 for å kunne kaste feilmld senere
		for (char letter: subjectCode.toCharArray()) {
			if (Character.isDigit(letter)) {
				firstInt = String.valueOf(letter);
				break;
			}
		}
		
		int intIndex = subjectCode.indexOf(firstInt);
		String subjectLetterCode = "";
		String subjectNumberCode = "";
		if (intIndex >= 0) {
			subjectLetterCode = subjectCode.substring(0, intIndex);
			subjectNumberCode = subjectCode.substring(intIndex, subjectCode.length());
			if (!subjectNumberCode.matches("[0-9]+")) {
				throw new IllegalArgumentException("Subjectcode has to end with "
						+ "a four-digit number.");
			}
		} else {
			throw new IllegalArgumentException("Subjectcode must contain a number code.");
		}
		if (subjectNumberCode.length() != 4) {
			throw new IllegalArgumentException("Subjectcode must contain a 4 digit number.");
		}
		if (!validSubjectCode(subjectLetterCode.toUpperCase())) {
			throw new IllegalArgumentException("Subjectcode is not valid.");
		} else {
			subjectCode = subjectCode.toUpperCase();			
		}
		if (!validSubjectName(subjectName)) {
			throw new IllegalArgumentException("Subject name can only contain letters and numbers");
		}
		
		if (registeredSubject(subjectCode)) {
			throw new IllegalArgumentException(subjectCode + " has already been "
					+ "added.");
		}
		
		subjects.put(subjectCode, subjectName);
	}
	

	public HashMap<String, String> getSubjects() {
		return subjects;
	}
	
	@Override
	public String toString() {
		HashMap<String, String> hashMap = getSubjects();
		String output = "";
		for (Map.Entry<String, String> entry : hashMap.entrySet()) {
			String code = entry.getKey();
			String name = entry.getValue();
			output = code + "=" + name + "\n";
		}
		return output;
	}

	//	VALIDERINGSMETODER
	public boolean validSubjectCode(String subjectCode) {
		List<String> validSubjectCodes = new ArrayList<>(Arrays.asList("TDT", 
				"TET", "TFE", "TMA","TTK", "TTM", "TMT", "TTT", "TBA", "TEP", "TGB", 
				"TKT", "TME", "TMM", "TMR", "TPD", "TPG", "TPK", "TVM", "TIØ", 
				"BK", "AAR", "EXPH", "EXFAC", "HFEL", "AFR", "ALIT", "ANT", 
				"ARK", "AVS", "DANS", "DRA", "ENG", "FI", "FFV", "FILM", "FON", 
				"FRA", "GRE", "HIST", "ITA", "KULT", "KRL", "KUH", "LAT", 
				"LING", "MUSP", "MUST", "MUSV", "MVIT", "NFU", "NORD", "RVI",
				"SAM", "SWA", "TYSK", "RFEL", "IT", "MA", "ST", "GEOL", "AK", 
				"BI", "BO", "FY", "KJ", "ZO", "SFEL", "AFR", "FPED", "GEOG", 
				"HLS", "IDR", "MVIT", "PED", "POL", "PPU", "PSY", "PSYPRO", 
				"SARB", "SAM", "SANT", "SANT", "SOS", "SPED", "SØK", "ØKAD", "MD"));
		
		return validSubjectCodes.contains(subjectCode);
	}
	
//	Kilde for regex-kode:
//	https://stackoverflow.com/questions/24744375/regex-for-only-allowing-letters-numbers-space-commas-periods
	public boolean validSubjectName(String subjectName) {
		if (subjectName.matches("^[æøåa-zÆØÅA-Z\\d- ]+$")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Denne valideringsmetoden skal sørge for at bruker-input ikke ødelegger 
	 * tabellstrukturen i alle tekstfilene
	 * @param string
	 */
	private void breaksFileBuild(String string) {
		if (string.contains(", ") || string.contains(",==") || string.contains(",__") || string.contains("__") || string.contains("==")) {
			throw new IllegalArgumentException("Input cannot contain \",__\", "
					+ "\",==\", \"==\". \"__\".");
		}
	}
	
	private boolean registeredSubject(String subjectCode) throws FileNotFoundException {
		EditSubjectFile subjectFile = new EditSubjectFile();
		HashMap<String, String> existingsSubjects = subjectFile.readFile();
		for (Map.Entry<String, String> entry : existingsSubjects.entrySet()) {
			if (subjectCode.toUpperCase().equals(entry.getKey())) {
				return true;
			}
		}
		return false;
	}
}
