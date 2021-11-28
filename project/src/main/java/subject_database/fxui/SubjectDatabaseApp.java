package subject_database.fxui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class SubjectDatabaseApp extends Application {

	@Override
	public void start(final Stage primaryStage) throws Exception {
		primaryStage.setTitle("Subject database");
		primaryStage.setScene(new Scene(FXMLLoader.load(SubjectDatabaseApp.class.getResource("loginAndRegister.fxml"))));
		primaryStage.setFullScreen(false);
		primaryStage.show();
	}
	
	public static void main(String[] args) {
		SubjectDatabaseApp.launch(args);
	}
}