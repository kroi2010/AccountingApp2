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
 * All BigDecimal will be written as int with moving dot by 2 (0.00 = 0)
 * @author aljona.gvozdeva
 *
 */
@Entity
@Table(name="costs")
public class Costs {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="cost_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="house_id")
	private House house;
	
	@Column(name="date")
	private Date date;
	
	@Column(name="maintenance")
	private int maintenance;
	
	@Column(name="heating")
	private int heating;
	
	@Column(name="garbage_collection")
	private int garbage;
	
	@Column(name="water_sewer")
	private int waterSewer;
	
	@Column(name="subscription_fees_water")
	private int subscFees;
	
	@Column(name="electricity")
	private int electricity;
	
	public Costs(){}
	public Costs(House house, Date date, int maintenance, int heating, int garbage, int water, int subsc, int electricity){
		this.house = house;
		this.garbage = garbage;
		this.date = date;
		this.maintenance = maintenance;
		this.heating = heating;
		this.waterSewer = water;
		this.subscFees = subsc;
		this.electricity = electricity;
	}
	public int getId() {
		return id;
	}
	public void setCostId(int costId) {
		this.id = costId;
	}
	public House getHouse() {
		return house;
	}
	public void setHouse(House house) {
		this.house = house;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getMaintenance() {
		return maintenance;
	}
	public void setMaintenance(int maintenance) {
		this.maintenance = maintenance;
	}
	public int getHeating() {
		return heating;
	}
	public void setHeating(int heating) {
		this.heating = heating;
	}
	public int getGarbage() {
		return garbage;
	}
	public void setGarbage(int garbage) {
		this.garbage = garbage;
	}
	public int getWaterSewer() {
		return waterSewer;
	}
	public void setWaterSewer(int waterSewer) {
		this.waterSewer = waterSewer;
	}
	public int getSubscFees() {
		return subscFees;
	}
	public void setSubscFees(int sewerCleaning) {
		this.subscFees = sewerCleaning;
	}
	public int getElectricity() {
		return electricity;
	}
	public void setElectricity(int electricity) {
		this.electricity = electricity;
	}
}
