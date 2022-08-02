package lircom;
import org.w3c.dom.Document;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.KeyCode;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.web.HTMLEditor;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.scene.image.Image;
 
public class HTMLEditorSample extends Application {
    private final String INITIAL_TEXT = "Start typing here, then hit Enter to send";
    private final HTMLEditor htmlEditor = new HTMLEditor();
    private WebEngine webEngine = null;
 
    @Override
    public void start(Stage stage) {
        stage.setTitle("LIRCom Chat");
        stage.setWidth(500);
        stage.setHeight(500);
        Scene scene = new Scene(new Group());
	scene.getStylesheets().addAll(this.getClass().getResource("/lircom/image.css").toExternalForm());
    
        VBox root = new VBox();     
        root.setPadding(new Insets(8, 8, 8, 8));
        root.setSpacing(5);
        root.setAlignment(Pos.BOTTOM_LEFT);
 
        htmlEditor.setPrefHeight(245);
        htmlEditor.setHtmlText(INITIAL_TEXT);
	htmlEditor.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("/lircom/Space_Tears_II.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
	htmlEditor.setOnKeyPressed(new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent evt) {
	    	if (evt.getCode() == KeyCode.ENTER) {
		    appendText();
		}
	    }
	});
	htmlEditor.setOnKeyReleased(new EventHandler<KeyEvent>() {
            @Override public void handle(KeyEvent evt) {
	    	if (evt.getCode() == KeyCode.ENTER) {
		    htmlEditor.requestFocus();
		}
	    }
	});
        
        final WebView browser = new WebView();
	browser.setPageFill(Color.TRANSPARENT);
        webEngine = browser.getEngine();
	webEngine.setUserStyleSheetLocation(getClass().getResource("/lircom/image.css").toString());
     
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setContent(browser);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(180);
 
	/*
        Button showHTMLButton = new Button("Load Content in Browser");
        showHTMLButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override public void handle(ActionEvent arg0) {
	    	appendText();
            }
        });
	*/
        
        root.setAlignment(Pos.CENTER);
        root.getChildren().addAll(scrollPane, htmlEditor);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }

    java.lang.StringBuffer buf = new java.lang.StringBuffer();

    private void appendText() {
	String htmlText = htmlEditor.getHtmlText();
	if (!"<html dir='ltr'><head></head><body contenteditable='true'></body></html>".replace("'", "\"").equals(htmlText)) {
		buf.append("&lt;yottzumm&gt;");
		Pattern pattern = Pattern.compile("<[^>]*>");
		Matcher matcher = pattern.matcher(htmlText);
		while(matcher.find()) {
		    matcher.appendReplacement(buf, " ");
		}
		matcher.appendTail(buf);
		buf.append("<br>");
		webEngine.loadContent(buf.toString());
	}
	htmlEditor.setHtmlText("");
	htmlEditor.requestFocus();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
