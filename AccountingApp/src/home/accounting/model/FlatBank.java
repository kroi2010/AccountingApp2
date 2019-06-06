package home.accounting.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="flat_bank")
public class FlatBank implements Serializable{

	@Id
	@ManyToOne
	@JoinColumn(name="flat_id")
	private Flat flat;
	
	@Id
	@ManyToOne
	@JoinColumn(name="bank_id")
	private Bank bank;
	
	public FlatBank(){}
	
	public FlatBank(Flat flat, Bank bank){
		this.bank = bank;
		this.flat = flat;
	}

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	public Bank getBank() {
		return bank;
	}

	public void setBank(Bank bank) {
		this.bank = bank;
	}
	
}
