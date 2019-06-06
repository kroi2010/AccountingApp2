package home.accounting.view;

import home.accounting.MainApp;
import home.accounting.controller.CurrentUser;
import home.accounting.controller.Word;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

public class HomePageController {
	@FXML
	private VBox houseAddressVBox;
	
	@FXML
	private GridPane stickyNotesGridPane;
	
	@FXML
	private Label houseNumLabel;
	
	@FXML
	private Label houseAddressLabel;
	
	@FXML
	private Label dateLabel;
	
	@FXML
	private Label dayLabel;
	
	@FXML
	private Label monthLabel;
	
	@FXML
	private Button houseRegistrationButton;
	
	@FXML
	private AnchorPane homePageContainer;
	
	@FXML
	private VBox homePageVBox;
	
	private AnchorPane houseRegForm = null;

	@FXML
	private void initialize() {
		translateStrings();
		
		if(CurrentUser.getHouse()==null){
			houseAddressVBox.getChildren().removeAll(houseAddressLabel);
			houseNumLabel.setText(Word.translate("no_house_yet"));
			((VBox)stickyNotesGridPane.getParent()).getChildren().remove(stickyNotesGridPane);
		}else{
			houseAddressVBox.getChildren().remove(houseRegistrationButton);
			houseNumLabel.setText(CurrentUser.getHouse().getStreet()+" "+CurrentUser.getHouse().getHouseNumber());
			houseAddressLabel.setText(CurrentUser.getHouse().getPostCode()+", "+CurrentUser.getHouse().getCity()+", "+CurrentUser.getHouse().getCountry());
		}
	}
	
	private void translateStrings(){
		houseRegistrationButton.setText(Word.translate("house_registration"));
	}
	
	@FXML
	private void showHouseRegistrationForm(){
		homePageVBox.setVisible(false);
		
		try{			
			FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
	        loader.setLocation(MainApp.class.getResource("view/HouseRegistration.fxml")); // which document
	        houseRegForm = (AnchorPane) loader.load();
	        
	        HouseRegistrationController controller = loader.<HouseRegistrationController>getController();
	        controller.initialRegistration(false);
	        
	        AnchorPane.setLeftAnchor(houseRegForm, 0.0);
	        AnchorPane.setRightAnchor(houseRegForm, 50.0);
	        AnchorPane.setTopAnchor(houseRegForm, 60.0);
	        AnchorPane.setBottomAnchor(houseRegForm, 0.0);
	        
			homePageContainer.getChildren().add(houseRegForm);
		}catch(Exception e){
			System.out.println("Unable to generate house registration form.");
			e.printStackTrace();
		}
	}
}
