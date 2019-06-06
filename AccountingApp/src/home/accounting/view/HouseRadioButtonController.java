package home.accounting.view;

import java.util.ArrayList;
import java.util.List;

import home.accounting.DA.HouseDA;
import home.accounting.controller.ControllerConnector;
import home.accounting.controller.Word;
import home.accounting.model.House;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

public class HouseRadioButtonController {

	@FXML
	private RadioButton radioButton;
	
	private House house;
	
	@FXML
	private Button deleteButton;
	
	@FXML
	private void initialize(){
		translateStrings();
		radioButton.selectedProperty().addListener(new ChangeListener<Boolean>() {

			@Override
			public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean isSelected) {
				// TODO Auto-generated method stub
				if(isSelected){
					selectHouse();
				}
			}});
	}
	
	private void translateStrings(){
		deleteButton.setText(Word.translate("delete"));
	}
	
	private void setText(){
		radioButton.setText(house.getStreet()+" "+house.getHouseNumber()+", "+house.getPostCode()+", "+house.getCity()+", "+house.getCountry());
	}
	
	public void setHouse(House house, ToggleGroup group){
		this.house = house;
		setText();
		radioButton.setToggleGroup(group);
		setState();
	}
	
	@FXML
	private void deleteHouse(){
		boolean wasSelected = radioButton.isSelected();
		HouseDA.delete(house);
		if(wasSelected){
			StackPane child = (StackPane) radioButton.getParent();
			VBox parent = (VBox) child.getParent();
			
			removeRadioButton();
			
			RadioButton r = (RadioButton) parent.getChildren().get(0).lookup(".radio-button");
			r.setSelected(true);
		}
		MainSideMenuController controller = ControllerConnector.getSideMenuController();
        controller.refreshHouseInfo();
	}
	
	private void selectHouse(){
		HouseDA.setDefault(house);
		MainSideMenuController controller = ControllerConnector.getSideMenuController();
        controller.refreshHouseInfo();
		System.out.println("House selected!");
	}
	
	private void removeRadioButton(){
		StackPane child = (StackPane) radioButton.getParent();
		VBox parent = (VBox) child.getParent();
		parent.getChildren().remove(child);
		radioButton.getToggleGroup().getToggles().remove(radioButton);
		child = null;
	}
	
	public void setHouse(Boolean defaultHouse){
		this.house.setDefaultHouse(defaultHouse);
	}
	
	public House getHouse(){
		return this.house;
	}
	
	public void setState(){
		radioButton.setSelected(house.getDefaultHouse());
	}
	
}
