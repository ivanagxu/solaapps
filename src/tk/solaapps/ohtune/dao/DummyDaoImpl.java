package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.springframework.transaction.annotation.Transactional;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Dummy;

@Transactional
public class DummyDaoImpl extends BaseDao implements IDummyDao{
	@Override
	public List<Dummy> getAllDummy() {
		List<Dummy> dummy = 
				getSession().createCriteria(Dummy.class).list();
        return dummy;
	}
	
	@Override
	protected Class getModelClass() {
		return Dummy.class;
	}
}
