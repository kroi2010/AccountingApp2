package home.accounting.controller;

import java.util.function.Consumer;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class FocusChangeListener implements ChangeListener<Boolean>{
	private final TextField textField;
	private final Consumer<? super String> changeAction;
	private final Consumer<? super String> initialAction;
	private String initialValue = null;

	public FocusChangeListener(TextField textField, Consumer<? super String> changeAction, Consumer<? super String> initialAction) {
		this.textField = textField;
		this.changeAction = changeAction;
		this.initialAction = initialAction;
	}
	
	public FocusChangeListener(TextField textField, Consumer<? super String> changeAction) {
		this.textField = textField;
		this.changeAction = changeAction;
		this.initialAction = null;
	}
	
	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean focus) {
		if(focus){ // gained focus
			initialValue = textField.getText();
			if(initialAction != null){
				initialAction.accept(initialValue);
			}
		}else{     // lost focus
			trimText(textField);
			if(changeAction != null && !textField.getText().equals(initialValue)){ // text was changed
				changeAction.accept(textField.getText());
			}
		}
	}
	
	private void trimText(TextField field) {
		String string = field.getText();
		string = string.replaceAll("\\s+", " ").trim();
		field.setText(string);
	}

}
