package home.accounting.DA.mySchemaComparison;

public class DatabaseConstants {
	private static final String basePath = "src/home/accounting/DB/";
	private static final String dbName = "Accounting.sqlite";
	private static final String mainPath = "";
	private static final String structurePath = "check/structure/";
	private static final String backupPath = "backup/";
	private static final String beforeChangeDBPath = "check/original";
	
	public static String getBasepath() {
		return basePath;
	}
	public static String getDbname() {
		return dbName;
	}
	public static String getMainpath() {
		return mainPath;
	}
	public static String getStructurepath() {
		return structurePath;
	}
	public static String getBackuppath() {
		return backupPath;
	}
	public static String getBeforechangedbpath() {
		return beforeChangeDBPath;
	}
}
