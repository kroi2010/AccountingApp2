package home.accounting.view;
import java.util.ResourceBundle;

import home.accounting.DA.LogInfoDA;
import home.accounting.controller.CurrentUser;
import home.accounting.controller.InterfaceController;
import home.accounting.controller.LoginController;
import home.accounting.controller.PaneNavigator;
import javafx.fxml.FXML;
import javafx.scene.control.Button;

public class UserSettingsController {
	@FXML
	private Button logOutButton;
	
	@FXML
	private void initialize() {
		translateStrings();
	}
	
	private void translateStrings(){
		try {
			ResourceBundle interfaceBundle = InterfaceController.getInterfaceBundle();
			logOutButton.setText(interfaceBundle.getString("logout_button"));
		} catch (Exception e) {
			// TODO: handle exception
				e.printStackTrace();
		}
	}
	
	@FXML
	private void logOut(){
		LoginController.logout(CurrentUser.getUser());
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.left);
		PaneNavigator.loadPane(PaneNavigator.clear, PaneNavigator.center);
		PaneNavigator.loadPane(PaneNavigator.registration, PaneNavigator.center);
	}
}
