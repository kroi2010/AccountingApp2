package home.accounting.model;

import javax.persistence.*;

@Entity
@Table(name="languages")
public class Languages {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="language_id")
	private int id;
	
	@Column(name="language_name")
	private String name;
	
	@Column(name="language_code")
	private String code;
	
	public Languages(){}
	
	public Languages(String name, String code){
		this.name = name;
		this.code = code;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}
	
	
}
