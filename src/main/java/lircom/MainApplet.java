package lircom;

import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplet extends Application {
	public void start(Stage stage) {
		stage.setTitle("LIRCom Chat");
		stage.setWidth(500);
		stage.setHeight(500);
		try {
			new HTMLEditorSample(stage);
		} catch (Exception e) {
			e.printStackTrace();
		}
        	stage.show();
	}
	static public void main(String [] args) {
		launch(args);
	}
}
