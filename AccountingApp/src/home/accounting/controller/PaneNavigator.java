package home.accounting.controller;

public class PaneNavigator {

	public static final String registration = "RegistrationForm.fxml";
	public static final String home = "HomePage.fxml";
	public static final String sideMenu = "MainSideMenu.fxml";
	public static final String userSettings = "UserSettings.fxml";
	public static final String costs = "CostsPage.fxml";
	public static final String receipts = "ReceiptPage.fxml";
	public static final String waterList= "WaterListPage.fxml";
	public static final String changeHouse= "HouseChange.fxml";
	public static final String error = "ErrorPage.fxml";
	public static final String flatEdit = "FlatInformation.fxml";
	public static final String clear = "clear";
	public static final String pathBase = "view/";

	public static final String top = "top";
	public static final String bottom = "bottom";
	public static final String right = "right";
	public static final String left = "left";
	public static final String center = "center";
	
	private static PaneSwitcher paneSwitcher;

	public static void loadPane(String name, String pos) {
		paneSwitcher.redirect(PaneNavigator.pathBase + name, pos);
	}

	public static void setSwitcher(PaneSwitcher switcher) {
		PaneNavigator.paneSwitcher = switcher;
	}
}
