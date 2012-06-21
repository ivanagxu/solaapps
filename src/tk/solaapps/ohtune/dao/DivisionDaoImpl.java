package tk.solaapps.ohtune.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Department;
import tk.solaapps.ohtune.model.Division;

public class DivisionDaoImpl extends BaseDao implements IDivisionDao{

	@Override
	public List<Division> getAllDivision() {
		return getSession().createCriteria(Division.class).list();
	}

	@Override
	public boolean addDivision(Division division) {
		getSession().save(division);
		return false;
	}

	@Override
	public Division getDivisionByName(String name) {
		List<Division> divs = getSession().createCriteria(Division.class).add(Restrictions.eq("name",name)).list();
		if(divs.size() != 0)
		{
			return divs.get(0);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	public List<Division> getDivisionByDepartment(Department dept)
	{
		if(dept == null)
			return new ArrayList<Division>();
		
		List<Division> divisions = getSession().createCriteria(Division.class).add(Restrictions.eq("department",dept.getId())).list();
		return divisions;
	}

	@Override
	public Division getDivisionById(Long id) {
		List<Division> divs = getSession().createCriteria(Division.class).add(Restrictions.eq("id",id)).list();
		if(divs.size() != 0)
		{
			return divs.get(0);
		}
		else
		{
			return null;
		}
	}
	
	@Override
	protected Class getModelClass() {
		return Division.class;
	}
}
