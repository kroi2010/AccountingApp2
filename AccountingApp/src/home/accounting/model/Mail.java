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

@Entity
@Table(name="mail")
public class Mail {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="mail_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="flat_id")
	private Flat flat;
	
	@Column(name="mail")
	private String mail;
	
	@Column(name="active")
	private Boolean active;
	
	@Column(name="default_message")
	private Boolean defaultMessage;
	
	@Column(name="message")
	private String message;
	
	@ManyToOne
	@JoinColumn(name="language_id")
	private Languages language;
	
	public Mail(){}
	
	public Mail(Flat flat){
		this.flat = flat;
		this.active = false;
	}
	
	public Mail(Flat flat, String mail, Languages lang){
		this.flat = flat;
		this.mail = mail;
		this.active = true;
		this.defaultMessage = true;
		this.language = lang;
	}
	
	public Mail(Flat flat, String mail, String message){
		this.flat = flat;
		this.mail = mail;
		this.active = true;
		this.defaultMessage = false;
		this.message = message;
	}
	
	public Mail(Flat flat, String mail){
		this.flat = flat;
		this.mail = mail;
		this.active = false;
		//this.defaultMessage = false;
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

	public String getMail() {
		return mail;
	}

	public void setMail(String mail) {
		this.mail = mail;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Languages getLanguage() {
		return language;
	}

	public void setLanguage(Languages language) {
		this.language = language;
	}

	public Boolean getDefaultMessage() {
		return defaultMessage;
	}

	public void setDefaultMessage(Boolean defaultMessage) {
		this.defaultMessage = defaultMessage;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
}
