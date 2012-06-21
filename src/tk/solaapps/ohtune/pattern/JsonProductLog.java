package tk.solaapps.ohtune.pattern;

import java.util.Date;

import tk.solaapps.ohtune.model.ProductLog;

public class JsonProductLog {
	private String product_name;
	private String product_our_name;
	private String section_name;
	private Integer quantity;
	private Date process_date;
	private String handled_by;
	private String process_type;
	private Long jobid;
	private Long id;
	
	public JsonProductLog(ProductLog log)
	{
		this.product_name = log.getProduct_name();
		this.product_our_name  = log.getProduct_our_name();
		this.section_name = log.getSection_name();
		this.quantity = log.getQuantity();
		this.process_date = log.getProcess_date();
		this.handled_by = log.getHandled_by();
		this.process_type = log.getProcess_type();
		this.jobid = log.getJobid();
		this.id = log.getId();
	}
	
}
