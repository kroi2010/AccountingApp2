package home.accounting.model;

import javax.persistence.*;

@Entity
@Table(name="log_info")
public class LogInfo {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="log_id")
	private int id;
	
	@OneToOne
	@JoinColumn(name="user_id")
	private Accountant user;
	
	public LogInfo(){}
	
	public LogInfo(Accountant user){
		this.user = user;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Accountant getUser() {
		return user;
	}

	public void setUser(Accountant user) {
		this.user = user;
	}
	
	
}
