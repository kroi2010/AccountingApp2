package home.accounting.controller;

import java.util.ResourceBundle;

public class Word {

	public static String capitalize(final String line) {
		   return Character.toUpperCase(line.charAt(0)) + line.substring(1);
		}
	
	public static String translate(String source){
		String result = null;
		try {
			ResourceBundle interfaceBundle = InterfaceController.getInterfaceBundle();
			result = interfaceBundle.getString(source);
		} catch (Exception e) {
			// TODO: handle exception
				System.out.println("Can't translate "+source);
		}
		return result;
	}
	
	public static String customTranslate(String source, String lang){
		InterfaceController.loadCustomLang(lang);
		String result = null;
		try {
			ResourceBundle customBundle = InterfaceController.getCustomBundle();
			result = customBundle.getString(source);
		} catch (Exception e) {
			// TODO: handle exception
				System.out.println("Can't translate "+source);
		}
		return result;
	}
	
	public static String docTranslate(String source){
		String result = null;
		try {
			ResourceBundle documentationBundle = InterfaceController.getDocumentationBundle();
			result = documentationBundle.getString(source);
		} catch (Exception e) {
			// TODO: handle exception
				System.out.println("Can't translate "+source);
		}
		return result;
	}
}
