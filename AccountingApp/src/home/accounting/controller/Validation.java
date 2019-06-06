package home.accounting.controller;

public class Validation {

	public static Boolean name(String name) {
		String regx = "^[\\p{L} .'-]+$";

		if (name.matches(regx)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean phone(String phone) {
		String regx = "^(?=.{7,32}$)(\\(?\\+?[0-9]*\\)?)?[0-9_\\- \\(\\)]*((\\s?x\\s?|ext\\s?|extension\\s?)\\d{1,5}){0,1}$";

		if (phone.matches(regx)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean mail(String mail) {
		if(mail!=null && !mail.isEmpty() && !mail.equals(null)){
			String regx = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

			if (mail.matches(regx)) {
				return true;
			} else {
				return false;
			}
		}else{
			return false;
		}
	}

	public static Boolean houseNumber(String number) {
		String regex = "^\\d+/?\\d*\\p{L}?(?<!/)$";
		if (number.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean postCode(String code) {
		String regex = "[\\p{L}0-9\\s]+$";
		if (code.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean number(String number) {
		String regex = "^(?!0)[0-9]+$";
		if (number.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}

	public static Boolean whiteSpace(String string, String newChar, Boolean fullTrimming) {
		boolean allowSpace = true;

		if (fullTrimming) {
			if (newChar.matches("\\s") && Validation.acceptableInput(newChar)) {
				allowSpace = false;
			}
		} else {
			int lastCharIndex = string.length() > 0 ? (string.length() - 1) : 0;

			if (string.length() == 0 && newChar.matches("\\s")) {
				allowSpace = false;
			} else if (string.substring(lastCharIndex).equals(" ") && newChar.matches("\\s")) {
				allowSpace = false;
			}
		}

		return allowSpace;
	}

	public static Boolean checkLength(String string, int maxLength) {
		if (string.length() >= maxLength) {
			return false;
		} else {
			return true;
		}
	}
	
	public static Boolean acceptableInput(String input){
		String regex = "\\P{Cc}";
		if (input.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}
	
	public static Boolean decimalNumber(String number){
		String regex = "^\\d*\\.?\\d*$";
		
		if (number.matches(regex)) {
			return true;
		} else {
			return false;
		}
	}
}
