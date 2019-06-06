package home.accounting.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name="sending_history")
public class SendingHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="sent_id")
	private int id;
	
	@ManyToOne
	@JoinColumn(name="flat_id")
	private Flat flat;
	
	@Column(name="mail")
	private String mail;
	
	@Column(name="message")
	private String message;
	
	@OneToOne
	@JoinColumn(name="receipt_id")
	private Receipt receipt;
	
	public SendingHistory(){}
	
	public SendingHistory(Flat flat, String mail, String message, Receipt receipt){
		this.flat = flat;
		this.mail = mail;
		this.message = message;
		this.receipt = receipt;
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

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Receipt getReceipt() {
		return receipt;
	}

	public void setReceipt(Receipt receipt) {
		this.receipt = receipt;
	}
	
}
