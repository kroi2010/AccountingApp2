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
 */
@Entity
@Table(name="water")
public class Water {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="water_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="flat_id")
	private Flat flat;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="amount")
	private Integer amount;		// 00.0 = 0
	
	public Water(){}
	
	public Water(Flat flat, Date date){
		this.flat = flat;
		this.date = date;
	}
	
	public Water(Flat flat, Date date, int amount){
		this.flat = flat;
		this.date = date;
		this.amount = amount;
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public Integer getAmount() {
		return amount;
	}

	public void setAmount(int amount) {
		this.amount = amount;
	}
	
}
