package tk.solaapps.ohtune.dao;

import java.util.Collection;
import java.util.List;

import tk.solaapps.ohtune.model.Order;

public interface IOrderDao extends IBaseDao{
	boolean addOrder(Order order);
	boolean updateOrder(Order order);
	boolean deleteOrder(Order order);
	List<Order> searchOrder(String[] columns, Object[] values, String[] inClause, Collection[] in, int start, int limit, String orderby, boolean sortAsc);
	Order getOrderById(Long id);
}
