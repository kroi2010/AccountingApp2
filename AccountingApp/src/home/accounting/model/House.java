package home.accounting.model;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;


@Entity
@Table(name="house")
public class House {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="house_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="accountant_id")
	private Accountant accountant;
	
	@Column(name="country")
	private String country;
	
	@Column(name="city")
	private String city;
	
	@Column(name="street")
	private String street;
	
	@Column(name="post_code")
	private String postCode;
	
	@Column(name="house_number")
	private String houseNumber;
	
	@Column(name="flats")
	private int flats;
	
	@Column(name="picture")
	private String picture;
	
	@Column(name="surname_first") // order names will be written in receipt
	private Boolean writeSurnameFirst;
	
	@Column(name="cut_name") // Nado ili net sokrashat imena
	private Boolean cutName;
	
	@ManyToOne
	@JoinColumn(name="currency_id")
	private Currency currency;
	
	@ManyToOne
	@JoinColumn(name="documentation_lang_id")
	private Languages documentationLang;
	
	
	private Boolean defaultHouse;
	
	public House(){
	}
	
	public House(Accountant accountant, String country, String city, String street, String postCode, String houseNumber, int flats, String picture, Currency currency, Boolean surnamFirst, Boolean cutName, Languages docs){
		this.accountant = accountant;
		this.country = country;
		this.city = city;
		this.street = street;
		this.postCode = postCode;
		this.houseNumber = houseNumber;
		this.flats = flats;
		this.picture = picture;
		this.currency = currency;
		this.documentationLang = docs;
		this.cutName = cutName;
		this.writeSurnameFirst = surnamFirst;
		this.defaultHouse = false;
	}

	public int getId() {
		return id;
	}

	public void setId(int houseId) {
		this.id = houseId;
	}

	public Accountant getAccountant() {
		return accountant;
	}

	public void setAccountant(Accountant accountant) {
		this.accountant = accountant;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getPostCode() {
		return postCode;
	}

	public void setPostCode(String postCode) {
		this.postCode = postCode;
	}

	public String getHouseNumber() {
		return houseNumber;
	}

	public void setHouseNumber(String houseNumber) {
		this.houseNumber = houseNumber;
	}

	public int getFlats() {
		return flats;
	}

	public void setFlats(int flats) {
		this.flats = flats;
	}

	public String getPicture() {
		return picture;
	}

	public void setPicture(String picture) {
		this.picture = picture;
	}

	public Currency getCurrency() {
		return currency;
	}

	public void setCurrency(Currency currency) {
		this.currency = currency;
	}

	public Languages getDocumentationLang() {
		return documentationLang;
	}

	public void setDocumentationLang(Languages documentationLang) {
		this.documentationLang = documentationLang;
	}

	public Boolean getWriteSurnameFirst() {
		return writeSurnameFirst;
	}

	public void setWriteSurnameFirst(Boolean writeSurnameFirst) {
		this.writeSurnameFirst = writeSurnameFirst;
	}

	public Boolean getCutName() {
		return cutName;
	}

	public void setCutName(Boolean cutName) {
		this.cutName = cutName;
	}

	@Column(name="default_house")
	@Access(AccessType.PROPERTY)
	public Boolean getDefaultHouse() {
		return defaultHouse;
	}

	public void setDefaultHouse(Boolean defaultHouse) {
		this.defaultHouse = defaultHouse;
	}
	
}
