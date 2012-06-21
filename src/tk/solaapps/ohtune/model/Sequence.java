package tk.solaapps.ohtune.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="sequence")
public class Sequence {
	private String name;
	private Long value;
	private String remark;
	
	public static final String SEQ_NAME_DEPARTMENT = "DepartmentID";
	public static final String SEQ_NAME_DIVISION = "DivisionID";
	public static final String SEQ_NAME_SECTION = "SectionID";
	public static final String SEQ_NAME_ROLE = "RoleID";
	public static final String SEQ_NAME_POST = "PostID";
	public static final String SEQ_NAME_USERAC = "UserACID";
	public static final String SEQ_NAME_ORDER = "OrderID";
	public static final String SEQ_NAME_JOB = "JobID";
	public static final String SEQ_NAME_CUSTOMER = "CustomerID";
	
	@Id
	@Column(name="name")
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	@Column(name="value")
	public Long getValue() {
		return value;
	}
	public void setValue(Long value) {
		this.value = value;
	}
	
	@Column(name="remark")
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}
	
	
	
}
