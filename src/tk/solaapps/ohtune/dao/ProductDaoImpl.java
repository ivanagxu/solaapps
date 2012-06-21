package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Product;

public class ProductDaoImpl extends BaseDao implements IProductDao{

	@Override
	public List<Product> getAllProduct(boolean includeDisabled) {
		if(includeDisabled)
			return getSession().createCriteria(Product.class).addOrder(Property.forName("name").asc()).list();
		else
			return getSession().createCriteria(Product.class).add(Restrictions.eq("status", Product.STATUS_ENABLE)).addOrder(Property.forName("name").asc()).list();
	}

	@Override
	public boolean updateProduct(Product product) {
		getSession().saveOrUpdate(product);
		return true;
	}
	
	@Override
	public boolean addProduct(Product product)
	{
		getSession().save(product);
		return true;
	}

	@Override
	public Product getProductByName(String name) {
		List<Product> products = getSession().createCriteria(Product.class).add(Restrictions.eq("name", name))
				.addOrder(Property.forName("name").asc()).list();
		if(products.size() == 0)
			return null;
		else
			return products.get(0);
	}
	
	@Override
	public boolean deleteProduct(Product product)
	{
		getSession().delete(product);
		return true;
	}
	
	@Override
	protected Class getModelClass() {
		return Product.class;
	}
}
