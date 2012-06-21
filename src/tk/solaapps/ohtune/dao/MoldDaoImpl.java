package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Mold;

public class MoldDaoImpl extends BaseDao implements IMoldDao{

	@Override
	public boolean addMold(Mold mold) {
		getSession().save(mold);
		return true;
	}

	@Override
	public boolean deleteMold(Mold mold) {
		getSession().delete(mold);
		return true;
	}

	@Override
	public List<Mold> getAllMold() {
		List<Mold> molds = getSession().createCriteria(Mold.class).list();
		return molds;
	}

	@Override
	public Mold getMoldByCode(String code) {
		List<Mold> molds = getSession().createCriteria(Mold.class).add(Restrictions.eq("code", code)).list();
		if(molds.size() > 0)
		{
			return molds.get(0);
		}
		else
			return null;
	}
	
	@Override
	protected Class getModelClass() {
		return Mold.class;
	}
}
