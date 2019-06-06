package home.accounting.controller;

import home.accounting.view.ErrorPageController;
import home.accounting.view.MainSideMenuController;

public class ControllerConnector {

	private static MainSideMenuController sideMenuController = null;
	private static ErrorPageController errorPageController = null;

	public static MainSideMenuController getSideMenuController() {
		return sideMenuController;
	}

	public static void setSideMenuController(MainSideMenuController sideMenuController) {
		ControllerConnector.sideMenuController = sideMenuController;
	}

	public static ErrorPageController getErrorPageController() {
		return errorPageController;
	}

	public static void setErrorPageController(ErrorPageController errorPageController) {
		ControllerConnector.errorPageController = errorPageController;
	}
}
