package subject_database.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class EditUserFile extends User implements UserFileManagement {
	
	/**
	 * Legger til bruker til users.txt
	 */
	@Override
	public void addToFile(User user) throws FileNotFoundException {
		fileCheck(USER_INFO_FILE);
		fileCheck(USER_IDS_FILE);
		ArrayList<String> inputDataList = new ArrayList<>(
				Arrays.asList(String.valueOf(user.getUserId()), 
				user.getFirstname(), user.getSurname(), 
				String.valueOf(user.getDob()), user.getFieldOfStudy(), 
				user.getUsername(), user.getPassword(),	String.valueOf
				(user.getUserSubjects()), String.valueOf(user.getAdmin())));
		
		try {
			PrintWriter fw = new PrintWriter(new FileWriter(USER_INFO_FILE, true));
			Writer writer = new BufferedWriter(fw);
			writer.write(inputDataList.toString() + "\n");
			writer.flush();
			writer.close();
			System.out.println("Written to file.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * Overskriver fil med en ny tabell
	 */
	@Override
	public void overwriteFile(ArrayList<ArrayList<String>> userTable) throws FileNotFoundException {
		fileCheck(USER_INFO_FILE);
		fileCheck(USER_IDS_FILE);
		String userInfoString = "";
		try {
			PrintWriter fw = new PrintWriter(new FileWriter(USER_INFO_FILE, false));
			Writer writer = new BufferedWriter(fw);
			for (int i = 0; i < userTable.size(); i++) {
				userInfoString += userTable.get(i).toString() + "\n";
			}
			writer.write(userInfoString);
			writer.close();
			System.out.println(userInfoFilePath + " has been overwritten.");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Henter en liste med brukerinfoen til alle brukere
	 */
	@Override
	public ArrayList<String> getDataFromFile() throws FileNotFoundException {
		fileCheck(USER_INFO_FILE);
		fileCheck(USER_IDS_FILE);
		ArrayList<String> allUserInfoList = new ArrayList<String>();
		try {
			Scanner scanner = new Scanner(USER_INFO_FILE);
			while (scanner.hasNextLine()) {
				String list = scanner.nextLine();
				allUserInfoList.add(list);
			}
			scanner.close();	
		} catch (IOException e) {
			e.printStackTrace();
		}
		return allUserInfoList;
	}
	
	/**
	 * Endrer admin state, enten fra true til false eller fra false til true
	 * @param userId
	 * @param userInput
	 */
	public void editAdmin(String userId, String userInput) throws FileNotFoundException {
		if (!trueOrFalse(userInput)) {
			throw new IllegalStateException("Admin state has to be true or "
					+ "false.");
		}
		ArrayList<ArrayList<String>> userTable = userInfoTable();
		ArrayList<ArrayList<String>> newUserTable = new ArrayList
														<ArrayList<String>>();
		for (int i = 0; i < userTable.size(); i++) {
			ArrayList<String> userList = userTable.get(i);
			if (userList.get(0).equals(userId)) {
				userList.set(8, userInput);
				newUserTable.add(userList);
				System.out.println("Admin state changed.");
			} else if (!userList.get(0).equals(userId)) {
				newUserTable.add(userList);
			}
		}
		overwriteFile(newUserTable);
	}
	
	/**
	 * Lager en tabell av brukerinfo for alle brukere
	 * @return
	 */
	public ArrayList<ArrayList<String>> userInfoTable() throws FileNotFoundException {
 		ArrayList<String> allUserList = getDataFromFile();
		ArrayList<ArrayList<String>> userInfoTable = new ArrayList<ArrayList<String>>();
		
		for (String userString : allUserList) {
			boolean noSubjects = false;
			int firstIndex = 1;
			int lastIndex = userString.length() - 1;
			userString = userString.substring(firstIndex, lastIndex);
			ArrayList<String> userList = new ArrayList<String>
										(Arrays.asList(userString.split(", ")));
			
			if (userString.contains("[]")) {
				noSubjects = true;
			}
			if (noSubjects) {
				userList.set(7, "[]");
				userInfoTable.add(userList);
			} else if (!noSubjects) {
				ArrayList<ArrayList<String>> subjectTable = new ArrayList
						<ArrayList<String>>();
				String subjectString = userList.get(7);
				firstIndex = 1;
				lastIndex = subjectString.length() - 1;
				subjectString = subjectString.substring(firstIndex, lastIndex);
				String[] subjects = subjectString.split(",__");
				for (String subject : subjects) {
					subject = subject.replace("[", "").replace("]", "");
					ArrayList<String> subjectList = new ArrayList<String>
					(Arrays.asList(subject.split(",==")));
					subjectTable.add(subjectList);
				}
				String subjectTableString = subjectTable.toString()
						.replace("], ", "],__").replace(", ", ",==");
				userList.set(7, subjectTableString);
				userInfoTable.add(userList);
			}
		}
		return userInfoTable;
	}
	
	/**
	 * Sletter bruker med oppgitt bruker ID
	 * @param userId
	 */
	public void deleteUser(String userId) {
		try {
			int id = Integer.valueOf(userId);
			User user = new User();
			
			if (user.uniqueUserId(id)) {
				throw new IllegalArgumentException("There are no users with "
						+ "the ID: " + userId);
			}
			
			ArrayList<ArrayList<String>> userTable = userInfoTable();
			ArrayList<ArrayList<String>> newUserTable = new ArrayList
														<ArrayList<String>>();
			
			for (int i = 0; i < userTable.size(); i++) {
				if (Integer.valueOf(userTable.get(i).get(0)) != id) {
					newUserTable.add(userTable.get(i));
				}
			overwriteFile(newUserTable);
			}
		} catch (Exception e) {
			throw new IllegalArgumentException("Could not find a user ID that "
					+ "matches " + userId);
		}
	}
	
	/**
	 * Henter ut en liste med info til en bruker med oppgitt ID
	 * @param userId
	 * @return
	 */
	public ArrayList<String> getUserOnId(int userId) throws FileNotFoundException {
		User user = new User();
		if (user.uniqueUserId(userId)) {
			throw new IllegalArgumentException("There are no users that have "
					+ "the ID " + userId);
		}
		ArrayList<ArrayList<String>> userTable = userInfoTable();
		ArrayList<String> output = new ArrayList<String>();
		
		for (ArrayList<String> userList : userTable) {
			if (Integer.valueOf(userList.get(0)) == userId) {
				
				output.add(String.valueOf(userId));
				output.add(userList.get(1));
				output.add(userList.get(2));
				output.add(userList.get(3));
				output.add(userList.get(4));
				output.add(userList.get(5));
				output.add(userList.get(6));
				output.add(userList.get(7));
				output.add(userList.get(8));
				break;
			}
		}
		
		if (output.size() == 0) {
			throw new IllegalArgumentException("Could not find a user on the "
					+ "ID " + userId);
		}
		
		return output;
	}
	
	/**
	 * Legger til fag og karakter for en bruker. Fagene må eksistere i subjects.txt
	 * @param userId
	 * @param subjectCode
	 * @param grade
	 */
	public void addUserSubjects(int userId, String subjectCode, String grade) throws FileNotFoundException {
		if (grade == null || grade.isBlank()) {
			grade = "0";
		}
		char convertedGrade = grade.charAt(0);
		
		User user = new User();
		user.setUserId(getLastId());
		user.setUserSubjects(subjectCode.toUpperCase(), convertedGrade);
		if (addedSubject(subjectCode)) {
			throw new IllegalArgumentException("You have allready added " + 
						subjectCode + " to the database.");
		}
		
		ArrayList<ArrayList<String>> userSubjectTable = user.getUserSubjects();
		String validSubjectCode = userSubjectTable.get(0).get(0);
		String validSubjectName = userSubjectTable.get(0).get(1);
		String validGrade = userSubjectTable.get(0).get(2);
		
		ArrayList<String> userList = getUserOnId(userId);
		String subjectString = userList.get(7);
		
		// Gjør om strukturen på fagene slik at det er mulig å skille mellom
		// verdier til brukeren (id, fornavn, etternavn, brukernavn osv.) og
		// verdier til fag
		if (hasSubjects(userList)) {
			subjectString = subjectString.replace("]]", "],__[==" + validSubjectCode 
								+ ",==" + validSubjectName + ",==" + validGrade + "]]");
		} else {
			subjectString = subjectString.replace("[]", "[==" + validSubjectCode + ",==" 
							+ validSubjectName+ ",==" + validGrade + "]");
		}
		userList.set(7, subjectString);
		ArrayList<ArrayList<String>> userTable = userInfoTable();
		for (int i = 0; i < userTable.size(); i++) {
			ArrayList<String> list = userTable.get(i);
			if (Integer.valueOf(list.get(0)) == userId) {
				int index = userTable.indexOf(list);
				userTable.set(index, userList);
				break;
			}
		}
		overwriteFile(userTable);
	}
	
	public void editUserSubject(int userId, String subjectCode, String grade) throws FileNotFoundException {
		if (grade.length() > 1) {
			throw new IllegalArgumentException("Your grade is invalid.");
		}
		
		if (grade == null || grade.isBlank()) {
			grade = "0";
		}
		
		char convertedGrade = grade.charAt(0);
		setUserSubjects(subjectCode, convertedGrade);
		subjectCode = subjectCode.toUpperCase();
		ArrayList<ArrayList<String>> subjectTable = userSubjectTable(userId);
		grade = String.valueOf(convertedGrade).toUpperCase();
		if (subjectTable.size() == 0) {
			throw new IllegalArgumentException("You do not have any subjects "
					+ "to edit.");
		}
		
		String stringTable = "";
		int errorNum = 0;
		
		for (ArrayList<String> subject : subjectTable) {
			String stringSubject = "";
			
			if (subject.get(0).equals(subjectCode)) {
				stringSubject = "[==" + subject.get(0) + ",==" + subject.get(1)
				+ ",==" + grade.toUpperCase() + "]";
				errorNum++;
			} else {
				stringSubject = "[==" + subject.get(0) + ",==" + subject.get(1)
				+ ",==" + subject.get(2) + "]";
			}
			
			if (stringTable.isBlank()) {
				stringTable = "[" + stringSubject + "]";
			} else {
				stringTable = stringTable.replace("[[", "[").replace("]]", "]");
				stringTable += ",__" + stringSubject + "]"; 
			}
		}
		
		if (errorNum == 0) {
			throw new IllegalArgumentException("Did not find any subject code" +
					" that matches " + subjectCode);
		}
		
		ArrayList<ArrayList<String>> userTable = userInfoTable();
		ArrayList<ArrayList<String>> updatedUserTable = new 
												ArrayList<ArrayList<String>>();
		
		for (ArrayList<String> userList : userTable) {
			if (userList.get(0).equals(String.valueOf(userId))) {
				userList.set(7, stringTable);
				updatedUserTable.add(userList);
			} else {
				updatedUserTable.add(userList);
			}
		}
		
		overwriteFile(updatedUserTable);
	}
	
	public void deleteUserSubject(int userId, String subjectCode) throws FileNotFoundException {
		char grade = '0';
		setUserSubjects(subjectCode, grade);
		subjectCode = subjectCode.toUpperCase();
		ArrayList<ArrayList<String>> subjectTable = userSubjectTable(userId);
		
		if (subjectTable.size() == 0) {
			throw new IllegalArgumentException("You do not have any subjects "
					+ "to delete.");
		}
		
		String stringTable = "";
		int errorNum = 0;
		
		for (ArrayList<String> subject : subjectTable) {
			String stringSubject = "";
			
			if (subject.get(0).equals(subjectCode)) {
				errorNum++;
			} else {
				stringSubject = "[==" + subject.get(0) + ",==" + subject.get(1)
				+ ",==" + subject.get(2) + "]";
			}
			if (!stringSubject.isBlank()) {
				if (stringTable.isBlank()) {
					stringTable = "[" + stringSubject + "]";
				} else {
					stringTable = stringTable.replace("[[", "[").replace("]]", "]");
					stringTable += ",__" + stringSubject + "]";
					stringTable = "[" + stringTable;
				}
			}
		}
		
		if (errorNum == 0) {
			throw new IllegalArgumentException("Did not find any subject codes " 
					+ "that matches " + subjectCode);
		}
		
		ArrayList<ArrayList<String>> userTable = userInfoTable();
		ArrayList<ArrayList<String>> updatedUserTable = new 
												ArrayList<ArrayList<String>>();
		
		for (ArrayList<String> userList : userTable) {
			if (userList.get(0).equals(String.valueOf(userId))) {
				if (stringTable.isBlank()) {
					stringTable = "[]";
				}
				userList.set(7, stringTable);
				updatedUserTable.add(userList);
			} else {
				updatedUserTable.add(userList);
			}
		}
		
		overwriteFile(updatedUserTable);
	}
	
	/**
	 * Lager en tabell med fagene til en bruker
	 * @param userId
	 * @return
	 */
	public ArrayList<ArrayList<String>> userSubjectTable(int userId) throws FileNotFoundException {
		ArrayList<String> userList = getUserOnId(userId);
		ArrayList<ArrayList<String>> subjectTable = new ArrayList<ArrayList<String>>();
		String subjectString = userList.get(7);
		if (subjectString.indexOf("[]") == -1) {			
			subjectString = subjectString.replace("[", "").replace("]", "");
			ArrayList<String> subjects = new ArrayList<String>
			(Arrays.asList(subjectString.split(",__")));
			for (String data : subjects) {
				ArrayList<String> list = new ArrayList<String>();
				String[] subjectInfo = data.split(",==");
				for (String word : subjectInfo) {
					word = word.replace("==", "");
					list.add(word);
				}
				subjectTable.add(list);
			}
		}
		return subjectTable;
	}
	
	/**
	 * Skriver til en fil med kun bruker ID, gjort dette for å kunne sende info
	 * mellom kontrollere
	 */
	@Override
	public void writeToUserIdFile(int userId) {
		try {
			PrintWriter fw = new PrintWriter(new FileWriter(USER_IDS_FILE, true));
			Writer writer = new BufferedWriter(fw);
			writer.write(String.valueOf(userId) + "\n");
			writer.flush();
			writer.close();
			System.out.println(userIdFilePath + " has been overwritten.");
		} catch (IOException e) {
			e.printStackTrace();
		}	
	}
	
	/**
	 * Henter ut siste bruker ID som blir lagt til
	 * @throws FileNotFoundException 
	 */
	@Override
	public int getLastId() throws FileNotFoundException {
		fileCheck(USER_INFO_FILE);
		fileCheck(USER_IDS_FILE);
		String id = "";
		try {
			Scanner scanner = new Scanner(USER_IDS_FILE);
			while (scanner.hasNextLine()) {
				id = scanner.nextLine();
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (id.isBlank()) {
			throw new IllegalArgumentException("Something went wrong with " +
					"user ID.");
		}
		return Integer.valueOf(id);
	}
	
//	VALIDERINGSMETODER
	private boolean trueOrFalse(String userInput) {
		if (userInput.equals("true") || userInput.equals("false")) {
			return true;
		}
		return false;
	}
	
	public boolean hasSubjects(ArrayList<String> userList) {
		if (userList.get(7).indexOf("[]") == -1) {
			return true;
		}
		return false;	
	}
	
	private boolean addedSubject(String subjectCode) throws FileNotFoundException {
		EditUserFile userFile = new EditUserFile();
		ArrayList<ArrayList<String>> subjectTable = new ArrayList<ArrayList<String>>();
		try {
			subjectTable = userFile.userSubjectTable(userFile.getLastId());			
		} catch (IllegalArgumentException e) {
			return false;
		}
		for (ArrayList<String> subject : subjectTable) {
			if (subject.get(0).equals(subjectCode.toUpperCase())) {
				return true;
			}
		} 
		return false;
	}
	
	private void fileCheck(File file) throws FileNotFoundException {
		String fileName = file.getName();
		if (!file.exists()) 
			throw new FileNotFoundException("Something went wrong with the database");
		if (!file.canRead()) 
			throw new FileNotFoundException("Can not read " + fileName);
		if (!file.canWrite()) 
			throw new FileNotFoundException("Can not write to " + fileName);
	}
}
