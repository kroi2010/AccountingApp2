package home.accounting.DA;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import home.accounting.DA.mySchemaComparison.DatabaseConstants;

public class SQLiteConnection {
	
	public static Connection getConnection(String pathEnd){
		String path = DatabaseConstants.getBasepath()+pathEnd+DatabaseConstants.getDbname();
		Connection connection = null;
		File dbFile = new File(path);
		if (dbFile.exists()) {
			try {
		        Class.forName("org.sqlite.JDBC");
		        connection = DriverManager.getConnection("jdbc:sqlite:" + path);
		    } catch (ClassNotFoundException | SQLException e) {
		        System.err.println(e.getClass().getName() + ": " + e.getMessage());
		    }
		}
		return connection;
	}
	
	public static String getBasePath(){
		return "src/home/accounting/DB";
	}
	  
}
