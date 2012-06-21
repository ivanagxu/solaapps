package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Division;
import tk.solaapps.ohtune.model.Section;

public interface ISectionDao extends IBaseDao{
	boolean addSection(Section section);
	List<Section> getAllSection();
	List<Section> getSectionByDivision(Division division);
	Section getSectionByName(String name);
	Section getSectionById(Long id);
}
