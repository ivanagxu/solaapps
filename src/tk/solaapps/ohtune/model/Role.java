package tk.solaapps.ohtune.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name="role")
public class Role {
	
	public static final String SUPERUSER_ADMIN = "管理员";
	public static final String SUPERUSER_MANAGER = "经理";
	public static final String SUPERUSER_MANAGER2 = "生产部";
	public static final String SUPERUSER_MANAGER3 = "厂长";
	
	private Long id;
	private String name;
	private Role parentRole;
	private String remark;
	
	@Id
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@ManyToOne
	@JoinColumn(name="parent_role")
	public Role getParentRole() {
		return parentRole;
	}
	public void setParentRole(Role parentRole) {
		this.parentRole = parentRole;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
}
