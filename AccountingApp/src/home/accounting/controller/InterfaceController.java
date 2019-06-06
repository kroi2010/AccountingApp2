package home.accounting.controller;

import java.io.File;
import java.util.Locale;
import java.util.ResourceBundle;


public class InterfaceController {

	private static Locale interfaceLocale;
	private static Locale documentationLocale;
	private static Locale customLocale;

	private static ResourceBundle interfaceBundle;
	private static ResourceBundle documentationBundle;
	private static ResourceBundle customBundle;
	
	private static final String defaultLanguage = "en";
	
	public static void setDefaultLanguage(){
		String systemLanguage = Locale.getDefault().getLanguage();
		String languagePath = "src/home/accounting/view/lang/lang_"+systemLanguage+".properties";
		File langFile = new File(languagePath);
		if (langFile.exists()) {
			System.out.println("Language is supported");
			loadInterfaceLang(systemLanguage);
		} else {
			System.out.println("Language is not supported");
			System.out.println("Applying default language");
			loadInterfaceLang(defaultLanguage);
		}
	}

	public static void loadInterfaceLang(String lang) {
		interfaceLocale = new Locale(lang);
		interfaceBundle = ResourceBundle.getBundle("home.accounting.view.lang.lang", interfaceLocale);
	}

	public static void loadDocumentationLang(String lang) {
		documentationLocale = new Locale(lang);
		documentationBundle = ResourceBundle.getBundle("home.accounting.view.lang.lang", documentationLocale);
	}
	
	public static void loadCustomLang(String lang) {
		customLocale = new Locale(lang);
		customBundle = ResourceBundle.getBundle("home.accounting.view.lang.lang", customLocale);
	}

	public static ResourceBundle getInterfaceBundle() {
		return interfaceBundle;
	}

	public static ResourceBundle getDocumentationBundle() {
		return documentationBundle;
	}
	
	public static ResourceBundle getCustomBundle() {
		return customBundle;
	}
}
