package home.accounting.view;

import java.util.Locale;

import home.accounting.MainApp;
import home.accounting.controller.Word;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

public class DialogOverlayController {
	
	@FXML
	private TextArea msg;
	
	@FXML
	private Label bindingText;
	
	@FXML
	private Button button; 
	
	private MainApp mainApp;
	
	@FXML
	private void initialize() {
		bindingText.textProperty().bind(msg.textProperty());
		msg.setText(Word.customTranslate("data_transfer_failed", Locale.getDefault().getLanguage()));
		msg.prefHeightProperty().bind(bindingText.heightProperty());
		button.setText(Word.customTranslate("close_app", Locale.getDefault().getLanguage()));
	}
	
	@FXML
	private void pressButton(){
		mainApp.closeApp();
	}
	
	public void setMainApp(MainApp app){
		this.mainApp = app;
	}

}
