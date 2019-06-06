package home.accounting.view;

import java.time.LocalDate;

import home.accounting.DA.CostsDA;
import home.accounting.controller.CurrentUser;
import home.accounting.controller.TextFieldPasteListener;
import home.accounting.controller.Word;
import home.accounting.controller.converter.Money;
import home.accounting.model.Costs;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyEvent;

public class CostsPageController {

	@FXML
	private Label maintenanceLabel;

	@FXML
	private Label heatingLabel;

	@FXML
	private Label garbageLabel;

	@FXML
	private Label sewageLabel;

	@FXML
	private Label subscribtionFeesLabel;

	@FXML
	private Label electricityLabel;

	@FXML
	private TextFieldPasteListener maintenanceTextField;

	@FXML
	private TextFieldPasteListener heatingTextField;

	@FXML
	private TextFieldPasteListener garbageTextField;

	@FXML
	private TextFieldPasteListener sewageTextField;

	@FXML
	private TextFieldPasteListener subscriptionsFeesTextField;

	@FXML
	private TextFieldPasteListener electricityTextField;
	
	private Costs costs;

	@FXML
	private void initialize() {
		translateStrings();
		
		costs = CostsDA.getCosts(java.sql.Date.valueOf(LocalDate.now()), CurrentUser.getHouse());
		if (costs == null) {
			costs = new Costs();
		}

		maintenanceTextField.setText(Money.toString(costs.getMaintenance()));
		maintenanceTextField.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));
		maintenanceTextField.focusedProperty().addListener(moneyFieldLostFocus(maintenanceTextField));

		heatingTextField.setText(Money.toString(costs.getHeating()));
		heatingTextField.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));
		heatingTextField.focusedProperty().addListener(moneyFieldLostFocus(heatingTextField));

		garbageTextField.setText(Money.toString(costs.getGarbage()));
		garbageTextField.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));
		garbageTextField.focusedProperty().addListener(moneyFieldLostFocus(garbageTextField));

		sewageTextField.setText(Money.toString(costs.getWaterSewer()));
		sewageTextField.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));
		sewageTextField.focusedProperty().addListener(moneyFieldLostFocus(sewageTextField));

		subscriptionsFeesTextField.setText(Money.toString(costs.getSubscFees()));
		subscriptionsFeesTextField.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));
		subscriptionsFeesTextField.focusedProperty().addListener(moneyFieldLostFocus(subscriptionsFeesTextField));

		electricityTextField.setText(Money.toString(costs.getElectricity()));
		electricityTextField.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));
		electricityTextField.focusedProperty().addListener(moneyFieldLostFocus(electricityTextField));
	}

	private void translateStrings() {
		maintenanceLabel.setText(Word.translate("costs_maintenance") + ":");
		heatingLabel.setText(Word.translate("costs_heating") + ":");
		garbageLabel.setText(Word.translate("costs_garbage") + ":");
		sewageLabel.setText(Word.translate("costs_water") + ":");
		subscribtionFeesLabel.setText(Word.translate("costs_subscribtion_fees") + ":");
		electricityLabel.setText(Word.translate("costs_electricity") + ":");
	}

	private EventHandler<KeyEvent> fieldChangeListener(final Integer max_Lengh) {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				TextField field = (TextField) event.getSource();
				String text = field.getText();

				if (event.getCharacter().matches("[0-9.,]") && !moneyDigitRestriction(field)) {
					if (text.contains(".") && event.getCharacter().matches("[.,]")) {
						event.consume();
					} else if (text.length() == 0 && event.getCharacter().matches("[.]")) {
						event.consume();
					} else if (event.getCharacter().matches("[,]")) {
						int caretPosition = field.getCaretPosition();
						text = text.substring(0, caretPosition) + "." + text.substring(caretPosition, text.length());
						field.setText(text);
						field.positionCaret(caretPosition + 1);
						event.consume();
					}
				} else {
					event.consume();
				}

			}// end handle()
		};
	}

	private ChangeListener<Boolean> moneyFieldLostFocus(TextFieldPasteListener field) {
		return new ChangeListener<Boolean>() {
			private String initialValue = null;
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean focus) {
				if(focus){
					initialValue = field.getText();
					if(initialValue.equals(Money.getMoneyFormat())){
						field.setText("");
					}
				}else if (!focus && !field.getText().isEmpty() && !field.getText().equals(initialValue)) {
					field.setText(Money.toString(field.getText()));
					if(field==maintenanceTextField){
						costs.setMaintenance(Money.toInt(field.getText()));
					}else if(field==sewageTextField){
						costs.setWaterSewer(Money.toInt(field.getText()));
					}else if(field==subscriptionsFeesTextField){
						costs.setSubscFees(Money.toInt(field.getText()));
					}else if(field==electricityTextField){
						costs.setElectricity(Money.toInt(field.getText()));
					}else if(field==garbageTextField){
						costs.setGarbage(Money.toInt(field.getText()));
					}else if(field==heatingTextField){
						costs.setHeating(Money.toInt(field.getText()));
					}
					costs.setDate(java.sql.Date.valueOf(LocalDate.now()));
					costs.setHouse(CurrentUser.getHouse());
					CostsDA.update(costs);
				}else if(field.getText().isEmpty()){
					field.setText(Money.getMoneyFormat());
				}
			}
		};
	}

	private Boolean moneyDigitRestriction(TextField field) {
		String text = field.getText();
		String[] decimalParts = text.split("\\.");
		boolean restrict = false;

		if (decimalParts.length > 1) {
			if (decimalParts[1].length() >= 2) {
				int caretPosition = field.getCaretPosition();
				int dotPosition = decimalParts[0].length();
				if(caretPosition>dotPosition){
					restrict = true;
				}
			}
		}
		return restrict;
	}

}
