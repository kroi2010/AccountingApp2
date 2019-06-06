package home.accounting.DA.mySchemaComparison.models;

public class Column {
	private String name;
	private String type;
	private String isNullable;
	private int ordinalPos;

	public Column(String name, String type, String nullable, String pos) {
		this.name = name;
		this.type = type;
		this.isNullable = nullable;
		this.ordinalPos = Integer.parseInt(pos);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getIsNullable() {
		return isNullable;
	}

	public void setIsNullable(String isNullable) {
		this.isNullable = isNullable;
	}

	public int getOrdinalPos() {
		return ordinalPos;
	}

	public void setOrdinalPos(String ordinalPos) {
		this.ordinalPos = Integer.parseInt(ordinalPos);
	}
}
