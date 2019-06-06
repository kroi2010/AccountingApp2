package home.accounting.DA.mySchemaComparison.models;

import java.util.ArrayList;

public class Row {

	private ArrayList<Value> values;
	
	public Row(){
		this.values = new ArrayList<Value>();
	}

	public ArrayList<Value> getValues() {
		return values;
	}

	public void addValue(Value value) {
		this.values.add(value);
	}
	
}
