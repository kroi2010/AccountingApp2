package home.accounting.DA.mySchemaComparison.models;

import java.util.ArrayList;

public class Table {
	private String name;
	private ArrayList<Column> columns;
	private ArrayList<Row> rows;

	public Table(String name) {
		this.name = name;
		this.columns = new ArrayList<Column>();
		this.rows = new ArrayList<Row>();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ArrayList<Column> getColumns() {
		return columns;
	}

	public void setColumns(ArrayList<Column> columns) {
		this.columns = columns;
	}
	
	public void addColumn(Column column){
		this.columns.add(column);
	}

	public ArrayList<Row> getRows() {
		return rows;
	}

	public void addRows(Row row) {
		this.rows.add(row);
	}
	
}
