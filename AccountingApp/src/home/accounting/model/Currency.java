package home.accounting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="currency")
public class Currency {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="currency_id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="symbol")
	private String symbol;
	
	@Column(name="prior")
	private Boolean startWithSymbol;
	
	public Currency(){}
	
	public Currency(String name, String symbol, Boolean start){
		this.name = name;
		this.symbol = symbol;
		this.startWithSymbol = start;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	public Boolean getStartWithSymbol() {
		return startWithSymbol;
	}

	public void setStartWithSymbol(Boolean startWithSymbol) {
		this.startWithSymbol = startWithSymbol;
	}
	
}
