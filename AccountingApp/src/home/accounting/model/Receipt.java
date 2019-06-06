package home.accounting.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * BigDecimal to int conversion:
 * Money: 00.00 = 0
 * Water: 00.0 = 0
 * Area: 00.0 =0
 * @author aljona.gvozdeva
 *
 */
@Entity
@Table(name="receipt")
public class Receipt {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="receipt_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="flat_id")
	private Flat flat;
	
	@Column(name="accountant")
	private String accountant;
	
	@Column(name="owner_name")
	private String owner;
	
	@Column(name="people")
	private int people;
	
	@Column(name="area_size")
	private int areaSize;   // 0.0 = 0
	
	@Column(name="water")
	private int water;		// 0.0 = 0
	
	@Column(name="date")
	private Date date;
	
	@Column(name="deadline")
	private Date deadline;
	
	@Column(name="debt")
	private int debt;  		// 0.00 = 0
	
	@Column(name="empty")
	private Boolean empty;
	
	@Column(name="sent")
	private Date sent;
	
	public Receipt(){}
	
	public Receipt(Flat flat, String accountant, String owner, int people, int areaSize, int water, Date date, Date deadline, int debt, Date sent){
		this.flat = flat;
		this.accountant = accountant;
		this.owner = owner;
		this.areaSize = areaSize;
		this.people = people;
		this.water = water;
		this.date = date;
		this.deadline = deadline;
		this.debt = debt;
		this.sent = sent;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Flat getFlat() {
		return flat;
	}

	public void setFlat(Flat flat) {
		this.flat = flat;
	}

	public String getAccountant() {
		return accountant;
	}

	public void setAccountant(String accountant) {
		this.accountant = accountant;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getPeople() {
		return people;
	}

	public void setPeople(int people) {
		this.people = people;
	}

	public int getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(int areaSize) {
		this.areaSize = areaSize;
	}

	public int getWater() {
		return water;
	}

	public void setWater(int water) {
		this.water = water;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Date getDeadline() {
		return deadline;
	}

	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}

	public int getDebt() {
		return debt;
	}

	public void setDebt(int debt) {
		this.debt = debt;
	}

	public Date getSent() {
		return sent;
	}

	public void setSent(Date sent) {
		this.sent = sent;
	}
	
	public Boolean getEmpty() {
		return empty;
	}

	public void setEmpty(Boolean empty) {
		this.empty = empty;
	}
}
