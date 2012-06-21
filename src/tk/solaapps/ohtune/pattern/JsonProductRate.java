package tk.solaapps.ohtune.pattern;

import tk.solaapps.ohtune.model.ProductRate;

public class JsonProductRate {
	
	private String order_number;
	private String product_name;
	private String product_our_name;
	private String rate;
	private String remark;
	public JsonProductRate(ProductRate rate)
	{
		if(rate.getOrder() == null)
			this.order_number = "";
		else
			this.order_number = rate.getOrder().getNumber();
		
		if(rate.getProduct() == null)
		{
			this.product_name = "";
			this.product_our_name = "";
		}
		else
		{
			this.product_name = rate.getProduct().getName();
			this.product_our_name = rate.getProduct().getOur_name();
		}
		this.rate = rate.getRate();
		this.remark = rate.getOrder() == null ? "" : rate.getOrder().getRequirement_4();
	}
}
