package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Product;

public interface IProductDao extends IBaseDao{
	List<Product> getAllProduct(boolean includeDisabled);
	boolean updateProduct(Product product);
	boolean addProduct(Product product);
	Product getProductByName(String name);
	boolean deleteProduct(Product product);
}
