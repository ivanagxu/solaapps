package tk.solaapps.ohtune.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="orders")
public class Order {
	
	public static final String COLUMN_ID = "id";
	public static final String COLUMN_NUMBER = "number";
	public static final String COLUMN_CREATOR = "creator";
	public static final String COLUMN_PRODUCT_NAME = "product_name";
	public static final String COLUMN_REQUIREMENT_1 = "requirement_1";
	public static final String COLUMN_REQUIREMENT_2 = "requirement_2";
	public static final String COLUMN_REQUIREMENT_3 = "requirement_3";
	public static final String COLUMN_REQUIREMENT_4 = "requirement_4";
	public static final String COLUMN_CREATE_DATE = "create_date";
	public static final String COLUMN_DEADLINE = "deadline";
	public static final String COLUMN_STATUS = "status";
	public static final String COLUMN_CUSTOMER_NAME = "customer_name";
	public static final String COLUMN_CUSTOMER_CODE = "customer_code";
	public static final String COLUMN_PRODUCT_OUR_NAME = "product_our_name";
	public static final String COLUMN_QUANTITY = "quantity";
	public static final String COLUMN_USE_FINISHED = "use_finished";
	public static final String COLUMN_USE_SEMI_FINISHED = "use_semi_finished";
	public static final String COlUMN_PRODUCT_RATE = "product_rate";
	public static final String COLUMN_C_DEADLINE = "c_deadline";
	public static final String COLUMN_E_QUANTITY = "e_quantity";
	public static final String COLUMN_PRIORITY = "priority";
	
	public static final String STATUS_PROCESSING = "进行中";
	public static final String STATUS_APPROVING = "审核中";
	public static final String STATUS_FINISHED = "完成";
	public static final String STATUS_PAUSED = "暂停";
	public static final String STATUS_CANCELED = "取消";
	
	
	private Long id;
	private String number;
	private String creator;
	private String product_name;
	private String requirement_1;
	private String requirement_2;
	private String requirement_3;
	private String requirement_4;
	private Date create_date;
	private Date deadline;
	private String status;
	private String customer_name;
	private String customer_code;
	private String product_our_name;
	private Integer quantity;
	private Integer use_finished;
	private Integer use_semi_finished;
	private Float product_rate;
	private Date c_deadline;
	private Integer e_quantity;
	private Integer priority;
	
	@Id
	@Column(name="id")
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	
	@Column(name="creator")
	public String getCreator()
	{
		return creator;
	}
	public void setCreator(String creator)
	{
		this.creator = creator;
	}
	
	@Column(name="number")
	public String getNumber() {
		return number;
	}
	public void setNumber(String number) {
		this.number = number;
	}
	
	@Column(name="product_name")
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	
	@Column(name="requirement_1")
	public String getRequirement_1() {
		return requirement_1;
	}
	public void setRequirement_1(String requirement_1) {
		this.requirement_1 = requirement_1;
	}
	
	@Column(name="requirement_2")
	public String getRequirement_2() {
		return requirement_2;
	}
	public void setRequirement_2(String requirement_2) {
		this.requirement_2 = requirement_2;
	}
	
	@Column(name="requirement_3")
	public String getRequirement_3() {
		return requirement_3;
	}
	public void setRequirement_3(String requirement_3) {
		this.requirement_3 = requirement_3;
	}
	
	@Column(name="requirement_4")
	public String getRequirement_4() {
		return requirement_4;
	}
	public void setRequirement_4(String requirement_4) {
		this.requirement_4 = requirement_4;
	}
	
	@Column(name="create_date")
	public Date getCreate_date() {
		return create_date;
	}
	public void setCreate_date(Date create_date) {
		this.create_date = create_date;
	}
	
	@Column(name="deadline")
	public Date getDeadline() {
		return deadline;
	}
	public void setDeadline(Date deadline) {
		this.deadline = deadline;
	}
	
	@Column(name="status")
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	
	@Column(name="customer_name")
	public String getCustomer_name() {
		return customer_name;
	}
	public void setCustomer_name(String customer_name) {
		this.customer_name = customer_name;
	}
	
	@Column(name="customer_code")
	public String getCustomer_code() {
		return customer_code;
	}
	public void setCustomer_code(String customer_code) {
		this.customer_code = customer_code;
	}
	
	@Column(name="product_our_name")
	public String getProduct_our_name() {
		return product_our_name;
	}
	public void setProduct_our_name(String product_our_name) {
		this.product_our_name = product_our_name;
	}
	
	@Column(name="quantity")
	public Integer getQuantity() {
		return quantity;
	}
	public void setQuantity(Integer quantity) {
		this.quantity = quantity;
	}
	
	@Column(name="use_finished")
	public Integer getUse_finished() {
		return use_finished;
	}
	public void setUse_finished(Integer use_finished) {
		this.use_finished = use_finished;
	}
	
	@Column(name="use_semi_finished")
	public Integer getUse_semi_finished() {
		return use_semi_finished;
	}
	public void setUse_semi_finished(Integer use_semi_finished) {
		this.use_semi_finished = use_semi_finished;
	}
	
	@Column(name="product_rate")
	public Float getProduct_rate() {
		return product_rate;
	}
	public void setProduct_rate(Float product_rate) {
		this.product_rate = product_rate;
	}
	
	@Column(name="c_deadline")
	public Date getC_deadline() {
		return c_deadline;
	}
	public void setC_deadline(Date c_deadline) {
		this.c_deadline = c_deadline;
	}
	
	@Column(name="e_quantity")
	public Integer getE_quantity() {
		return e_quantity;
	}
	public void setE_quantity(Integer e_quantity) {
		this.e_quantity = e_quantity;
	}
	
	@Column(name="priority")
	public Integer getPriority() {
		return priority;
	}
	public void setPriority(Integer priority) {
		this.priority = priority;
	}
	
	
}
