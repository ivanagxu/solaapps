package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Role;

public interface IRoleDao extends IBaseDao{
	boolean addRole(Role role);
	List<Role> getAllRole();
	Role getRoleByName(String name);
	Role getRoleById(Long id);
}
