package tk.solaapps.ohtune.pattern;

import java.util.ArrayList;
import java.util.List;

import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.Order;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.ProductLog;
import tk.solaapps.ohtune.model.ProductRate;
import tk.solaapps.ohtune.model.UserAC;

public class JsonDataWrapper {
	private int total;
	private List data;
	
	public static final int TYPE_DEFAULT = 0;
	public static final int TYPE_JOB = 1;
	public static final int TYPE_ORDER = 2;
	public static final int TYPE_JOB_TYPE = 3;
	public static final int TYPE_USER = 4;
	public static final int TYPE_PRODUCT = 5;
	public static final int TYPE_PRODUCT_RATE = 6;
	public static final int TYPE_PRODUCT_LOG = 7;
	
	public JsonDataWrapper(List data, int type)
	{
		if(data == null || data.size() == 0)
			this.total = 0;
		else 
			this.total = data.size();
		
		if(type == JsonDataWrapper.TYPE_DEFAULT)
		{
			this.data = data;
		}
		else if(type == JsonDataWrapper.TYPE_JOB)
		{
			this.data = new ArrayList<JsonJob>();
			for(int i = 0; i < data.size(); i++)
			{
				this.data.add(new JsonJob((Job)data.get(i)));
			}
		}
		else if(type == JsonDataWrapper.TYPE_ORDER)
		{
			this.data = new ArrayList<JsonOrder>();
			for(int i = 0 ; i < data.size(); i++)
			{
				this.data.add(new JsonOrder((Order)data.get(i)));
			}
		}
		else if(type == JsonDataWrapper.TYPE_JOB_TYPE)
		{
			this.data = new ArrayList<JsonJobType>();
			for(int i = 0 ; i < data.size(); i++)
			{
				this.data.add(new JsonJobType((JobType)data.get(i)));
			}
		}
		else if(type == JsonDataWrapper.TYPE_USER)
		{
			this.data = new ArrayList<JsonUser>();
			for(int i = 0 ; i < data.size(); i++)
			{
				this.data.add(new JsonUser((UserAC)data.get(i)));
			}
		}
		else if(type == JsonDataWrapper.TYPE_PRODUCT)
		{
			this.data = new ArrayList<JsonProduct>();
			for(int i = 0 ; i < data.size(); i++)
			{
				this.data.add(new JsonProduct((Product)data.get(i)));
			}
		}
		else if(type == JsonDataWrapper.TYPE_PRODUCT_RATE)
		{
			this.data = new ArrayList<JsonProductRate>();
			for(int i = 0 ; i < data.size(); i++)
			{
				this.data.add(new JsonProductRate((ProductRate)data.get(i)));
			}
		}
		else if(type == JsonDataWrapper.TYPE_PRODUCT_LOG)
		{
			this.data = new ArrayList<JsonProductLog>();
			for(int i = 0 ; i < data.size(); i++)
			{
				this.data.add(new JsonProductLog((ProductLog)data.get(i)));
			}
		}
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public List getData() {
		return data;
	}

	public void setData(List data) {
		this.data = data;
	}
	
	
}
