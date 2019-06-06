package home.accounting.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputEvent;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import javafx.util.Duration;

import java.io.File;
import java.util.ResourceBundle;
import java.util.Set;

import home.accounting.MainApp;
import home.accounting.DA.AccountantDA;
import home.accounting.DA.LanguagesDA;
import home.accounting.controller.InterfaceController;
import home.accounting.controller.LoginController;
import home.accounting.controller.PaneNavigator;
import home.accounting.controller.Validation;
import home.accounting.controller.Word;
import home.accounting.model.Accountant;
import home.accounting.model.Languages;
import javafx.animation.*;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;

public class RegistrationFormController {

	/******************************************************************
	 * STRINGS THAT NEED TRANSLATION
	 ******************************************************************/
	@FXML
	private Label nameLabel;

	@FXML
	private Label lastNameLabel;

	@FXML
	private Label phoneNumberLabel;

	@FXML
	private Label mailLabel;

	@FXML
	private Label houseRegistrationLabel;

	@FXML
	private Label languageLabel;
	
	@FXML
	private Label accountantNameLabel;
	
	@FXML
	private Label passwordLabel;
	
	@FXML
	private Label loginLabel;

	/******************************************************************
	 * INPUT DATA
	 ******************************************************************/
	@FXML
	private TextField name;

	@FXML
	private TextField lastName;

	@FXML
	private TextField phoneNumber;

	@FXML
	private TextField mail;

	/******************************************************************
	 * ACTION OBJECTS
	 ******************************************************************/
	@FXML
	private Region whiteBackground;
	
	@FXML
	private StackPane imageStack;

	@FXML
	private Button regButton;

	@FXML
	private Button loginButton;

	@FXML
	private GridPane grid;

	@FXML
	private VBox userForm;

	@FXML
	private VBox userRegistrationRegion;
	
	@FXML
	private VBox passwordVBox;
	
	@FXML
	private VBox loginVBox;

	/******************************************************************
	 * NEED TO POPULATE WITH DATA
	 ******************************************************************/
	
	@FXML
	private ComboBox<Accountant> userComboBox;

	@FXML
	private HBox interfaceLanguage;

	/******************************************************************
	 * PRIVATE VARIABLES
	 ******************************************************************/
	private Boolean showingUser = true;
	//private boolean moveToLeft = true;
	private ResourceBundle interfaceBundle;
	private ObservableList<Languages> languages;

	/******************************************************************
	 * STORRED DATA
	 ******************************************************************/
	private Languages iLanguage = null;

	ChangeListener<Number> moveListener = new ChangeListener<Number>() {

		@Override
		public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
			// TODO Auto-generated method stub
			
			if (imageStack.getLocalToSceneTransform().getTx() <= userRegistrationRegion.getLocalToSceneTransform()
					.getTx() && showingUser) {
				userForm.setVisible(false);
				showingUser = false;
			}
			if (imageStack.getLocalToSceneTransform().getTx() <= (int) (Math.round(5 / 100 * grid.getLayoutBounds().getWidth()))) {
				interfaceLanguage.setVisible(false);
				languageLabel.setVisible(false);
				//moveToLeft = false;
				imageStack.translateXProperty().removeListener(this);
			}
		}
	};

	@FXML
	private void initialize() {

		regButton.setDisable(true);
		houseRegistrationLabel.setVisible(false);

		/******************************************************************
		 * POPULATE INTERFACE LANGUAGES
		 *****************************************************************/
		interfaceBundle = InterfaceController.getInterfaceBundle();
		
		// Define an event handler
		EventHandler<InputEvent> languageChangeHandler = new EventHandler<InputEvent>() {
			public void handle(InputEvent event) {
				String pressedLang = ((Label) event.getSource()).getParent().getStyleClass().toString();
				languages.forEach(language -> {
					if (language.getCode().equals(pressedLang)) {
						iLanguage = language;
						InterfaceController.loadInterfaceLang(pressedLang);
						interfaceBundle = InterfaceController.getInterfaceBundle();
						translateStrings();
						setLanguageSelection();
					}
				});
				event.consume();
			}
		};

		languages = LanguagesDA.getAll();
		languages.forEach(language -> {
			String picPath = "src/home/accounting/view/styles/images/languages/" + language.getCode() + ".jpg";
			File picFile = new File(picPath);
			if (picFile.exists()) {
				StackPane stackPane = new StackPane();
				stackPane.getStyleClass().add(language.getCode());

				Circle glass = new Circle(15, 15, 15);

				Label b = new Label();
				b.setPrefHeight(30);
				b.setPrefWidth(30);
				b.getStyleClass().add("language-buttons");
				b.setStyle("-fx-background-image:url('" + picFile.toURI() + "');");
				b.setClip(glass);
				b.addEventHandler(MouseEvent.MOUSE_PRESSED, languageChangeHandler);

				Region region = new Region();
				region.setPrefHeight(40);
				region.setPrefWidth(40);
				region.setMaxHeight(40);
				region.setMaxWidth(40);
				region.getStyleClass().add("selection-indicator");

				stackPane.setAlignment(Pos.CENTER);
				stackPane.getChildren().add(region);
				stackPane.getChildren().add(b);

				if (language.getCode().equals(interfaceBundle.getLocale().toString())) {
					iLanguage = language;
				}
				
				interfaceLanguage.getChildren().add(stackPane);
			}
		});
		
		setLanguageSelection();

		/******************************************************************
		 * FILL DROP DOWN MENUS
		 *****************************************************************/
		
		ObservableList<Accountant> userItem = AccountantDA.getAll();
		userComboBox.setItems(userItem);
		Callback<ListView<Accountant>, ListCell<Accountant>> userFactory = lv -> new ListCell<Accountant>() {
			@Override
			protected void updateItem(Accountant user, boolean empty) {
				super.updateItem(user, empty);
				try{
					setText(empty ? "" : Word.capitalize(user.getName()+" "+user.getSurname()));
				}catch(Exception e){
						e.printStackTrace();
				}
			}
		};
		
		userComboBox.valueProperty().addListener(new ChangeListener<Accountant>() {
	        @Override public void changed(ObservableValue ov, Accountant oldUser, Accountant newUser) {
	          System.out.println("selected "+newUser.getName());
	          hideShowPassword(newUser);
	        }    
	    });

		userComboBox.setCellFactory(userFactory);
		userComboBox.setButtonCell(userFactory.call(null));
		userComboBox.getSelectionModel().selectFirst();

		/******************************************************************
		 * SET STRINGS
		 *****************************************************************/

		translateStrings();

		/******************************************************************
		 * SET EVENT HANDLERS
		 *****************************************************************/

		name.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		name.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(name);
				}
			}
		});

		lastName.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(50));

		lastName.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(lastName);
				}
			}
		});

		phoneNumber.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(16));

		phoneNumber.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(phoneNumber);
				}
			}
		});

		mail.addEventHandler(KeyEvent.KEY_TYPED, fieldChangeListener(100));

		mail.focusedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue,
					Boolean newPropertyValue) {
				if (!newPropertyValue) {
					trimText(mail);
				}
			}
		});
	}

	private EventHandler<KeyEvent> fieldChangeListener(final Integer max_Lengh) {
		return new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				TextField field = (TextField) event.getSource();
				String text = field.getText();
				Region icon = (Region) ((StackPane) field.getParent()).getChildren().get(1);

				System.out.println("Key got triggered + "+text.length());
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

					System.out.println(text+";");
					if (text.length() > 0) {
						boolean valid = false;

						if (field == name || field == lastName) {
							valid = Validation.name(text);
						} else if (field == phoneNumber) {
							valid = Validation.phone(text);
						} else if (field == mail) {
							valid = Validation.mail(text);
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

					disableNextStep();
				}
			}// end handle()
		};
	}
	
	@FXML
	private void showLoginForm(){
		userForm.setVisible(false);
		loginVBox.setVisible(true);
	}
	
	@FXML
	private void userLogin(){
		LoginController.login(userComboBox.getValue());
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.home, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.sideMenu, PaneNavigator.left);
	}
	
	private void hideShowPassword(Accountant userSelected){
		if(userSelected.getPassword() != null && !userSelected.getPassword().isEmpty()){
			passwordVBox.setVisible(true);
			loginButton.setTranslateY(0);
		}else{
			passwordVBox.setVisible(false);
			loginButton.setTranslateY(-100);
		}
	}

	@FXML
	private void showAddHomeForm() {
		
		Accountant accountant = new Accountant(name.getText(), lastName.getText(), mail.getText(), phoneNumber.getText(), iLanguage, null);
		AccountantDA.register(accountant);
		
	try{
			
			FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
	        loader.setLocation(MainApp.class.getResource("view/HouseRegistration.fxml")); // which document
	        AnchorPane houseRegForm = (AnchorPane) loader.load();
	        
	        HouseRegistrationController controller = loader.<HouseRegistrationController>getController();
	        controller.initialRegistration(true);
	       
			GridPane.setRowIndex(houseRegForm, 3);
			GridPane.setRowSpan(houseRegForm, 17);
			GridPane.setColumnIndex(houseRegForm, 1);
			GridPane.setColumnSpan(houseRegForm, 18);
			grid.getChildren().add(0, houseRegForm);
			
			
		}catch(Exception e){
			System.out.println("Unable to generate house registration form.");
			e.printStackTrace();
		}
		

		int newPosition = calculateHorizontalTranslate(-35, grid, imageStack, false);
		TranslateTransition transition = new TranslateTransition();
		transition.setDuration(Duration.seconds(1));
		transition.setNode(imageStack);
		transition.setToX(newPosition);
		imageStack.translateXProperty().addListener(moveListener);
		transition.setOnFinished(e -> {
			GridPane.setColumnIndex(imageStack, 0);
			GridPane.setColumnSpan(imageStack, 1);
			// AnchorPane.setRightAnchor(imageStack, null);
			imageStack.setTranslateX(0);
		});
		
		int newPositionWhite = calculateHorizontalTranslate(-60, grid, whiteBackground, false);
		TranslateTransition transitionWhite = new TranslateTransition();
		transitionWhite.setDuration(Duration.seconds(1));
		transitionWhite.setNode(whiteBackground);
		transitionWhite.setToX(newPositionWhite);
		transitionWhite.setOnFinished(e -> {
			grid.getChildren().remove(whiteBackground);
		});
		
		int newPosition2 = calculateVerticalTranslate(-90, grid, imageStack, false);
		TranslateTransition topSquarePosition = new TranslateTransition();
		topSquarePosition.setDuration(Duration.seconds(0.6));
		topSquarePosition.setNode(imageStack);
		topSquarePosition.setToY(newPosition2);
		topSquarePosition.setOnFinished(e -> {
			GridPane.setRowIndex(imageStack, 0);
			GridPane.setRowSpan(imageStack, 2);
			GridPane.setColumnSpan(imageStack, 20);
			int hidePosition = calculateHorizontalTranslate(-95, grid, imageStack, false);
			imageStack.setTranslateX(hidePosition);
			imageStack.setTranslateY(0);
			this.houseRegistrationLabel.setVisible(true);

			int newPosition3 = calculateHorizontalTranslate(0, grid, imageStack, false);
			TranslateTransition openBackground = new TranslateTransition();
			openBackground.setDuration(Duration.seconds(1));
			openBackground.setNode(imageStack);
			openBackground.setToX(newPosition3);
			openBackground.setOnFinished(me -> {
				imageStack.setTranslateX(0);
			});
			openBackground.play();
		});

		SequentialTransition sequence = new SequentialTransition(transition, topSquarePosition);
		transitionWhite.play();
		sequence.play();
	}

	private void trimText(TextField field) {
		String userName = field.getText();
		userName = userName.replaceAll("\\s+", " ").trim();
		field.setText(userName);
	}

	private void disableNextStep() {
		Set<Node> allGood = userForm.lookupAll(".field-correct");
		if(allGood.size()==4){
			regButton.setDisable(false);
		}else{
			regButton.setDisable(true);
		}
		
		//regButton.setDisable(false);
	}

	private void setLanguageSelection() {
		try {
			interfaceLanguage.getChildren().forEach(pane -> {
				Region highlight = (Region)pane.lookup(".selection-indicator");
				
				if(pane.getStyleClass().toString().equals(interfaceBundle.getLocale().toString())){
					highlight.getStyleClass().add("languages-highlight");
				}else{
					highlight.getStyleClass().remove("languages-highlight");
				}
			});
		} catch (Exception e) {
			// TODO: handle exception
			if (iLanguage != null) {
				System.out.println("No current support for " + interfaceBundle.getLocale() + " available.");
			} else {
				System.out.println(e);
			}
		}
	}
	
	private void translateStrings() {
		try {
			regButton.setText(interfaceBundle.getString("ready"));
			nameLabel.setText(interfaceBundle.getString("user_registration_name"));
			lastNameLabel.setText(interfaceBundle.getString("user_registration_last_name"));
			phoneNumberLabel.setText(interfaceBundle.getString("user_registration_phone"));
			mailLabel.setText(interfaceBundle.getString("user_registration_email"));
			houseRegistrationLabel.setText(interfaceBundle.getString("house_registration"));
			languageLabel.setText(interfaceBundle.getString("language") + ":");
			accountantNameLabel.setText(Word.translate("choose_user")+":");
			passwordLabel.setText(Word.translate("password")+":");
			loginButton.setText(Word.translate("login"));
			loginLabel.setText(Word.translate("login"));
		} catch (Exception e) {
			// TODO: handle exception
			if (iLanguage != null) {
				System.out.println("Can't translate to " + iLanguage.getName() + " language.");
			} else {
				System.out.println(e);
			}
		}
	}

	private int calculateHorizontalTranslate(double percentage, Node container, Node element, Boolean inBounds) {
		double containerWidth = container.getLayoutBounds().getWidth();
		double elementMarginLeft = element.getLayoutX();
		int newPosition = (int) (Math.round(percentage / 100 * containerWidth));
		int distance = newPosition - (int) (Math.round(elementMarginLeft));
		if (inBounds && (distance + element.getLayoutBounds().getWidth() > containerWidth)) {
			distance = (int) (containerWidth - element.getLayoutBounds().getWidth());
		} else if (inBounds && (distance - element.getLayoutBounds().getWidth() < -containerWidth)) {
			distance = -(int) (containerWidth - element.getLayoutBounds().getWidth());
		}
		return distance;
	}

	private int calculateVerticalTranslate(double percentage, Node container, Node element, Boolean inBounds) {
		double containerHeight = container.getLayoutBounds().getHeight();
		double elementMarginTop = element.getLayoutY();
		int newPosition = (int) (Math.round(percentage / 100 * containerHeight));
		int distance = newPosition - (int) (Math.round(elementMarginTop));
		if (inBounds && (distance + element.getLayoutBounds().getHeight() > containerHeight)) {
			distance = (int) (containerHeight - element.getLayoutBounds().getHeight());
		} else if (inBounds && (distance - element.getLayoutBounds().getHeight() < -containerHeight)) {
			distance = -(int) (containerHeight - element.getLayoutBounds().getHeight());
		}
		return distance;
	}

	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}

}

class TextFieldListener implements ChangeListener<Boolean> {
	private final TextField textField;

	TextFieldListener(TextField textField) {
		this.textField = textField;
	}

	@Override
	public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
		if (!newValue) // check if focus gained or lost
		{
			this.textField.setText(getFormattedText(this.textField.getText()));
		}
	}

	public String getFormattedText(String str) {
		String result = str.replaceAll("\\s+", " ").trim();
		return result;
		// return formated text
	}
}
