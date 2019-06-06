package home.accounting.view;

import java.io.IOException;
import java.util.List;

import home.accounting.MainApp;
import home.accounting.DA.HouseDA;
import home.accounting.controller.CurrentUser;
import home.accounting.controller.Word;
import home.accounting.model.House;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HouseChangeController {

	@FXML
	private VBox houseListVBox;
	
	@FXML
	private AnchorPane houseSwitchAnchorPane;
	
	@FXML Button addHomeButton;
	
	@FXML
	private void initialize(){
		translateStrings();
		
		List<House> houses = HouseDA.getAll(CurrentUser.getUser());
		if(!houses.isEmpty()){
			createList(houses);
		}
	}
	
	private void translateStrings(){
		addHomeButton.setText(Word.translate("add_house"));
	}
	
	private void createList(List<House> houses){
		ToggleGroup group = new ToggleGroup();
		for(House house : houses){
			try {
				FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
		        loader.setLocation(MainApp.class.getResource("view/HouseRadioButton.fxml")); // which document
		        StackPane stackPane = (StackPane) loader.load();
				
				HouseRadioButtonController controller = loader.<HouseRadioButtonController>getController();
		        controller.setHouse(house, group);
				
		        if(houseListVBox.getChildren().size()>1 && !house.getDefaultHouse()){
		        	houseListVBox.getChildren().add(1, stackPane);
		        }else{
		        	houseListVBox.getChildren().add(0, stackPane);
		        }
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}}
	
	}
	
	@FXML
	private void addNewHouse(){
		houseListVBox.setVisible(false);
		
		try{			
			FXMLLoader loader = new FXMLLoader(); //Loads an object hierarchy from an XML document.
	        loader.setLocation(MainApp.class.getResource("view/HouseRegistration.fxml")); // which document
	        AnchorPane houseRegForm = (AnchorPane) loader.load();
	        
	        HouseRegistrationController controller = loader.<HouseRegistrationController>getController();
	        controller.initialRegistration(false);
	        
	        AnchorPane.setLeftAnchor(houseRegForm, 0.0);
	        AnchorPane.setRightAnchor(houseRegForm, 50.0);
	        AnchorPane.setTopAnchor(houseRegForm, 60.0);
	        AnchorPane.setBottomAnchor(houseRegForm, 0.0);
	        
			houseSwitchAnchorPane.getChildren().add(houseRegForm);
		}catch(Exception e){
			System.out.println("Unable to generate house registration form.");
			e.printStackTrace();
		}
	}
}
