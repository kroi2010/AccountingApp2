package home.accounting.controller;

import home.accounting.model.Accountant;
import home.accounting.model.House;

public class CurrentUser {

	private static Accountant user;
	
	private static House house;

	
	public static Accountant getUser() {
		return user;
	}

	public static void setUser(Accountant user) {
		CurrentUser.user = user;
	}

	public static House getHouse() {
		return house;
	}

	public static void setHouse(House house) {
		CurrentUser.house = house;
	}
}
