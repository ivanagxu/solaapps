package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Department;
import tk.solaapps.ohtune.model.Division;

public interface IDivisionDao extends IBaseDao{
	List<Division> getAllDivision();
	List<Division> getDivisionByDepartment(Department dept);
	boolean addDivision(Division division);
	Division getDivisionByName(String name);
	Division getDivisionById(Long id);
}
