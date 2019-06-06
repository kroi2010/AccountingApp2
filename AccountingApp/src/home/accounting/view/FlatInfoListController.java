package home.accounting.view;

import java.util.ArrayList;
import java.util.List;

import home.accounting.DA.FlatDA;
import home.accounting.controller.CurrentUser;
import home.accounting.model.Flat;
import javafx.fxml.FXML;
import javafx.scene.layout.HBox;

public class FlatInfoListController {
	@FXML
	private HBox pageContainerHBox;
	
	private List<Flat> flatList = new ArrayList<Flat>();
	@FXML
	private void initialize(){
		
	}
	
	private void populatePages(){
		int flats = CurrentUser.getHouse().getFlats();
		List<Flat> existingFlats = FlatDA.getAll(CurrentUser.getHouse());
		if(existingFlats!=null && !existingFlats.isEmpty()){
			for(int i = 0; i<flats; i++){
				boolean foundFlat = false;
				int flatLoop = 0;
				while(!foundFlat && flatLoop<existingFlats.size()){
					if(existingFlats.get(flatLoop).getNumber()==i+1){
						foundFlat = true;
					}
				}
				if(!foundFlat){
					//flatList.add(new Flat(i+1));
				}
			}
		}else{
			for(int i = 0; i<flats; i++){
				//flatList.add(new Flat(i+1));
			}
		}
		
	}
}
