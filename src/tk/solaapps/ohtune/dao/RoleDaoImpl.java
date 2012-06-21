package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Role;

public class RoleDaoImpl extends BaseDao implements IRoleDao{

	@Override
	public boolean addRole(Role role) {
		getSession().save(role);
		return true;
	}

	@Override
	public List<Role> getAllRole() {
		return getSession().createCriteria(Role.class).list();
	}

	@Override
	public Role getRoleByName(String name) {
		List<Role> roles = getSession().createCriteria(Role.class).add(Restrictions.eq("name", name)).list();
		if(roles.size() != 0)
			return roles.get(0);
		else
			return null;
	}

	@Override
	public Role getRoleById(Long id) {
		List<Role> roles = getSession().createCriteria(Role.class).add(Restrictions.eq("id", id)).list();
		if(roles.size() != 0)
			return roles.get(0);
		else
			return null;
	}
	
	@Override
	protected Class getModelClass() {
		return Role.class;
	}
}
