package home.accounting.view;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import home.accounting.DA.BankDA;
import home.accounting.DA.FlatBankDA;
import home.accounting.DA.FlatDA;
import home.accounting.DA.LanguagesDA;
import home.accounting.DA.MailDA;
import home.accounting.controller.CoreConstants;
import home.accounting.controller.CurrentUser;
import home.accounting.controller.FlatInfoCheck;
import home.accounting.controller.FocusChangeListener;
import home.accounting.controller.ListenerHandle;
import home.accounting.controller.Validation;
import home.accounting.controller.Word;
import home.accounting.controller.converter.Area;
import home.accounting.model.Bank;
import home.accounting.model.Flat;
import home.accounting.model.Languages;
import home.accounting.model.Mail;
import javafx.animation.ParallelTransition;
import javafx.animation.Transition;
import javafx.beans.property.BooleanProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import javafx.util.Duration;

public class FlatInfoController {

	@FXML
	private Label nameLabel;

	@FXML
	private Label surnameLabel;

	@FXML
	private Label numberOfPeopleLabel;

	@FXML
	private Region morePeopleButton;

	@FXML
	private Region lessPeopleButton;

	@FXML
	private Label areaSizeLabel;

	@FXML
	private Label areaMeasureLabel;

	@FXML
	private Label mailLabel;

	@FXML
	private Label languageLabel;

	@FXML
	private Label messageHeaderLabel;

	@FXML
	private Label bankLabel;

	@FXML
	private VBox firstColumnVBox;

	@FXML
	private VBox secondColumnVBox;

	@FXML
	private Region spaceRegion;

	@FXML
	private VBox mainVBox;

	@FXML
	private VBox availableBanksVBox;

	@FXML
	private TextField nameTextField;

	@FXML
	private TextField surnameTextField;

	@FXML
	private TextField numberOfPeopleTextField;

	@FXML
	private TextField areaSizeTextField;

	@FXML
	private TextField mailTextField;

	@FXML
	private ComboBox<Languages> languageComboBox;

	@FXML
	private VBox mailVBox;

	@FXML
	private Region addMailButton;

	@FXML
	private CheckBox sendMail;

	@FXML
	private VBox bankVBox;

	@FXML
	private CheckBox addOwnMessage;

	@FXML
	private TextArea messageTextArea;

	@FXML
	private VBox messageContainer;

	@FXML
	private VBox messageVBox;

	@FXML
	private AnchorPane container;

	@FXML
	private Button flatLabel;

	@FXML
	private Button nextFlatButton;

	@FXML
	private Button previousFlatButton;

	@FXML
	private HBox pageContainerHBox;

	@FXML
	private Circle nextPagesButton;

	@FXML
	private Circle previousPagesButton;

	@FXML
	private HBox mailNumberHBox;

	private Flat currentFlat;

	private Flat lastMailFlat;

	private List<Flat> flats;

	private List<Mail> allMails;

	private String defaultMessage;

	private String userMessage;

	private final int maxPagesShown = 5;

	private int pageGroup = 0;

	private List<Circle> removedDots;

	private Callback<ListView<Languages>, ListCell<Languages>> mailLanguageFactory;
	private List<ListenerHandle<Boolean>> checkBoxListenerHandles = new ArrayList<>();
	private ListenerHandle<Boolean> sendMailListenerHandle;
	private ListenerHandle<Boolean> defaultMsgListenerHandle;
	private ListenerHandle<Languages> langComboBoxListenerHandle;

	@FXML
	private void initialize() {
		
		/******************************************************************
		 * INITIALIZE VARIABLES
		 *****************************************************************/
		
		flats = new ArrayList<>();
		flats = FlatDA.getAll(CurrentUser.getHouse());

		allMails = new ArrayList<>();
		allMails = MailDA.getAll();

		removedDots = new ArrayList<>();
		

		/******************************************************************
		 * FILL DROP DOWN MENUS
		 *****************************************************************/
		
		ObservableList<Languages> languages = LanguagesDA.getAll();
		languageComboBox.setItems(languages);

		mailLanguageFactory = lv -> new ListCell<Languages>() {
			@Override
			protected void updateItem(Languages lang, boolean empty) {
				super.updateItem(lang, empty);
				setText(empty ? "" : Word.translate("lang_" + lang.getCode()));
			}
		};

		languageComboBox.valueProperty().addListener(new ChangeListener<Languages>() {

			@Override
			public void changed(ObservableValue<? extends Languages> observable, Languages oldValue,
					Languages newValue) {
				if (newValue != null) {
					defaultMessage = Word.customTranslate("default_mail", newValue.getCode());
					Date date = new Date();
					LocalDate localDate = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
					defaultMessage = defaultMessage.replace("%month%",
							Word.customTranslate(localDate.getMonth().toString().toLowerCase(), newValue.getCode()));
					defaultMessage = defaultMessage.replace("%name%",
							CurrentUser.getUser().getName() + " " + CurrentUser.getUser().getSurname());
					messageTextArea.setText(defaultMessage);
				}

			}
		});

		languageComboBox.setCellFactory(mailLanguageFactory);
		languageComboBox.setButtonCell(mailLanguageFactory.call(null));

		for (Languages lang : languageComboBox.getItems()) {
			if (lang.getId() == CurrentUser.getUser().getInterfaceCode().getId()) {
				languageComboBox.getSelectionModel().select(lang);
				break;
			}
		}
		
		
		/******************************************************************
		 * PREPARE LAYOUT
		 *****************************************************************/
		
		if (!sendMail.isSelected()) {
			// one column view
			columnViewSwitch(sendMail.isSelected());
		}

		if (addOwnMessage.isSelected()) {
			messageTextArea.setText(userMessage);
			messageTextArea.setEditable(true);
			messageVBox.setVisible(false);
		} else {
			// messageTextArea.setText(defaultMessage);
			messageTextArea.setEditable(false);
			messageVBox.setVisible(true);
		}

		/******************************************************************
		 * TEXT FORMATTERS
		 *****************************************************************/

		nameTextField.setTextFormatter(new TextFormatter<>(change -> {
			int maxLength = 50;
			if (change.isAdded()) {
				if(change.getControlNewText().length()<=maxLength){
					if (change.getText().matches("\\s")) { // if whitespace
						int lastCharIndex = change.getCaretPosition();
						if ((lastCharIndex - 1) == 0 || (change.getControlText().substring(lastCharIndex - 2, lastCharIndex - 1).matches("\\s"))) {
							change = null;
						}
					} else {
						change = change.getControlNewText().matches("^[\\p{L} .'-]+$") ? change : null;
					}
				}else{
					if(change.getText().length()==1){
						change = null;
					}else{
						int allowedLength = maxLength - change.getControlText().length();
						change.setText(change.getText().substring(0, allowedLength));
					}
				}
				
				if(change!=null){
					if(nameTextField.isFocused()){
						if(Validation.name(change.getControlNewText())){
							showCorrectIcon(nameTextField);
						}else{
							showErrorIcon(nameTextField);
						}
					}else{
						if(!change.getControlText().equals(change.getControlNewText())){
							hideIcon(nameTextField);
						}
					}
				}
			}else if(change.isDeleted()){
					if(change.getControlNewText().length()>0){
						if(Validation.name(change.getControlNewText())){
							showCorrectIcon(nameTextField);
						}else{
							showErrorIcon(nameTextField);
						}
					}else{
						hideIcon(nameTextField);
					}
				}
			
			return change;
		}));
		
		
		surnameTextField.setTextFormatter(new TextFormatter<>(change -> {
			int maxLength = 50;
			
			if (change.isAdded()) {
				if(change.getControlNewText().length()<=maxLength){
					if (change.getText().matches("\\s")) { // if whitespace
						int lastCharIndex = change.getCaretPosition();
						if ((lastCharIndex - 1) == 0 || (change.getControlText()
								.substring(lastCharIndex - 2, lastCharIndex - 1).matches("\\s"))) {
							change = null;
						}
					} else {
						change = change.getControlNewText().matches("^[\\p{L} .'-]+$") ? change : null;
					}
				}else{
					if(change.getText().length()==1){
						change = null;
					}else{
						int allowedLength = maxLength - change.getControlText().length();
						change.setText(change.getText().substring(0, allowedLength));
					}
				}
				
				if(change!=null){
					if(surnameTextField.isFocused()){
						if(Validation.name(change.getControlNewText())){
							showCorrectIcon(surnameTextField);
						}else{
							showErrorIcon(surnameTextField);
						}
					}else{
						if(!change.getControlText().equals(change.getControlNewText())){
							hideIcon(surnameTextField);
						}
					}
				}
			}else if(change.isDeleted()){
					if(change.getControlNewText().length()>0){
						if(Validation.name(change.getControlNewText())){
							showCorrectIcon(surnameTextField);
						}else{
							showErrorIcon(surnameTextField);
						}
					}else{
						hideIcon(surnameTextField);
					}
				}
			
			return change;
		}));
		
		
		areaSizeTextField.setTextFormatter(new TextFormatter<>(change -> {
			int maxLength = 10;
			
			if (change.isAdded()) {
				if(change.getControlNewText().length()<=maxLength){
					if (change.getText().contains(",")) {
						change.setText(change.getText().replaceAll(",", "."));
					}
					change = change.getControlNewText().matches("^\\d*(\\.\\d{0,1})?$") ? change : null;
				}else{
					if(change.getText().length()==1){
						change = null;
					}else{
						int allowedLength = maxLength - change.getControlText().length();
						change.setText(change.getText().substring(0, allowedLength));
					}
				}
			}
			return change;
		}));
		
		numberOfPeopleTextField.setTextFormatter(new TextFormatter<>(change -> {
			int maxLength = 3;
			
			if (change.isAdded() && numberOfPeopleTextField.isFocused()) {
				if(change.getControlNewText().length()>maxLength){
					if(change.getText().length()==1){
						change = null;
					}else{
						int allowedLength = maxLength - change.getControlText().length();
						change.setText(change.getText().substring(0, allowedLength));
					}
				}
				
				if(change!=null){
					change = change.getControlNewText().matches("^(?!0)[0-9]+$") ? change : null;
					if(change!=null){
						change = Integer.parseInt(change.getControlNewText())>100 ? null : change;
					}
				}
				
				if(change!=null){
					numberOfPeopleButtonController(change.getControlNewText());
				}
			}else if(change.isDeleted() && numberOfPeopleTextField.isFocused()){
				numberOfPeopleButtonController(change.getControlNewText());
			}
			return change;
		}));
		
		mailTextField.setTextFormatter(new TextFormatter<>(change -> {
			int maxLength = 100;
			
			if (change.isAdded()) {
				if(change.getControlNewText().length()>maxLength){
					if(change.getText().length()==1){
						change = null;
					}else{
						int allowedLength = maxLength - change.getControlText().length();
						change.setText(change.getText().substring(0, allowedLength));
					}
				}
				
				if(change.getText().matches("^\\s+$")){
					change = null;
				}
				
				if(change!=null){
					if(mailTextField.isFocused()){
						if(Validation.mail(change.getControlNewText())){
							showCorrectIcon(mailTextField);
						}else if(!Validation.mail(change.getControlNewText())){
							showErrorIcon(mailTextField);
						}
					}else{
						if(!change.getControlText().equals(change.getControlNewText())){
							hideIcon(mailTextField);
						}
					}
				}
			}else if(change.isDeleted()){
					if(change.getControlNewText().length()>0){
						if(Validation.mail(change.getControlNewText())){
							showCorrectIcon(mailTextField);
						}else if(!Validation.mail(change.getControlNewText())){
							showErrorIcon(mailTextField);
						}
					}else{
						hideIcon(mailTextField);
					}
				}
			return change;
		}));
		
		messageTextArea.setTextFormatter(new TextFormatter<>(change -> {
			int maxLength = 10000;
			
			if (change.isAdded()) {
				
				if(change.getControlNewText().length()>maxLength){
					if(change.getText().length()==1){
						change = null;
					}else{
						int allowedLength = maxLength - change.getControlText().length();
						change.setText(change.getText().substring(0, allowedLength));
					}
				}
			}else if(change.isDeleted()){
				if(messageTextArea.getText()!=null){
				}
			}
			return change;
		}));
		
		
		/******************************************************************
		 * LISTENERS
		 *****************************************************************/
		
		nameTextField.focusedProperty().addListener(new FocusChangeListener(nameTextField, text -> {
			boolean valid = Validation.name(text);
			if(valid){
				currentFlat.setOwnerName(text);
				FlatDA.update(currentFlat);
			}
		}));
		
		surnameTextField.focusedProperty().addListener(new FocusChangeListener(surnameTextField, text -> {
			boolean valid = Validation.name(text);
			if(valid || text.isEmpty()){
				currentFlat.setOwnerLastName(text);
				FlatDA.update(currentFlat);
			}
		}));
		
		mailTextField.focusedProperty().addListener(new FocusChangeListener(mailTextField, text -> {
			mailInfoUpdate(CoreConstants.MAIL);
		}));
		
		areaSizeTextField.focusedProperty().addListener(new FocusChangeListener(areaSizeTextField, text -> {
			if(text.isEmpty()){
				areaSizeTextField.setText(Area.getAreaFormat());
			}else{
				areaSizeTextField.setText(Area.toString(areaSizeTextField.getText()));
			}
			currentFlat.setAreaSize(Area.toInt(areaSizeTextField.getText()));
			FlatDA.update(currentFlat);
		}, text -> {
			if(text.equals(Area.getAreaFormat())){
				areaSizeTextField.setText("");
			}
		}));
		
		numberOfPeopleTextField.focusedProperty().addListener(new FocusChangeListener(numberOfPeopleTextField, text -> {
			if(text.isEmpty()){
				numberOfPeopleTextField.setText(Integer.toString(0));
				numberOfPeopleButtonController(null);
			}
			currentFlat.setPeopleLiving(Integer.parseInt(numberOfPeopleTextField.getText()));
			FlatDA.update(currentFlat);
		}, text -> {
			if(text.equals(Integer.toString(0))){
				numberOfPeopleTextField.setText("");
			}
		}));

		sendMail.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				// TODO Auto-generated method stub
				columnViewSwitch(newValue);
			}
		});

		addOwnMessage.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
				if (newValue) {
					messageTextArea.setText(userMessage);
					messageTextArea.setEditable(true);
					messageVBox.setVisible(false);
				} else {
					userMessage = messageTextArea.getText();
					messageTextArea.setText(defaultMessage);
					messageTextArea.setEditable(false);
					messageVBox.setVisible(true);
				}
			}
		});

		ChangeListener<Boolean> sendMailActionListener = (obs, wasSelected, isNowSelected) -> {
			mailInfoUpdate("send");
		};
		sendMailListenerHandle = new ListenerHandle<>(sendMail.selectedProperty(), sendMailActionListener);

		ChangeListener<Boolean> defaultMsgActionListener = (obs, wasSelected, isNowSelected) -> {
			mailInfoUpdate("default");
		};
		defaultMsgListenerHandle = new ListenerHandle<>(addOwnMessage.selectedProperty(), defaultMsgActionListener);

		ChangeListener<Languages> languageActionListener = (obs, wasSelected, isNowSelected) -> {
			mailInfoUpdate("language");
		};
		langComboBoxListenerHandle = new ListenerHandle<>(languageComboBox.valueProperty(), languageActionListener);

		messageTextArea.focusedProperty().addListener(new ChangeListener<Boolean>() {
			private String initialValue = null;

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean focus) {
				
				if (!focus) {
					if (!messageTextArea.getText().equals(initialValue)) {
						mailInfoUpdate("message");

					}
				} else {
					initialValue = messageTextArea.getText();
				}
			}
		});

		
		/******************************************************************
		 * METHODS CALLED
		 *****************************************************************/
		
		populateBanks();
		drawPages();
		drawEmailDots();

		autoSelectFlat();

		drawPageNumbers();
		pageButtonController();
		flatButtonController();

		translateStrings();
		numberOfPeopleButtonController(null);
	}

	private void translateStrings() {
		nameLabel.setText(Word.translate("user_registration_name"));
		surnameLabel.setText(Word.translate("user_registration_last_name"));
		numberOfPeopleLabel.setText(Word.translate("number_of_people"));
		areaSizeLabel.setText(Word.translate("area_size"));
		mailLabel.setText(Word.translate("user_registration_email"));
		sendMail.setText(Word.translate("send_receipt"));
		addOwnMessage.setText(Word.translate("create_your_message"));
		languageLabel.setText(Word.translate("letter_language"));
		messageHeaderLabel.setText(Word.translate("letter_example"));
		nextFlatButton.setText(Word.translate("next"));
		previousFlatButton.setText(Word.translate("previous"));
		bankLabel.setText(Word.translate("bank"));
		areaMeasureLabel.setText(Word.translate("area_measure"));
	}
	
	
	/****************************************************************
	 * FLAT
	 ****************************************************************/
	
	/**
	 * Selects first flat without full information, otherwise selects first flat
	 */
	private void autoSelectFlat() {
		int iterator = 0;
		boolean foundFlat = false;
		while (!foundFlat && iterator < flats.size()) {
			foundFlat = !FlatInfoCheck.start(flats.get(iterator));
			if (foundFlat) {
				currentFlat = flats.get(iterator);
				pageGroup = getFlatGroup();
			}
			iterator++;
		}
		if (!foundFlat) {
			currentFlat = flats.get(0);
		}
		fillFlatInfo();
	}
	
	/**
	 * Fills current flat information into all fields.
	 */
	private void fillFlatInfo() {
		if (currentFlat != null) {
			flatLabel.setText(Word.translate("flat") + " " + currentFlat.getNumber());

			if (currentFlat.getOwnerName() != null) {
				nameTextField.setText(currentFlat.getOwnerName());
			} else {
				nameTextField.setText("");
			}
			if (currentFlat.getOwnerLastName() != null) {
				surnameTextField.setText(currentFlat.getOwnerLastName());
			} else {
				surnameTextField.setText("");
			}

			if (currentFlat.getPeopleLiving() == null) {
				numberOfPeopleTextField.setText("0");
			} else {
				numberOfPeopleTextField.setText(Integer.toString(currentFlat.getPeopleLiving()));
			}

			if (currentFlat.getAreaSize() != null) {
				areaSizeTextField.setText(Area.toString(currentFlat.getAreaSize()));
			} else {
				areaSizeTextField.setText(Area.getAreaFormat());
			}

			checkBoxListenerHandles.forEach(ListenerHandle::detach);

			for (Node node : availableBanksVBox.getChildren()) {

				CheckBox checkBox = (CheckBox) node;
				if (FlatBankDA.check(currentFlat, (Bank) checkBox.getUserData())) {
					checkBox.setSelected(true);
				} else {
					checkBox.setSelected(false);
				}

			}

			checkBoxListenerHandles.forEach(ListenerHandle::attach);

			fillEmailDots();
			selectMail(0);

			numberOfPeopleButtonController(null);

		}
	}
	
	private void columnViewSwitch(Boolean expanded){
		if (expanded) { // send
			// two column view
			HBox columnParent = (HBox) firstColumnVBox.getParent();
			firstColumnVBox.getChildren().removeAll(mailVBox, sendMail, bankVBox);
			firstColumnVBox.setAlignment(Pos.TOP_LEFT);
			firstColumnVBox.setMaxWidth(700);
			secondColumnVBox.getChildren().addAll(mailVBox, sendMail, bankVBox);
			VBox.setMargin(sendMail, new Insets(0, 0, 0, 0));
			VBox.setMargin(bankVBox, new Insets(0, 0, 0, 0));
			columnParent.getChildren().addAll(spaceRegion, secondColumnVBox);
			mainVBox.getChildren().add(4, messageContainer);
		} else { // not send
			// one column view
			HBox columnParent = (HBox) secondColumnVBox.getParent();
			secondColumnVBox.getChildren().removeAll(mailVBox, sendMail, bankVBox);
			firstColumnVBox.getChildren().addAll(mailVBox, sendMail, bankVBox);
			VBox.setMargin(sendMail, new Insets(-15, 0, 0, 0));
			VBox.setMargin(bankVBox, new Insets(-15, 0, 0, 0));
			firstColumnVBox.setAlignment(Pos.CENTER_LEFT);
			firstColumnVBox.setMaxWidth(400);
			columnParent.getChildren().removeAll(secondColumnVBox, spaceRegion);
			mainVBox.getChildren().remove(messageContainer);
		}
	}

	/**
	 * Shows list of all available banks for this house
	 */
	private void populateBanks() {

		
		ChangeListener<Boolean> listener = (obs, wasSelected, isNowSelected) -> {
			BooleanProperty booleanProperty = (BooleanProperty) obs;
			CheckBox checkBox = (CheckBox) booleanProperty.getBean();
			if (isNowSelected) {
				FlatBankDA.save(currentFlat, (Bank)checkBox.getUserData());
			} else {
				FlatBankDA.delete(currentFlat, (Bank)checkBox.getUserData());
			}

		};

		List<Bank> houseBanks = BankDA.getAll(CurrentUser.getHouse());
		if (!houseBanks.isEmpty()) {
			for (Bank bank : houseBanks) {
				CheckBox checkBox = new CheckBox();
				checkBox.setText(bank.getName() + "\n" + bank.getAccount());
				checkBox.setAlignment(Pos.TOP_LEFT);
				checkBox.setUserData(bank);

				ListenerHandle<Boolean> handle = new ListenerHandle<>(checkBox.selectedProperty(), listener);
				checkBoxListenerHandles.add(handle);

				availableBanksVBox.getChildren().add(checkBox);
			}
		}
	}

	/****************************************************************
	 * MAIL
	 ****************************************************************/
	
	private void selectMail(int index) {
		Event.fireEvent(mailNumberHBox.getChildren().get(index), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
				MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
	}

	/**
	 * Draws mail sections. Max amount - 5.
	 */
	private void drawEmailDots() {
		for (int i = 0; i < 5; i++) {
			Circle mailBackground = new Circle(5);
			mailBackground.getStyleClass().add("mail-background");

			mailBackground.setOnMouseClicked(new EventHandler<MouseEvent>() {
				public void handle(MouseEvent me) {

					Circle oldCircle = (Circle) mailNumberHBox.lookup(".selected");
					Circle newCircle = (Circle) me.getSource();

					newCircle.requestFocus();

					if (oldCircle != newCircle) {
						if (oldCircle != null) {
							oldCircle.getStyleClass().remove("selected");
						}
						newCircle.getStyleClass().add("selected");
						fillMailInfo(newCircle);
					} else {
						if (lastMailFlat != currentFlat) {
							fillMailInfo(newCircle);
						}
					}

					lastMailFlat = currentFlat;

				}// click event

			});

			mailNumberHBox.getChildren().add(mailBackground);
		}
	}

	private void fillEmailDots() {
		cleanupEmailDots();
		
		List<Mail> currentMails = new ArrayList<>();

		for (Mail mail : allMails) {
			if (mail.getFlat().getId() == currentFlat.getId()) {
				currentMails.add(mail);
			}
		}

		
		int diff = mailNumberHBox.getChildren().size() - currentMails.size();
		if (diff > 0) { // hide
			for (int i = 0; i < diff; i++) {
				Circle c = (Circle) mailNumberHBox.getChildren().get(0);
				hideDot(c);
			}
		} else { // add
			for (int i = 0; i > diff; i--) {
				showDot();
			}
		}

		if (currentMails.size() == 0) {
			mailNumberHBox.getChildren().get(0).setUserData(null);
		} else {
			for (int i = 0; i < currentMails.size(); i++) {
				mailNumberHBox.getChildren().get(i).setUserData(currentMails.get(i));
			}
		}
	}
	
	private void cleanupEmailDots(){
		for(Node node : mailNumberHBox.getChildren()){
			Mail mail = (Mail) node.getUserData();
			if(mail!=null && (mail.getMail()==null || mail.getMail().isEmpty()) && allMails.contains(mail)){
					allMails.remove(mail);
					MailDA.delete(mail);
			}
		}
	}
	
	private void fillMailInfo(Circle currentSelection) {
		mailTextField.setUserData(currentSelection.getUserData());

		if (mailTextField.getUserData() != null) {
			Mail mail = (Mail) mailTextField.getUserData();
			mailTextField.setText(mail.getMail());

			sendMailListenerHandle.detach();
			sendMail.setSelected(mail.getActive());
			sendMailListenerHandle.attach();

			if (sendMail.isSelected()) {

				defaultMsgListenerHandle.detach();
				addOwnMessage.setSelected(!mail.getDefaultMessage());
				defaultMsgListenerHandle.attach();

				if (addOwnMessage.isSelected()) {
					messageTextArea.setText(mail.getMessage());
				} else {
					for (Languages lang : languageComboBox.getItems()) {
						if (lang.getId() == mail.getLanguage().getId()) {
							langComboBoxListenerHandle.detach();
							languageComboBox.getSelectionModel().select(lang);
							langComboBoxListenerHandle.attach();

							break;
						}
					}
				}
			} else {
				defaultMsgListenerHandle.detach();
				addOwnMessage.setSelected(false);
				defaultMsgListenerHandle.attach();

				userMessage = "";

				for (Languages lang : languageComboBox.getItems()) {
					if (lang.getId() == CurrentUser.getUser().getInterfaceCode().getId()) {
						langComboBoxListenerHandle.detach();
						languageComboBox.getSelectionModel().select(lang);
						langComboBoxListenerHandle.attach();

						break;
					}
				}
			}
		} else {
			mailTextField.setText("");

			sendMailListenerHandle.detach();
			sendMail.setSelected(false);
			sendMailListenerHandle.attach();

			defaultMsgListenerHandle.detach();
			addOwnMessage.setSelected(false);
			defaultMsgListenerHandle.attach();

			userMessage = "";

			for (Languages lang : languageComboBox.getItems()) {
				if (lang.getId() == CurrentUser.getUser().getInterfaceCode().getId()) {
					langComboBoxListenerHandle.detach();
					languageComboBox.getSelectionModel().select(lang);
					langComboBoxListenerHandle.attach();

					break;
				}
			}
		}
	}
	
	private void mailInfoUpdate(String item) {

		Circle currentCircle = (Circle) mailNumberHBox.lookup(".selected");
		Mail mail = (Mail) currentCircle.getUserData();
		if (mail == null) {
			mail = new Mail(currentFlat);
		}
		switch (item) {
		case CoreConstants.MAIL:
			mail.setMail(mailTextField.getText());
			break;

		case CoreConstants.SEND:
			mail.setActive(sendMail.isSelected());
			if (mail.getDefaultMessage() == null) {
				mail.setDefaultMessage(!addOwnMessage.isSelected());
				if (mail.getDefaultMessage()) {
					mail.setLanguage(languageComboBox.getValue());
				} else {
					mail.setMessage(messageTextArea.getText());
				}
			}
			break;

		case CoreConstants.DEFAULT:
			mail.setDefaultMessage(!addOwnMessage.isSelected());
			break;

		case CoreConstants.MSG:
			mail.setMessage(messageTextArea.getText());
			break;

		case CoreConstants.LANG:
			mail.setLanguage(languageComboBox.getValue());
			break;

		}

		currentCircle.setUserData(mail);

		mailInfoCheck();
	}

	private void mailInfoCheck() {
		Circle currentCircle = (Circle) mailNumberHBox.lookup(".selected");
		Mail mail = (Mail) currentCircle.getUserData();
		if (Validation.mail(mail.getMail())) {
			if (allMails.contains(mail)) {
				System.out.println("Mail " + mail.getMail() + " already exists in database. Updating...");
				MailDA.update(mail);
			} else {
				System.out.println("Mail " + mail.getMail() + " does not exist in database. Creating...");
				MailDA.save(mail);
			}
			allMails.add(mail);
		}
	}
	
	/**
	 * Disables "Add new mail" button
	 */
	@FXML
	private void addMail() {
		addMailButton.requestFocus();

		if (!mailTextField.getText().isEmpty()) {
			showDot();

			if (mailNumberHBox.getChildren().size() >= 5) {
				addMailButton.setDisable(true);
			}
		}
		selectMail(mailNumberHBox.getChildren().size() - 1);

		mailTextField.requestFocus();
	}
	
	@FXML
	private void deleteMail(){
		Circle currentCircle = (Circle) mailNumberHBox.lookup(".selected");
		Mail mail = (Mail) currentCircle.getUserData();
		if(mail!=null){
			if (Validation.mail(mail.getMail())) {
				if (allMails.contains(mail)) {
					allMails.remove(mail);
					MailDA.delete(mail);
				}
			}
			
			fillEmailDots();
			selectMail(0);
			fillMailInfo((Circle) mailNumberHBox.lookup(".selected"));
		}else{
			System.out.println("Couldn't delete mail, because was not able to find selection!");
		}
	}

	private void showDot() {
		if(removedDots.size()>0){
			Circle dot = removedDots.get(0);
			mailNumberHBox.getChildren().add(dot);
			removedDots.remove(dot);
		}
	}

	private void hideDot(Circle dot) {
		if (mailNumberHBox.getChildren().size() > 1) {
			if (dot.getStyleClass().contains("selected")) {
				dot.getStyleClass().remove("selected");
			}
			mailNumberHBox.getChildren().remove(dot);
			dot.setUserData(null);
			removedDots.add(dot);
		}
	}

	
	/****************************************************************
	 * GENERAL
	 ****************************************************************/

	private void showCorrectIcon(TextField field) {
		Region icon = (Region) ((StackPane) field.getParent()).getChildren().get(1);
		icon.getStyleClass().clear();
		icon.getStyleClass().add("field-correct");
		icon.setMaxWidth(20);
		icon.setMaxHeight(16);
	}

	private void showErrorIcon(TextField field) {
		Region icon = (Region) ((StackPane) field.getParent()).getChildren().get(1);
		icon.getStyleClass().clear();
		icon.getStyleClass().add("field-error");
		icon.setMaxWidth(20);
		icon.setMaxHeight(20);
	}

	private void hideIcon(TextField field) {
		Region icon = (Region) ((StackPane) field.getParent()).getChildren().get(1);
		icon.getStyleClass().clear();
	}

	/**
	 * Increases number of people living in a flat.
	 */
	@FXML
	private void increasePeople() {
		int currentNumber = Integer.parseInt(numberOfPeopleTextField.getText());
		currentNumber++;
		numberOfPeopleTextField.setText(Integer.toString(currentNumber));
		currentFlat.setPeopleLiving(currentNumber);
		FlatDA.update(currentFlat);
		numberOfPeopleButtonController(null);
	}

	/**
	 * Decreases number of people living in a flat.
	 */
	@FXML
	private void decreasePeople() {
		int currentNumber = Integer.parseInt(numberOfPeopleTextField.getText());
		currentNumber--;
		numberOfPeopleTextField.setText(Integer.toString(currentNumber));
		currentFlat.setPeopleLiving(currentNumber);
		FlatDA.update(currentFlat);
		numberOfPeopleButtonController(null);
	}

	private void numberOfPeopleButtonController(String text) {
		if(text==null){
			text = numberOfPeopleTextField.getText();
		}
		if (!text.isEmpty()) {
			if (Integer.parseInt(text) <= 0) {
				lessPeopleButton.setDisable(true);
			} else {
				lessPeopleButton.setDisable(false);
			}

			if (Integer.parseInt(text) >= 100) {
				morePeopleButton.setDisable(true);
			} else {
				morePeopleButton.setDisable(false);
			}
		} else {
			lessPeopleButton.setDisable(true);
			morePeopleButton.setDisable(true);
		}
	}

	
	/****************************************************************
	 * FLAT NAVIGATION
	 ****************************************************************/
	
	/**
	 * Draws page buttons. Draws buttons just once and then they are getting
	 * reused.
	 */
	private void drawPages() {
		for (int i = 0; i < maxPagesShown; i++) {
			Region stateIndicator = new Region();
			stateIndicator.setMaxWidth(17);
			stateIndicator.setMaxHeight(17);
			stateIndicator.setMinHeight(17);
			stateIndicator.setMinWidth(17);
			StackPane.setAlignment(stateIndicator, Pos.TOP_RIGHT);

			Circle page = new Circle(17.5);
			page.getStyleClass().add("background");
			StackPane.setAlignment(page, Pos.CENTER);

			Label number = new Label();
			number.getStyleClass().add("flat-number");
			StackPane.setAlignment(number, Pos.CENTER);

			StackPane innerHolder = new StackPane();
			innerHolder.setMaxHeight(40);
			innerHolder.setMaxWidth(40);

			innerHolder.getChildren().addAll(page, number, stateIndicator);

			Circle outerCircle = new Circle(17.5);
			outerCircle.getStyleClass().add("outer-circle");
			StackPane.setAlignment(outerCircle, Pos.CENTER);

			StackPane container = new StackPane();
			container.setMaxHeight(50);
			container.setMaxWidth(50);
			container.setMinWidth(50);
			container.setMinHeight(50);
			container.setPrefWidth(40);
			container.getStyleClass().add("page");

			container.getChildren().addAll(outerCircle, innerHolder);

			container.addEventHandler(MouseEvent.MOUSE_CLICKED, clickPageEvent());

			pageContainerHBox.getChildren().add(container);
		}
	}

	/**
	 * Draws numbers of flats when group is getting changed.
	 */
	private void drawPageNumbers() {
		StackPane oldContainer = (StackPane) pageContainerHBox.lookup(".page.selected");
		if (oldContainer != null) {
			oldContainer.getStyleClass().remove("selected");
			Circle oldCircle = (Circle) oldContainer.lookup(".outer-circle");
			oldCircle.setRadius(17.5);
		}

		int startFlat = pageGroup * maxPagesShown + 1;

		for (int i = 0; i < pageContainerHBox.getChildren().size(); i++) {
			if (startFlat <= CurrentUser.getHouse().getFlats()) {
				Label number = (Label) pageContainerHBox.getChildren().get(i).lookup(".flat-number");
				number.setText(Integer.toString(startFlat));
				if (startFlat == currentFlat.getNumber()) {
					StackPane container = (StackPane) number.getParent().getParent();
					container.getStyleClass().add("selected");
					Circle circle = (Circle) container.lookup(".outer-circle");
					circle.setRadius(25);
				}
				startFlat++;
			}
		}
	}

	/**
	 * Enables/disables next/previous flat group buttons. Hides extra flat
	 * buttons from last page.
	 */
	private void pageButtonController() {
		if (pageGroup == 0) {
			previousPagesButton.getParent().setDisable(true);
		} else {
			previousPagesButton.getParent().setDisable(false);
		}

		if ((pageGroup * maxPagesShown + maxPagesShown) >= CurrentUser.getHouse().getFlats()) {
			nextPagesButton.getParent().setDisable(true);

			int mod = CurrentUser.getHouse().getFlats() % maxPagesShown;

			if (mod > 0) {
				int itemsToRemove = maxPagesShown - mod;

				for (int i = 0; i < itemsToRemove; i++) {
					pageContainerHBox.getChildren().get(maxPagesShown - (i + 1)).setVisible(false);
				}
			}

		} else {
			if (nextPagesButton.getParent().isDisabled() && (CurrentUser.getHouse().getFlats() % maxPagesShown) > 0) {
				nextPagesButton.getParent().setDisable(false);
				for (int i = 0; i < pageContainerHBox.getChildren().size(); i++) {
					pageContainerHBox.getChildren().get(i).setVisible(true);
				}
			} else if (nextPagesButton.getParent().isDisabled()) {
				nextPagesButton.getParent().setDisable(false);
			}
		}
	}

	/**
	 * Shows next group of flats
	 */
	@FXML
	private void viewNextFlats() {
		pageGroup++;
		pageButtonController();
		drawPageNumbers();
	}

	/**
	 * Shows previous group of flats
	 */
	@FXML
	private void viewPreviousFlats() {
		pageGroup--;
		pageButtonController();
		drawPageNumbers();
	}

	@FXML
	private void goToNextFlat() {
		int itemToFire = currentFlat.getNumber() % maxPagesShown;

		// int oldPageGroup = getFlatGroup(currentFlat.getNumber());
		int oldPageGroup = getFlatGroup();
		if (pageGroup != oldPageGroup) {
			pageGroup = oldPageGroup;
			pageButtonController();
			drawPageNumbers();
		}

		if (itemToFire == 0) {
			Event.fireEvent(nextPagesButton, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0, MouseButton.PRIMARY,
					1, true, true, true, true, true, true, true, true, true, true, null));
		}
		Event.fireEvent(pageContainerHBox.getChildren().get(itemToFire), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0,
				0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
	}

	@FXML
	private void goToPreviousFlat() {
		int itemToFire = (currentFlat.getNumber() - 2) % maxPagesShown;

		int oldPageGroup = getFlatGroup();
		if (pageGroup != oldPageGroup) {
			pageGroup = oldPageGroup;
			pageButtonController();
			drawPageNumbers();
		}

		if (itemToFire == 4) {
			Event.fireEvent(previousPagesButton, new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0, 0, 0,
					MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
		}
		Event.fireEvent(pageContainerHBox.getChildren().get(itemToFire), new MouseEvent(MouseEvent.MOUSE_CLICKED, 0, 0,
				0, 0, MouseButton.PRIMARY, 1, true, true, true, true, true, true, true, true, true, true, null));
	}

	private int getFlatGroup() {
		int pageGroup = (currentFlat.getNumber() - 1) / maxPagesShown;
		return pageGroup;
	}

	/**
	 * Enables/disables next/previous buttons
	 */
	private void flatButtonController() {
		if (previousFlatButton.isDisabled() && currentFlat.getNumber() > 1) {
			previousFlatButton.setDisable(false);
		} else if (currentFlat.getNumber() <= 1) {
			previousFlatButton.setDisable(true);
		}

		if (currentFlat.getNumber() >= CurrentUser.getHouse().getFlats()) {
			nextFlatButton.setDisable(true);
		} else if (currentFlat.getNumber() < CurrentUser.getHouse().getFlats() && nextFlatButton.isDisabled()) {
			nextFlatButton.setDisable(false);
		}
	}

	private EventHandler<MouseEvent> clickPageEvent() {
		return new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				StackPane source = (StackPane) event.getSource();
				HBox parent = (HBox) source.getParent();
				StackPane oldPage = (StackPane) parent.lookup(".page.selected");
				source.requestFocus(); // clears focus from all inputs

				Label numberLabel = (Label) source.lookup(".flat-number");
				int number = Integer.parseInt(numberLabel.getText());

				int iterator = 0;
				boolean foundFlat = false;
				while (!foundFlat && iterator < flats.size()) {
					if (flats.get(iterator).getNumber() == number) {
						foundFlat = true;
						currentFlat = flats.get(iterator);
						fillFlatInfo();
						flatButtonController();
					}
					iterator++;
				}

				if (oldPage != null && oldPage != source) { // new page
															// selected, old
															// deselected
					oldPage.getStyleClass().remove("selected");
					Circle oldCircle = (Circle) oldPage.lookup(".outer-circle");
					CircleTranslation circleInTranslation = new CircleTranslation(oldCircle, 17.5,
							Duration.seconds(0.15));

					source.getStyleClass().add("selected");

					Circle circle = (Circle) source.lookup(".outer-circle");

					CircleTranslation circleOutTranslation = new CircleTranslation(circle, 25, Duration.seconds(0.15));

					ParallelTransition pt = new ParallelTransition();
					pt.getChildren().addAll(circleOutTranslation, circleInTranslation);
					pt.play();
				} else if (!source.getStyleClass().contains("selected")) { // selects
																			// page
																			// for
																			// the
																			// first
																			// time

					source.getStyleClass().add("selected");

					Circle circle = (Circle) source.lookup(".outer-circle");

					CircleTranslation circleOutTranslation = new CircleTranslation(circle, 25, Duration.seconds(0.15));

					circleOutTranslation.play();
				}
			}

		};
	}
}

class CircleTranslation extends Transition {

	protected Circle circle;
	protected double startRadius;
	protected double radiusDiff;

	public CircleTranslation(Circle circle, double newRadius, Duration duration) {
		setCycleDuration(duration);
		this.circle = circle;
		this.startRadius = circle.getRadius();
		this.radiusDiff = newRadius - startRadius;
	}

	@Override
	protected void interpolate(double fraction) {
		circle.setRadius(startRadius + (radiusDiff * fraction));
	}
}
