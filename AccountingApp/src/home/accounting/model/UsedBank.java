package home.accounting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="used_bank")
public class UsedBank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="used_bank_id")
	private int id;
	
	@Column(name="bank_name")
	private String name;
	
	@Column(name="bank_account")
	private String account;
	
	public UsedBank(){}
	
	public UsedBank(String name, String account){
		this.name = name;
		this.account = account;
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

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
}
