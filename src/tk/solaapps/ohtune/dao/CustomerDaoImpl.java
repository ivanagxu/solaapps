package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;

public class CustomerDaoImpl extends BaseDao implements ICustomerDao{

	@Override
	public List<Customer> getAllCustomer() {
		return getSession().createCriteria(Customer.class).addOrder(Property.forName("name").asc()).list();
	}

	@Override
	public Customer getCustomerById(Long id) {
		List<Customer> customers = getSession().createCriteria(Customer.class).add(Restrictions.eq("id", id)).addOrder(Property.forName("name").asc()).list();
		
		if(customers.size() != 0)
			return customers.get(0);
		else
			return null;
	}

	@Override
	public boolean addCustomer(Customer customer) {
		getSession().save(customer);
		return true;
	}

	@Override
	public boolean updateCustomer(Customer customer) {
		getSession().saveOrUpdate(customer);
		return true;
	}
	
	@Override
	public boolean deleteCustomer(Customer customer)
	{
		getSession().delete(customer);
		return true;
	}

	@Override
	protected Class getModelClass() {
		return Customer.class;
	}
	
	
}
