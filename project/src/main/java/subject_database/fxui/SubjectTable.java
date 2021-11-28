package subject_database.fxui;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.Map;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import subject_database.model.EditSubjectFile;

public class SubjectTable {
	private SimpleStringProperty tableTableSubjectCode, tableTableSubjectName;
	
	public String getTableSubjectCode() {
		return tableTableSubjectCode.get();
	}

	public void setTableSubjectCode(String tableTableSubjectCode) {
		this.tableTableSubjectCode = new SimpleStringProperty(tableTableSubjectCode);
	}

	public String getTableSubjectName() {
		return tableTableSubjectName.get();
	}

	public void setAdminTableSubjectName(String tableTableSubjectName) {
		this.tableTableSubjectName = new SimpleStringProperty(tableTableSubjectName);
	}
	
	/**
	 * Lager en observable list med instanser av SubjectTable
	 * Benyttes til å lage tabell over fag
	 * @return 
	 * @throws FileNotFoundException 
	 */
	public ObservableList<SubjectTable> getSubjects() throws FileNotFoundException {
		EditSubjectFile file = new EditSubjectFile();
		HashMap<String, String> subjectMap = file.readFile();
		ObservableList<SubjectTable> subjects = FXCollections.observableArrayList();
		for (Map.Entry<String, String> entry : subjectMap.entrySet()) {
			String code = entry.getKey();
			String name = entry.getValue();
			
			SubjectTable subject = new SubjectTable();
			subject.setTableSubjectCode(code);
			subject.setAdminTableSubjectName(name);
			
			subjects.add(subject);
		}
		return subjects;
	}
}
