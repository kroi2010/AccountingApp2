package home.accounting.controller.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Water {
	private static final String waterFormat = "0.0";

	/**
	 * Converts user input into appropriate decimal and then converts it to int for DB storage
	 * @param water
	 * @return int
	 */
	public static int toInt(String water){
		int waterInt = 0;
		try{
			BigDecimal userInput = new BigDecimal(water);
			DecimalFormat waterConverter = new DecimalFormat(waterFormat);
			String waterString = waterConverter.format(userInput);
			waterInt = Integer.parseInt(String.valueOf(new BigDecimal(waterString).movePointRight(1)));
		}catch (Exception e) {
			e.printStackTrace();
		}
		
		return waterInt;
	}
	
	/**
	 * Converts user input to appropriate String
	 * @param water - String from user input
	 * @return String
	 */
	public static String toString(String water){
		BigDecimal userInput = new BigDecimal(water);
		DecimalFormat waterConverter = new DecimalFormat(waterFormat);
		String waterString = waterConverter.format(userInput);
		return waterString;
	}
	
	/**
	 * Converts DB int into appropriate decimal and returns it as a String
	 * @param water
	 * @return String
	 */
	public static String toString(int water){
		System.out.println("TO STRING CONVERTER: "+water);
		BigDecimal userInput = new BigDecimal(water);
		DecimalFormat waterConverter = new DecimalFormat(waterFormat);
		String waterString = waterConverter.format(userInput.movePointLeft(1));
		return waterString;
	}

	public static String getwaterFormat() {
		return waterFormat;
	}
}
