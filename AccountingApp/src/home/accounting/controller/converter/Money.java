package home.accounting.controller.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Money {
	private static final String moneyFormat = "0.00";

	public static int toInt(String money){
		BigDecimal userInput = new BigDecimal(money);
		DecimalFormat moneyConverter = new DecimalFormat(moneyFormat);
		String moneyString = moneyConverter.format(userInput);
		int moneyInt = Integer.parseInt(String.valueOf(new BigDecimal(moneyString).movePointRight(2)));
		return moneyInt;
	}
	
	public static String toString(String money){
		BigDecimal userInput = new BigDecimal(money);
		DecimalFormat moneyConverter = new DecimalFormat(moneyFormat);
		String moneyString = moneyConverter.format(userInput);
		return moneyString;
	}
	
	public static String toString(int money){
		BigDecimal userInput = new BigDecimal(money);
		DecimalFormat moneyConverter = new DecimalFormat(moneyFormat);
		String moneyString = moneyConverter.format(userInput.movePointLeft(2));
		return moneyString;
	}

	public static String getMoneyFormat() {
		return moneyFormat;
	}
	
}
