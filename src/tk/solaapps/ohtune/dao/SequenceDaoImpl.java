package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Sequence;
import tk.solaapps.ohtune.model.UserAC;

public class SequenceDaoImpl extends BaseDao implements ISequenceDao{

	@Override
	public Sequence getNextValueByName(String name) {
		List<Sequence> seqs = getSession().createCriteria(Sequence.class)
				.add(Restrictions.eq("name",name))
				.list();
		if(seqs.size() != 0)
		{
			seqs.get(0).setValue(seqs.get(0).getValue() + 1);
			getSession().saveOrUpdate(seqs.get(0));
			return seqs.get(0);
		}
		else
		{
			return null;
		}
	}

	@Override
	protected Class getModelClass() {
		return Sequence.class;
	}
}
