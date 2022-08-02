package lircom;
import org.w3c.dom.Document;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import javafx.application.Application;
import javafx.event.ActionEvent;
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
    private final String INITIAL_TEXT = "Start typing here";
 
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
 
        final HTMLEditor htmlEditor = new HTMLEditor();
        htmlEditor.setPrefHeight(245);
        htmlEditor.setHtmlText(INITIAL_TEXT);
	htmlEditor.setBackground(new Background(new BackgroundImage(new Image(getClass().getResourceAsStream("/lircom/Space_Tears_II.jpg")), BackgroundRepeat.NO_REPEAT, BackgroundRepeat.NO_REPEAT, BackgroundPosition.DEFAULT, BackgroundSize.DEFAULT)));
        
        final WebView browser = new WebView();
	browser.setPageFill(Color.TRANSPARENT);
        final WebEngine webEngine = browser.getEngine();
	webEngine.setUserStyleSheetLocation(getClass().getResource("/lircom/image.css").toString());
     
        ScrollPane scrollPane = new ScrollPane();
        scrollPane.getStyleClass().add("noborder-scroll-pane");
        scrollPane.setContent(browser);
        scrollPane.setFitToWidth(true);
        scrollPane.setPrefHeight(180);
 
        Button showHTMLButton = new Button("Load Content in Browser");
        root.setAlignment(Pos.CENTER);
        showHTMLButton.setOnAction(new EventHandler<ActionEvent>() {
	    java.lang.StringBuffer buf = new java.lang.StringBuffer();
            @Override public void handle(ActionEvent arg0) {
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
            }
        });
        
        root.getChildren().addAll(scrollPane, htmlEditor, showHTMLButton);
        scene.setRoot(root);
        stage.setScene(scene);
        stage.show();
    }
 
    public static void main(String[] args) {
        launch(args);
    }
}
