package home.accounting.model;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="receipt_used_bank")
public class ReceiptUsedBank implements Serializable{

	@Id
	@ManyToOne
	@JoinColumn(name="receipt_id")
	private Receipt receipt;
	
	@Id
	@ManyToOne
	@JoinColumn(name="used_bank_id")
	private UsedBank usedBank;
	
	public ReceiptUsedBank(){}
	
	public ReceiptUsedBank(Receipt receipt, UsedBank usedBank){
		this.usedBank = usedBank;
		this.receipt = receipt;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}

	public UsedBank getUsedBank() {
		return usedBank;
	}

	public void setUsedBank(UsedBank usedBank) {
		this.usedBank = usedBank;
	}
	
}
