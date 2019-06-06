package home.accounting.model;

import javax.persistence.*;

@Entity
@Table(name="bank")
public class Bank {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="bank_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="house_id")
	private House house;
	
	@Column(name="bank_name")
	private String name;
	
	@Column(name="account")
	private String account;
	
	public Bank(){}
	
	public Bank(House house, String name, String account){
		this.house = house;
		this.name = name;
		this.account = account;
	}

	public int getId() {
		return id;
	}

	public void setId(int bankId) {
		this.id = bankId;
	}

	public House getHouseId() {
		return house;
	}

	public void setHouseId(House houseId) {
		this.house = houseId;
	}

	public String getName() {
		return name;
	}

	public void setName(String bankName) {
		this.name = bankName;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}
	
}
