package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Customer;

public interface ICustomerDao extends IBaseDao{
	List<Customer> getAllCustomer();
	Customer getCustomerById(Long id);
	boolean addCustomer(Customer customer);
	boolean updateCustomer(Customer customer);
	boolean deleteCustomer(Customer customer);
}
