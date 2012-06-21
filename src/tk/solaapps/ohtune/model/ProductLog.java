package tk.solaapps.ohtune.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="productlog")
public class ProductLog {
	
	public static final String TYPE_FINISHED = "完成";
	public static final String TYPE_REJECTED = "返工";
	public static final String TYPE_DISUSE = "废品";
	
	private String product_name;
	private String product_our_name;
	private String section_name;
	private Integer quantity;
	private Date process_date;
	private String handled_by;
	private String process_type;
	private Long jobid;
	private Long id;
	
	@Id
	@GeneratedValue
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="product_name")
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	
	@Column(name="section_name")
	public String getSection_name() {
		return section_name;
	}
	public void setSection_name(String section_name) {
		this.section_name = section_name;
	}
	
	@Column(name="quantity")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name="process_date")
	public Date getProcess_date() {
		return process_date;
	}
	public void setProcess_date(Date process_date) {
		this.process_date = process_date;
	}
	
	@Column(name="handled_by")
	public String getHandled_by() {
		return handled_by;
	}
	public void setHandled_by(String handled_by) {
		this.handled_by = handled_by;
	}
	
	@Column(name="process_type")
	public String getProcess_type() {
		return process_type;
	}
	public void setProcess_type(String process_type) {
		this.process_type = process_type;
	}
	
	@Column(name="product_our_name")
	public String getProduct_our_name() {
		return product_our_name;
	}
	public void setProduct_our_name(String product_our_name) {
		this.product_our_name = product_our_name;
	}
	
	@Column(name="jobid")
	public Long getJobid() {
		return jobid;
	}
	public void setJobid(Long jobid) {
		this.jobid = jobid;
	}
}
