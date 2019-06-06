package home.accounting.DA.mySchemaComparison;

import java.util.ArrayList;

public class DataMapping {
	private static ArrayList<String[]> mapping = new ArrayList<>();

	public static ArrayList<String[]> getMapping() {
		return mapping;
	}

	public static void setMapping(String oldName, String newName) {
		mapping.add(new String[]{oldName,newName});
	}
}
