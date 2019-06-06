package home.accounting.controller;

import home.accounting.DA.HouseDA;
import home.accounting.DA.LogInfoDA;
import home.accounting.model.Accountant;

public class LoginController {

	/**
	 * Sets all data for user, last opened house and interface languages
	 * @param user
	 */
	public static void login(Accountant user){
		LogInfoDA.add(user);
		CurrentUser.setUser(user);
		CurrentUser.setHouse(HouseDA.getDefault(user));
		InterfaceController.loadInterfaceLang(user.getInterfaceCode().getCode());
	}
	
    public static void logout(Accountant user){
    	LogInfoDA.remove(user);
		CurrentUser.setUser(null);
		CurrentUser.setHouse(null);
		InterfaceController.setDefaultLanguage();
	}
}
