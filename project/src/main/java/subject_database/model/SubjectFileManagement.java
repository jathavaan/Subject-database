package subject_database.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

public interface SubjectFileManagement {
	static String folderPath = "src/main/resources/subject_database/database/";
	static String subjectFilename = folderPath + "subjects.txt";
	static String workingDirectory = System.getProperty("user.dir");
	static String subjectPath = workingDirectory + File.separator + subjectFilename;
	final File SUBJECT_FILE = new File(subjectPath);
	
	void addToFile(Subject subject) throws FileNotFoundException;
	
	HashMap<String, String> readFile() throws FileNotFoundException;
	
	void deleteSubject(String subjectCode) throws FileNotFoundException;
}
