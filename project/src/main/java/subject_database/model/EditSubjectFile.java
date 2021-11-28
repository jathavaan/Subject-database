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
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class EditSubjectFile implements SubjectFileManagement{
	
	/**
	 * Legger til fag til subject.txt
	 */
	@Override
	public void addToFile(Subject subject) throws FileNotFoundException {
		fileCheck(SUBJECT_FILE);
		HashMap<String, String> existingFile = readFile();
		HashMap<String, String> subjects = subject.getSubjects();
		HashMap<String, String> updatedFile = new HashMap<String, String>();
		if (subjects.size() == 0) {
			throw new IllegalArgumentException("Subjectname and subjectcode cannot be blank");
		}
		
		for (Map.Entry<String, String> entry1 : subjects.entrySet()) {
			String subjectCodeInput = entry1.getKey();
			String subjectNameInput = entry1.getValue();
			
			Subject subjectValidation = new Subject();
			subjectValidation.setSubjects(subjectCodeInput, subjectNameInput);
			
			updatedFile.put(subjectCodeInput, subjectNameInput);
		}
		for (Map.Entry<String, String> entry2 : existingFile.entrySet()) {
			String subjectCode = entry2.getKey();
			String subjectName = entry2.getValue();
			updatedFile.put(subjectCode, subjectName);
		}
		
		try {
			PrintWriter fw = new PrintWriter(new FileWriter(SUBJECT_FILE, false));
			Writer writer = new BufferedWriter(fw);
			writer.write(updatedFile.toString());
			writer.close();
			System.out.println("Written to " + SUBJECT_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Returnerer en HashMap med alle fagene admin har registrert, altså fagene
	 * i subjects.txt
	 */
	@Override
	public HashMap<String, String> readFile() throws FileNotFoundException {
		fileCheck(SUBJECT_FILE);
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			Scanner scanner = new Scanner(SUBJECT_FILE);
			while (scanner.hasNextLine()) {
				String[] list = scanner.nextLine().replace("{", "")
							.replace("}", "").replace("==", "=").split(", ");
				for (String hashMap : list) {
					if (hashMap.indexOf("=") == -1) {
						break;
					}
					String subjectCode = hashMap.substring(0, hashMap.indexOf("="));
					String subjectName = hashMap.substring
								(hashMap.indexOf("=") + 1, hashMap.length());
					map.put(subjectCode, subjectName);
				}
			}
			scanner.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return map;
	}
	
	/**
	 * Sletter et fag fra subjects.txt, overskriver filen
	 * @throws FileNotFoundException 
	 */
	@Override
	public void deleteSubject(String subjectCode) throws FileNotFoundException {
		fileCheck(SUBJECT_FILE);
		subjectCode = subjectCode.toUpperCase();
		HashMap<String, String> subjects = readFile();
		HashMap<String, String> newSubjectMap = new HashMap<String, String>();
		for (Map.Entry<String, String> entry : subjects.entrySet()) {
			String code = entry.getKey();
			String name = entry.getValue();
			if (!code.equals(subjectCode)) {
				newSubjectMap.put(code, name);
			}
		}
		if (subjects.equals(newSubjectMap)) {
			throw new IllegalArgumentException("Could not find any subjectcodes "
					+ "that matches " + subjectCode);
		}
		
		deleteSubjectFromAllUsers(subjectCode.toUpperCase());
		
		try {
			PrintWriter fw = new PrintWriter(new FileWriter(SUBJECT_FILE, false));
			Writer writer = new BufferedWriter(fw);
			writer.write(newSubjectMap.toString() + "\n");
			writer.close();
			System.out.println("Written to " + SUBJECT_FILE);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Sletter fag fra alle brukere når admin sletter fag fra databasen 
	 * @param subjectCode
	 * @throws FileNotFoundException
	 */
	private void deleteSubjectFromAllUsers(String subjectCode) throws FileNotFoundException {
		fileCheck(SUBJECT_FILE);
		EditUserFile userFile = new EditUserFile();
		ArrayList<String> userTable = userFile.getDataFromFile();
		for (int i = 0; i < userTable.size(); i++) {
			ArrayList<String> userInfo = new ArrayList<String>
								(Arrays.asList(userTable.get(i).replace("[", "")
												.replace("]", "").split(", ")));
			int userId = Integer.valueOf(userInfo.get(0));
			try {
				userFile.deleteUserSubject(userId, subjectCode.toUpperCase());
			} catch (IllegalArgumentException e) {
				// skal ikke gjøre noe
			}
		}
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
