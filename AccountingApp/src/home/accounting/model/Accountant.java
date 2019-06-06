package home.accounting.model;

import javax.persistence.*;

@Entity
@Table(name="accountant")
public class Accountant {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="accountant_id")
	private int id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="surname")
	private String surname;
	
	@Column(name="mail")
	private String mail;
	
	@Column(name="avatar")
	private String avatar;
	
	@Column(name="phone")
	private String phone;
	
	@Column(name="password")
	private String password;
	
	@ManyToOne
	@JoinColumn(name="interface_lang_id")
	private Languages interfaceCode;

	public Accountant(){}
	
	public Accountant(String name, String surname, String mail, String phone, Languages interfaceLang, String avatar){
		this.name = name;
		this.surname = surname;
		this.mail = mail;
		this.avatar = avatar;
		this.phone = phone;
		this.interfaceCode = interfaceLang;
		this.password = null;
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

	public String getSurname() {
		return surname;
	}

	public void setSurname(String surname) {
		this.surname = surname;
	}

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public String getAvatar() {
		return avatar;
	}

	public void setAvatar(String avatar) {
		this.avatar = avatar;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public Languages getInterfaceCode() {
		return interfaceCode;
	}

	public void setInterfaceCode(Languages interfaceCode) {
		this.interfaceCode = interfaceCode;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	
	
}
