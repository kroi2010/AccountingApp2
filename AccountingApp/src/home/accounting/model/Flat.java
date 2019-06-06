package home.accounting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
/**
 * All BigDecimal will be written as int with moving dot by 1 (0.0 = 0)
 * @author aljona.gvozdeva
 *
 */
@Entity
@Table(name="flat")
public class Flat {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="flat_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="house_id")
	private House house;
	
	@Column(name="number")
	private int number;
	
	@Column(name="owner_name")
	private String ownerName;
	
	@Column(name="owner_surname")
	private String ownerLastName;
	
	@Column(name="area_size")
	private Integer areaSize;
	
	@Column(name="people_living")
	private Integer peopleLiving;
	
	@Column(name="need_mail")
	private Boolean needMail;
	
	public Flat(){}
	
	public Flat(House house, int number){
		this.house = house;
		this.number = number;
		this.needMail = false;
		this.peopleLiving = 1;
	}

	public int getId() {
		return id;
	}

	public void setId(int flatId) {
		this.id = flatId;
	}

	public House getHouse() {
		return house;
	}

	public void setHouse(House house) {
		this.house = house;
	}

	public int getNumber() {
		return number;
	}

	public void setNumber(int number) {
		this.number = number;
	}

	public String getOwnerName() {
		return ownerName;
	}

	public void setOwnerName(String ownerName) {
		this.ownerName = ownerName;
	}

	public String getOwnerLastName() {
		return ownerLastName;
	}

	public void setOwnerLastName(String ownerLastName) {
		this.ownerLastName = ownerLastName;
	}

	public Integer getAreaSize() {
		return areaSize;
	}

	public void setAreaSize(Integer areaSize) {
		this.areaSize = areaSize;
	}

	public Integer getPeopleLiving() {
		return peopleLiving;
	}

	public void setPeopleLiving(Integer peopleLiving) {
		this.peopleLiving = peopleLiving;
	}

	public Boolean getNeedMail() {
		return needMail;
	}

	public void setNeedMail(Boolean needMail) {
		this.needMail = needMail;
	}
	
}
