package home.accounting.DA.mySchemaComparison.models;

import java.util.ArrayList;

public class Schema {
	
	private String connection;
	private ArrayList<Table> tables;
	
	public Schema(String c){
		this.connection = c;
		this.tables = new ArrayList<Table>();
	}

	public ArrayList<Table> getTables() {
		return tables;
	}

	public void setTables(ArrayList<Table> tables) {
		this.tables = tables;
	}
	
	public void addTable(Table t){
		this.tables.add(t);
	}

	public String getConnection() {
		return connection;
	}

	public void setConnection(String connection) {
		this.connection = connection;
	}
	
}
