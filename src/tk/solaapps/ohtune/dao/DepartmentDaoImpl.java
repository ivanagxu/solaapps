package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Department;

public class DepartmentDaoImpl extends BaseDao implements IDepartmentDao{

	@Override
	public boolean addDepartment(Department dept) {
		getSession().save(dept);
		return true;
	}
	
	@Override
	public List<Department> getAllDepartment()
	{
		List<Department> depts = getSession().createCriteria(Department.class).list();
		return depts;
	}
	
	@Override
	public Department getDepartmentByName(String name)
	{
		List<Department> depts = getSession().createCriteria(Department.class).add(Restrictions.eq("name",name)).list();
		if(depts.size() != 0)
		{
			return depts.get(0);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public Department getDepartmentById(Long id)
	{
		List<Department> depts = getSession().createCriteria(Department.class).add(Restrictions.eq("id",id)).list();
		if(depts.size() != 0)
		{
			return depts.get(0);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	protected Class getModelClass() {
		return Department.class;
	}
}
