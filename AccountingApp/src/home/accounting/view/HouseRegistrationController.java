package home.accounting.view;

import java.util.ArrayList;
import java.util.Set;

import home.accounting.DA.CurrencyDA;
import home.accounting.DA.HouseDA;
import home.accounting.DA.LanguagesDA;
import home.accounting.controller.ControllerConnector;
import home.accounting.controller.CurrentUser;
import home.accounting.controller.PaneNavigator;
import home.accounting.controller.TextFieldPasteListener;
import home.accounting.controller.Validation;
import home.accounting.controller.Word;
import home.accounting.iban4j.IbanFormatException;
import home.accounting.iban4j.IbanUtil;
import home.accounting.iban4j.InvalidCheckDigitException;
import home.accounting.iban4j.UnsupportedCountryException;
import home.accounting.model.Bank;
import home.accounting.model.Currency;
import home.accounting.model.House;
import home.accounting.model.Languages;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Callback;

public class HouseRegistrationController {


	@FXML
	private Label streetLabel;

	@FXML
	private Label houseNumberLabel;

	@FXML
	private Label cityLabel;

	@FXML
	private Label countryLabel;

	@FXML
	private Label postCodeLabel;

	@FXML
	private Label flatNumberLabel;

	@FXML
	private Label bankLabel;

	@FXML
	private Label bankNumberLabel;

	@FXML
	private Label currencyLabel;

	@FXML
	private Label documentationLanguageLabel;
	
	/******************************************************************
	 * INPUT DATA
	 ******************************************************************/

	@FXML
	private TextField street;

	@FXML
	private TextField houseNumber;

	@FXML
	private TextField city;

	@FXML
	private TextField country;

	@FXML
	private TextField postCode;

	@FXML
	private TextField numberOfFlats;

	@FXML
	private CheckBox cutName;

	@FXML
	private CheckBox surnameFirst;

	/******************************************************************
	 * ACTION OBJECTS
	 ******************************************************************/

	@FXML
	private Button addBankButton;

	@FXML
	private Button readyButton;

	@FXML
	private Button skipButton;
	
	@FXML
	private Button cancelButton;

	@FXML
	private VBox homeRegistrationRegion;

	@FXML
	private VBox bankRegistrationRegion;

	@FXML
	private VBox bankContainer;

	@FXML
	private ScrollPane houseRegistrationScrollPane;
	
	@FXML
	private FlowPane buttonFlowPane;

	/******************************************************************
	 * NEED TO POPULATE WITH DATA
	 ******************************************************************/
	@FXML
	private ComboBox<Languages> languagesComboBox;
	
	@FXML
	private ComboBox<Currency> currenciesComboBox;

	/******************************************************************
	 * PRIVATE VARIABLES
	 ******************************************************************/
	private Callback<ListView<Currency>, ListCell<Currency>> currencyFactory;
	private Callback<ListView<Languages>, ListCell<Languages>> documentationLanguageFactory;
	
	@FXML
	private void initialize() {

		readyButton.setDisable(true);
		

		/******************************************************************
		 * FILL DROP DOWN MENUS
		 *****************************************************************/
		ObservableList<Currency> currencyItem = CurrencyDA.getAll();
		currenciesComboBox.setItems(currencyItem);
		currencyFactory = lv -> new ListCell<Currency>() {
			@Override
			protected void updateItem(Currency currency, boolean empty) {
				super.updateItem(currency, empty);
				try{
					setText(empty ? "" : Word.capitalize(Word.translate("currency_" + currency.getName())));
				}catch(Exception e){
				}
			}
		};

		currenciesComboBox.setCellFactory(currencyFactory);
		currenciesComboBox.setButtonCell(currencyFactory.call(null));
		currenciesComboBox.getSelectionModel().selectFirst();
		
		ObservableList<Languages> languages = LanguagesDA.getAll();
		languagesComboBox.setItems(languages);
		documentationLanguageFactory = lv -> new ListCell<Languages>() {
			@Override
			protected void updateItem(Languages lang, boolean empty) {
				super.updateItem(lang, empty);
				setText(empty ? "" : Word.translate("lang_" + lang.getCode()));
			}
		};

		languagesComboBox.setCellFactory(documentationLanguageFactory);
		languagesComboBox.setButtonCell(documentationLanguageFactory.call(null));
		languagesComboBox.getSelectionModel().selectFirst();

		/******************************************************************
		 * SET STRINGS
		 *****************************************************************/

		translateStrings();

		/******************************************************************
		 * SET EVENT HANDLERS
		 *****************************************************************/

		street.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		street.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(street);
				}
			}
		});

		houseNumber.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));

		houseNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(houseNumber);
				}
			}
		});

		city.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		city.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(city);
				}
			}
		});

		country.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		country.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(country);
				}
			}
		});

		postCode.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		postCode.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(postCode);
				}
			}
		});

		numberOfFlats.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(10));

		numberOfFlats.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(numberOfFlats);
				}
			}
		});

		bankContainer.lookup(".bank-name").addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		bankContainer.lookup(".bank-name").focusedProperty()
				.addListener(new TextFieldListener((TextField) bankContainer.lookup(".bank-name")));

		bankContainer.lookup(".bank-number").addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		bankContainer.lookup(".bank-number").focusedProperty()
				.addListener(new TextFieldListener((TextField) bankContainer.lookup(".bank-number")));
	}
	
	private void trimText(TextField field) {
		String userName = field.getText();
		userName = userName.replaceAll("\\s+", " ").trim();
		field.setText(userName);
	}
	
	private void translateStrings() {
			streetLabel.setText(Word.translate("house_registration_street"));
			houseNumberLabel.setText(Word.translate("house_registration_number"));
			cityLabel.setText(Word.translate("house_registration_city"));
			countryLabel.setText(Word.translate("house_registration_country"));
			postCodeLabel.setText(Word.translate("house_registration_post_code"));
			flatNumberLabel.setText(Word.translate("house_registration_flat_number"));
			bankLabel.setText(Word.translate("bank"));
			bankNumberLabel.setText(Word.translate("bank_number"));
			currencyLabel.setText(Word.translate("house_registration_currency") + ":");
			documentationLanguageLabel.setText(Word.translate("house_registration_documentation_language") + ":");
			cutName.setText(Word.translate("house_registration_cut_name"));
			surnameFirst.setText(Word.translate("house_registration_start_with_surname"));
			addBankButton.setText(Word.translate("house_registration_add_bank"));
			readyButton.setText(Word.translate("ready"));
			skipButton.setText(Word.translate("skip"));
			cancelButton.setText(Word.translate("cancel"));
			currenciesComboBox.setButtonCell(currencyFactory.call(null));
			languagesComboBox.setButtonCell(documentationLanguageFactory.call(null));
	}
	
	private EventHandler<KeyEvent> fieldChangeListener(final Integer max_Lengh) {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				TextField field = (TextField) event.getSource();
				String text = field.getText();
				Region icon = (Region) ((StackPane) field.getParent()).getChildren().get(1);

				if (!Validation.checkLength(text, max_Lengh)
						|| !Validation.whiteSpace(text, event.getCharacter(), false)) {
					event.consume();
					// if pasted value more than max length
					if(text.length()>max_Lengh){
						icon.getStyleClass().clear();
						icon.getStyleClass().add("field-error");
						icon.setMaxWidth(20);
						icon.setMaxHeight(20);
					}
				} else {
					if(Validation.acceptableInput(event.getCharacter())){
						text = text + event.getCharacter();
					}
					if (text.length() > 0) {
						boolean valid = false;

						if (field == street || field == city || field == country
								|| field.getStyleClass().contains("bank-name")) {
							valid = Validation.name(text);
						}else if (field == houseNumber) {
							valid = Validation.houseNumber(text);
						} else if (field == postCode) {
							valid = Validation.postCode(text);
						} else if (field == numberOfFlats) {
							valid = Validation.number(text);
						} else if (field.getStyleClass().contains("bank-number")) {
							try {
								String bankText = text.replaceAll("[\\s+\\-]", "");
								IbanUtil.validate(bankText);
								System.out.println("Iban valid");
								valid = true; // valid
							} catch (IbanFormatException | InvalidCheckDigitException | UnsupportedCountryException e) {
								valid = false;
								System.out.println("Iban NOT valid");
							}
						}

						if (valid) {
							icon.getStyleClass().clear();
							icon.getStyleClass().add("field-correct");
							icon.setMaxWidth(20);
							icon.setMaxHeight(16);
						} else {
							icon.getStyleClass().clear();
							icon.getStyleClass().add("field-error");
							icon.setMaxWidth(20);
							icon.setMaxHeight(20);
						}

					} else {
						icon.getStyleClass().clear();
					}

					disableHomeRegistrationButton();
				}
			}// end handle()
		};
	}
	
	@FXML
	private void homeRegistration() {
		try{
			ArrayList<Bank> bankList= new ArrayList<Bank>();
			
			Currency currency = currenciesComboBox.getValue();
			
			House house = new House(CurrentUser.getUser(), country.getText(), city.getText(), street.getText(), postCode.getText(),
					houseNumber.getText(), Integer.parseInt(numberOfFlats.getText()), null, currency,
					surnameFirst.isSelected(), cutName.isSelected(), languagesComboBox.getValue());
			
			house.setDefaultHouse(true);
			
			bankContainer.getChildren().forEach(bankContainer -> {
				String bankName = ((TextField)bankContainer.lookup(".bank-name")).getText();
				String account = ((TextField)bankContainer.lookup(".bank-number")).getText();
				bankList.add(new Bank(house, bankName, account));
			});
			
			HouseDA.register(house, bankList);
			
			if(buttonFlowPane.getChildren().contains(skipButton)){
				PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
				PaneNavigator.loadPane(PaneNavigator.home, PaneNavigator.center);
				PaneNavigator.loadPane(PaneNavigator.sideMenu, PaneNavigator.left);
			}else{
				PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
				PaneNavigator.loadPane(PaneNavigator.home, PaneNavigator.center);
				
				MainSideMenuController controller = ControllerConnector.getSideMenuController();
		        controller.refreshHouseInfo();
			}
		}catch(Exception e){
			System.out.println("Couldn't register house");
		}
	}
	
	@FXML
	private void skipHomeRegistration() {
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.home, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.sideMenu, PaneNavigator.left);
	}
	
	@FXML
	private void cancelHomeRegistration() {
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.home, PaneNavigator.center);
	}
	
	private EventHandler<InputEvent> removeBank = new EventHandler<InputEvent>() {
		public void handle(InputEvent event) {
			StackPane removingPane = (StackPane) ((Node) event.getSource()).getParent();
			Set<Node> bankNames = removingPane.lookupAll(".bank-name");
			bankContainer.getChildren().remove(removingPane);
			disableHomeRegistrationButton();
			event.consume();
		}
	};

	@FXML
	private void addBank() {

		StackPane stackPane = new StackPane();

		VBox vBox = new VBox();
		vBox.setSpacing(10);
		vBox.setPrefWidth(100);

		Label bnLabel = new Label();
		bnLabel.setText(Word.translate("bank"));
		bnLabel.getStyleClass().add("labels");

		TextField textField = new TextFieldPasteListener();
		textField.getStyleClass().add("text_field");
		textField.getStyleClass().add("bank-name");
		textField.setPrefHeight(30);
		textField.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));
		textField.focusedProperty().addListener(new TextFieldListener(textField));

		StackPane textFieldPane = new StackPane();
		textFieldPane.setMaxHeight(30);

		Region validDisplay = new Region();
		validDisplay.setMaxHeight(30);
		validDisplay.setMaxWidth(30);

		textFieldPane.getChildren().addAll(textField, validDisplay);
		StackPane.setAlignment(validDisplay, Pos.CENTER_RIGHT);

		Label bnLabel2 = new Label();
		bnLabel2.setText(Word.translate("bank_number"));
		bnLabel2.getStyleClass().add("labels");

		TextField textField2 = new TextFieldPasteListener();
		textField2.getStyleClass().add("text_field");
		textField2.setPrefHeight(30);
		textField2.getStyleClass().add("bank-number");
		textField2.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));
		textField2.focusedProperty().addListener(new TextFieldListener(textField2));

		StackPane textField2Pane = new StackPane();
		textField2Pane.setMaxHeight(30);

		Region validDisplay2 = new Region();
		validDisplay2.setMaxHeight(30);
		validDisplay2.setMaxWidth(30);

		textField2Pane.getChildren().addAll(textField2, validDisplay2);
		StackPane.setAlignment(validDisplay2, Pos.CENTER_RIGHT);

		vBox.getChildren().addAll(bnLabel, textFieldPane, bnLabel2, textField2Pane);
		VBox.setMargin(bnLabel2, new Insets(20, 0, 0, 0));

		HBox hBox = new HBox();

		Label removeLabel = new Label();
		removeLabel.setText(Word.translate("remove"));
		removeLabel.setPrefHeight(30);
		removeLabel.getStyleClass().add("remove-left");

		Region cross = new Region();
		cross.setPrefHeight(11);
		cross.setMaxHeight(11);
		cross.setPrefWidth(11);
		cross.setMaxWidth(11);
		cross.getStyleClass().add("remove-right-top");

		StackPane right = new StackPane();

		Region background = new Region();
		background.setPrefHeight(30);
		background.setMaxHeight(30);
		background.setPrefWidth(30);
		background.getStyleClass().add("remove-right-bottom");

		right.getChildren().addAll(background, cross);

		StackPane.setMargin(cross, new Insets(1, 0, 0, -1));

		hBox.getChildren().addAll(removeLabel, right);
		hBox.setTranslateX(310);
		hBox.setTranslateY(60);
		hBox.setMaxHeight(30);
		hBox.setCursor(Cursor.HAND);
		hBox.addEventHandler(MouseEvent.MOUSE_PRESSED, removeBank);

		stackPane.getChildren().addAll(vBox, hBox);

		bankContainer.getChildren().add(stackPane);

		disableHomeRegistrationButton();
	}
	
	private void disableHomeRegistrationButton() {
		int homeCorrectFields = homeRegistrationRegion.lookupAll(".field-correct").size();
		int bankCorrectFields = bankContainer.lookupAll(".field-correct").size();
		int bankFields = bankContainer.getChildren().size() * 2;
		int homeFields = homeRegistrationRegion.getChildren().size();
		
		if (homeCorrectFields == homeFields && bankCorrectFields == bankFields) {
			readyButton.setDisable(false);
		} else {
			readyButton.setDisable(true);
		}
	}
	
	public void initialRegistration(Boolean initialRegistration){
		if(initialRegistration){
			buttonFlowPane.getChildren().remove(cancelButton);
		}else{
			buttonFlowPane.getChildren().remove(skipButton);
		}
	}
}
