package tk.solaapps.ohtune.model;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.hibernate.Hibernate;

@Entity
@Table(name="job_types")
public class JobType {
	public static final String STATUS_ENABLE = "有效";
	public static final String STATUS_DISABLED = "无效";
	
	public static final String FINISH_DEPOT = "仓库";
	public static final String FINISH_SEMI_FINISH = "半成品";
	
	private String name;
	private Role role;
	private String status;
	private String remark;
	
	@Id
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "role")
	public Role getRole() {
		Hibernate.initialize(role);
		return role;
	}
	public void setRole(Role role) {
		this.role = role;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
