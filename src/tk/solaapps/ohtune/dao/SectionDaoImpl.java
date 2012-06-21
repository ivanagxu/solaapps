package tk.solaapps.ohtune.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Division;
import tk.solaapps.ohtune.model.Section;

public class SectionDaoImpl extends BaseDao implements ISectionDao{

	@Override
	public boolean addSection(Section section) {
		getSession().save(section);
		return true;
	}

	@Override
	public List<Section> getAllSection() {
		List<Section> sections = getSession().createCriteria(Section.class).list();
		return sections;
	}

	@Override
	public List<Section> getSectionByDivision(Division division) {
		if(division == null)
			return new ArrayList<Section>();
		
		List<Section> sections = getSession().createCriteria(Section.class).add(Restrictions.eq("division", division.getId())).list();
		return sections;
	}

	@Override
	public Section getSectionByName(String name) {
		List<Section> sections = getSession().createCriteria(Section.class).add(Restrictions.eq("name", name)).list();
		if(sections.size() != 0)
			return sections.get(0);
		else
			return null;
	}

	@Override
	public Section getSectionById(Long id) {
		List<Section> sections = getSession().createCriteria(Section.class).add(Restrictions.eq("id", id)).list();
		if(sections.size() != 0)
			return sections.get(0);
		else
			return null;
	}
	
	@Override
	protected Class getModelClass() {
		return Section.class;
	}
}
