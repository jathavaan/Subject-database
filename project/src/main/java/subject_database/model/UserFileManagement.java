package subject_database.model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;

public interface UserFileManagement {
	static String folderPath = "src/main/resources/subject_database/database/";
	
	static String usersFilename = folderPath + "users.txt";
	static String userIdsFilename = folderPath + "userIds.txt";
	
	static String workingDirectory = System.getProperty("user.dir");
	
	static String userInfoFilePath = workingDirectory + File.separator + usersFilename;
	static String userIdFilePath = workingDirectory + File.separator + userIdsFilename;
	
	final File USER_INFO_FILE = new File(userInfoFilePath);
	final File USER_IDS_FILE = new File(userIdFilePath);
	
	void addToFile(User user) throws FileNotFoundException;
	
	ArrayList<String> getDataFromFile() throws FileNotFoundException;

	void overwriteFile(ArrayList<ArrayList<String>> userTable) throws FileNotFoundException;
	
	void writeToUserIdFile(int userId) throws FileNotFoundException;
	
	int getLastId() throws FileNotFoundException;
}
