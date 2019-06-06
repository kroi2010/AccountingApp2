package home.accounting.DA.mySchemaComparison.models;

public class Value {

	String value;
	Column column;
	
	public Value(String v, Column c){
		this.value = v;
		this.column = c;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public Column getColumn() {
		return column;
	}

	public void setColumn(Column column) {
		this.column = column;
	}
}
