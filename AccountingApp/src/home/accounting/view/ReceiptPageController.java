package home.accounting.view;

import java.util.List;

import home.accounting.DA.FlatDA;
import home.accounting.controller.ControllerConnector;
import home.accounting.controller.CoreConstants;
import home.accounting.controller.CurrentUser;
import home.accounting.controller.FlatInfoCheck;
import home.accounting.controller.PaneNavigator;
import home.accounting.controller.Word;
import home.accounting.model.Flat;
import javafx.fxml.FXML;

public class ReceiptPageController {

	private List<Flat> flatList;
	@FXML
	private void initialize() {
	}
	
	
	public void checkForRedirect(){
		flatList = FlatDA.getAll(CurrentUser.getHouse());
		if(flatList.isEmpty() || flatList==null || !FlatInfoCheck.all(flatList)){
			PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
			PaneNavigator.loadPane(PaneNavigator.error, PaneNavigator.center);
			ErrorPageController controller = ControllerConnector.getErrorPageController();
			controller.setupPage(CoreConstants.MISSING_FLATS_ERR);
		}else{
			populateFlats();
		}
	}
	
	private void populateFlats(){
		for(int i=0; i<CurrentUser.getHouse().getFlats(); i++){
			
		}
	}
}
