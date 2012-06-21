package tk.solaapps.ohtune.pattern;

import java.util.Date;

import tk.solaapps.ohtune.model.Order;

public class JsonOrder {
	public Long id;
	public String number;
	public String creator;
	public String product_name;
	public String requirement_1;
	public String requirement_2;
	public String requirement_3;
	public String requirement_4;
	public Date create_date;
	public Date deadline;
	public String status;
	public String product_our_name;
	public Integer quantity;
	public Integer use_finished;
	public Integer use_semi_finished;
	public String customer_name;
	public String customer_code;
	public Float product_rate;
	public Date c_deadline;
	public Integer e_quantity;
	public Integer priority;
	public String image;
	public String drawing;
	
	
	public JsonOrder(Order order)
	{
		id = order.getId();
		number = order.getNumber();
		creator = order.getCreator();
		product_name = order.getProduct_name();
		requirement_1 = order.getRequirement_1();
		requirement_2 = order.getRequirement_2();
		requirement_3 = order.getRequirement_3();
		requirement_4 = order.getRequirement_4();
		create_date = order.getCreate_date();
		deadline = order.getDeadline();
		status = order.getStatus();
		product_our_name = order.getProduct_our_name();
		quantity = order.getQuantity();
		use_finished = order.getUse_finished();
		use_semi_finished = order.getUse_semi_finished();
		customer_name = order.getCustomer_name();
		customer_code = order.getCustomer_code();
		product_rate = order.getProduct_rate();
		c_deadline = order.getC_deadline();
		e_quantity = order.getE_quantity();
		priority = order.getPriority();
	}
}
