package subject_database.model;

import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class User extends UserSubjects {
	/**
	 * Denne klassen har ansvar for validering av bruker
	 */
	
	private int userId = 0;
	private String firstname, surname, fieldOfStudy, username, password;
	private LocalDate dob;
	
	private boolean admin;
	
	Subject subject = new Subject();
	
	public int getUserId() {
		return userId;
	}
	
	/**
	 setUserId() setter bruker ID, ID-en skal være unik og et heltall i intervallet [0, inf>
	 * Har følgende krav:
	 * Ikke null eller blank
	 * Et heltall
	 * Større eller lik 0
	 * Unik ID som sjekkes opp mot users.txt filen
	 * @param userId
	 */
	public void setUserId(int userId) {
		if (!validUserId(userId)) {
			throw new IllegalArgumentException("User-ID has to be an integer "
					+ "equal to or greater than 0.");
		}
		this.userId = userId;
	}
	
	public int generateUserId() throws FileNotFoundException {
		EditUserFile userInfo  = new EditUserFile();
		ArrayList<String> allUserInfo = userInfo.getDataFromFile();
		String[] userInfoList = null;
		for (int i = 0; i < allUserInfo.size(); i++) {
			userInfoList = allUserInfo.get(i).replace("[", "").replace("]", "").split(", ");
		}
		String userIdString = userInfoList[0];
		int userId = Integer.parseInt(userIdString);
		userId ++;
		if (!uniqueUserId(Integer.valueOf(userId))) {
			throw new IllegalArgumentException("The generated user ID allready "
					+ "exists.");
		}
		return userId;
	}

	public String getFirstname() {
		return firstname;
	}
	
	/**
	 * setFirstname og setSurname setter fornavnet til brukeren
	 * Har følgende krav:
	 * Ikke null eller blank
	 * Kan være adskilt med "-" og hvert navn på hver side av "-" må være minst 2 bokstaver langt
	 * Må uansett være minst to bokstaver langt
	 * @param firstname
	 * @param surname
	 */
	public void setFirstname(String firstname) {
		breaksFileBuild(firstname);
		if (firstname == null) {
			throw new IllegalArgumentException("Firstname cannot be null");
		}
		if (firstname.isBlank()) {
			throw new IllegalArgumentException("Firstname cannot be empty");
		} else {
			firstname = firstname.strip();			
		}
		if (!validName(firstname)) {
			throw new IllegalArgumentException("Firstname and surname has to be"
					+ " atleast 2 letters long. If you name contains \"-\" "
					+ "the name before and after \"-\" has to be longer than "
					+ "2 letters each.");
		}
		this.firstname = firstname;
	}

	public String getSurname() {
		return surname;
	}
	
	public void setSurname(String surname) {
		breaksFileBuild(surname);
		if (surname == null) {
			throw new IllegalArgumentException("Surname cannot be null");
		}
		if (surname.isBlank()) {
			throw new IllegalArgumentException("Surname cannot be empty");
		} else {
			surname = surname.strip();			
		}
		if (!validName(surname)) {
			throw new IllegalArgumentException("Firstname and surname has to be"
					+ " atleast 2 letters long. If you name contains \"-\" "
					+ "the name before and after \"-\" has to be longer than "
					+ "2 letters each.");
		}
		this.surname = surname;
	}

	public LocalDate getDob() {
		return dob;
	}
	
	/**
	 * setDob setter fødselsdatoen til brukeren
	 * Har følgende krav:
	 * Ikke null og blank
	 * Fødselsdato kan ikke være satt fram i tid
	 * @param dob
	 */
	public void setDob(LocalDate dob) {
		if (dob == null) {
			throw new IllegalArgumentException("Date of birth cannot be empty");
		}
		String stringDob = dob.toString();
		DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
		dob = LocalDate.parse(stringDob, dateFormatter);
		if (dateAfterToday(dob)) {
			throw new IllegalStateException("Date of birth cannot be in the "
					+ "future");
		}
		this.dob = dob;
	}

	public String getFieldOfStudy() {
		return fieldOfStudy;
	}
	
	/**
	 * setFieldOfStudy() setter studielinjen til brukeren
	 * Har følgende krav
	 * Kan ikke være null eller blank
	 * Kun tekst, med bindestrek
	 * @param fieldOfStudy
	 */
	public void setFieldOfStudy(String fieldOfStudy) {
		breaksFileBuild(fieldOfStudy);
		if (fieldOfStudy == null || fieldOfStudy.isBlank()) {
			throw new IllegalArgumentException("Field of study cannot be empty");
		} else {
			fieldOfStudy = fieldOfStudy.strip();			
		}
		if (!validFieldOfStudy(fieldOfStudy.trim())) {
			throw new IllegalArgumentException("Field of study can only contain"
					+ " letters.");
		}
		this.fieldOfStudy = fieldOfStudy;
	}
	
	public String getUsername() {
		return username;
	}
	
	/**
	 * setUsername setter brukernavnet til brukeren
	 * Har følgende krav:
	 * Må bestå av tall eller bokstaver
	 * Store og/eller små bokstaver
	 * Kan ikke ha ".", "_", "-" på begynnelsen eller slutten, og ikke komme etter hverandre
	 * Mellom 5 og 20 bokstaver
	 * @param username
	 */
	public void setUsername(String username) {
		breaksFileBuild(username);
		if (username == null || username.isBlank()) {
			throw new IllegalArgumentException("Username cannot be empty");
		} else {
			username = username.strip();
		}
		if (!validUsername(username.trim())) {
			throw new IllegalArgumentException("Username must fulfill: "
					+ "\nConsist of alphanumeric characters"
					+ "\nLowercase or uppercase characters"
					+ "\nDot, underscore, or hyphen cannot be the first og last"
					+ " character"
					+ "\nDot, hyphen or underscore cannot appear consecutively"
					+ "\nBetween 5 and 20 characters");
		}
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	
	/**
	 * setPassword setter passordet til brukeren
	 * Har følgende krav
	 * Ikke null eller blank
	 * Minst 8 tegn langt
	 * Minst 1 bokstav og 1 tall
	 * @param password
	 */
	public void setPassword(String password) {
		breaksFileBuild(password);
		if (password == null) {
			throw new IllegalArgumentException("Password cannot be \"null\".");
		}
			
		if (password.isBlank()) {
			throw new IllegalArgumentException("Password cannot be empty");
		}
		if (!validPassword(password.trim())) {
			throw new IllegalArgumentException("Password must fulfill: "
					+ "\nAtleast 8 characters long"
					+ "\nContains atleast one letter and one number");
		}
		this.password = password;
	}

	public boolean getAdmin() {
		return admin;
	}
	
	
	public void setAdmin(boolean admin) {
		if (admin == true || admin == false) {
			this.admin = admin;
		} else {			
			throw new IllegalArgumentException("Admin state has to be \"true\""
					+ " or \"false\"");
		}
	}

	/**
	 * Valideringsmetoder
	 */
	
	private boolean validUserId(int userId) {
		return userId >= 0;
	}
	
	public boolean uniqueUserId(int userId) throws FileNotFoundException {
		EditUserFile info = new EditUserFile();
		ArrayList<ArrayList<String>> table = info.userInfoTable();
		for (int i = 0; i < table.size(); i++) {
			if (Integer.valueOf(table.get(i).get(0)) == userId) {
				return false;
			}
		}
		return true;
	}
	
	private boolean validName(String name) {
		if (!name.matches("^[ÆØÅæøåa-zA-Z-]+$")) {
			return false;
		}
		if (name.length() < 2) {
			return false;
		}
		if (name.contains("-")) {
			String nameBefore = name.substring(0, name.indexOf("-"));
			String nameAfter = name.substring(name.indexOf("-") + 1, name.length());
			if (nameBefore.length() < 2 || nameAfter.length() < 2) {
				return false;
			}
		}
		return true;
	}
	
	private boolean dateAfterToday(LocalDate dob) {			
		LocalDate today = LocalDate.now();
		if (dob.isAfter(today)) {
			return true;
		}
		return false;
	}
	
	private boolean validFieldOfStudy(String fod) {
		if (!fod.matches("[ÆØÅæøåa-zA-Z]") && fod.length() < 2 && fod != null) {
			return false;
		}
		return true;
	}
	
	/**
	 * Kilde for regex-kode:
	 * https://stackoverflow.com/questions/19605150/regex-for-password-must-contain-at-least-eight-characters-at-least-one-number-a
	 * @param password
	 * @return
	 */
	private boolean validPassword(String password) {
		if (password.matches("^(?=.*[ÆØÅæøåA-Za-z])(?=.*\\d)[ÆØÅA-Zæøåa-z\\d]{8,}[^,]+$")) {
			return true;
		}
		return false;
	}
	
	/**
	 * Kilde for regex-kode:
	 * https://mkyong.com/regular-expressions/how-to-validate-username-with-regular-expression/
	 * @param username
	 * @return
	 */
	private boolean validUsername(String username) {
		if (username.matches("^[æøåa-zÆØÅA-Z0-9]([._-](?![._-])|[æøåa-zÆØÅA-Z0-9]){3,18}[æøåa-zÆØÅA-Z0-9]$")) {
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
		if (string != null && !string.isBlank()) {
			if (string.contains(", ") || string.contains(",==") || string.contains(",__") || string.contains("__") || string.contains("==")) {
				throw new IllegalArgumentException("Input cannot contain \",__\", "
						+ "\",==\", \"==\". \"__\".");
			}
		}
	}
	
	public static void main(String[] args) throws FileNotFoundException {
		User test = new User();
		test.setUserId(1);
		test.setUserSubjects("TDT4110", 'A');
	}
}
