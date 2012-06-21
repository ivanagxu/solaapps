package tk.solaapps.ohtune.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Order;

public class OrderDaoImpl extends BaseDao implements IOrderDao{

	@Override
	public boolean addOrder(Order order) {
		getSession().save(order);
		return true;
	}

	@Override
	public boolean updateOrder(Order order) {
		getSession().saveOrUpdate(order);
		return true;
	}
	
	@Override
	public boolean deleteOrder(Order order)
	{
		getSession().delete(order);
		return true;
	}

	@Override
	public List<Order> searchOrder(String[] columns, Object[] values, String[] inClause, Collection[] in,
			int start, int limit, String orderby, boolean sortAsc) {
		List<Order> orders = this.search(columns, values, inClause, in, start, limit, orderby, sortAsc);
		/*
		Criteria c = getSession().createCriteria(Order.class);
		
		if(columns != null && values != null)
		{
			for(int i = 0; i < columns.length; i++)
			{
				c.add(Restrictions.eq(columns[i], values[i]));
			}
		}
		
		c.setMaxResults(limit);
		if(orderby != null && !orderby.equals(""))
		{
			c.setFirstResult(start);
			
			if(sortAsc)
				c.addOrder(Property.forName(orderby).asc());
			else
				c.addOrder(Property.forName(orderby).desc());
		}
		
		orders = c.list();
		*/
		return orders;
	}
	
	public Order getOrderById(Long id)
	{
		List<Order> orders = getSession().createCriteria(Order.class).add(Restrictions.eq("id", id)).list();
		if(orders.size() != 0)
			return orders.get(0);
		else
			return null;
	}
	
	@Override
	protected Class getModelClass() {
		return Order.class;
	}
}
