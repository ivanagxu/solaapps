package tk.solaapps.ohtune.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="molds")
public class Mold {
	private String code;
	private String name;
	private String stand_no;
	
	@Id
	@Column(name="code")
	public String getCode() {
		return code;
	}
	public void setCode(String code) {
		this.code = code;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="stand_no")
	public String getStand_no() {
		return stand_no;
	}
	public void setStand_no(String stand_no) {
		this.stand_no = stand_no;
	}
	
	
}
