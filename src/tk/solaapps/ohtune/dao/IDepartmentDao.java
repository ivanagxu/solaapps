package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Department;
import tk.solaapps.ohtune.model.Division;

public interface IDepartmentDao extends IBaseDao{
	boolean addDepartment(Department dept);
	List<Department> getAllDepartment();
	Department getDepartmentByName(String name);
	Department getDepartmentById(Long id);
}
