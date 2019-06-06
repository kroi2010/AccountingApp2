package home.accounting.controller.converter;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class Area {
	private static final String areaFormat = "0.0";

	/**
	 * Converts user input into appropriate decimal and then converts it to int for DB storage
	 * @param area
	 * @return int
	 */
	public static int toInt(String area){
		BigDecimal userInput = new BigDecimal(area);
		DecimalFormat areaConverter = new DecimalFormat(areaFormat);
		String areaString = areaConverter.format(userInput);
		int areaInt = Integer.parseInt(String.valueOf(new BigDecimal(areaString).movePointRight(1)));
		return areaInt;
	}
	
	/**
	 * Converts user input to appropriate String
	 * @param area - String from user input
	 * @return String
	 */
	public static String toString(String area){
		if(area.equals(".")){
			return areaFormat;
		}else{
			BigDecimal userInput = new BigDecimal(area);
			DecimalFormat areaConverter = new DecimalFormat(areaFormat);
			String areaString = areaConverter.format(userInput);
			return areaString;
		}
	}
	
	/**
	 * Converts DB int into appropriate decimal and returns it as a String
	 * @param area
	 * @return String
	 */
	public static String toString(int area){
		BigDecimal userInput = new BigDecimal(area);
		DecimalFormat areaConverter = new DecimalFormat(areaFormat);
		String areaString = areaConverter.format(userInput.movePointLeft(1));
		return areaString;
	}

	public static String getAreaFormat() {
		return areaFormat;
	}
}
