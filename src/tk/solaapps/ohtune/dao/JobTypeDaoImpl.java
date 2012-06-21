package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.JobType;

public class JobTypeDaoImpl extends BaseDao implements IJobTypeDao{

	@Override
	public List<JobType> getAllJobType(boolean includeDisabled) {
		if(includeDisabled)
			return getSession().createCriteria(JobType.class).list();
		else
			return getSession().createCriteria(JobType.class).add(Restrictions.eq("status", JobType.STATUS_ENABLE)).list();
	}

	@Override
	public boolean updateJobType(JobType jobType) {
		getSession().saveOrUpdate(jobType);
		return true;
	}

	@Override
	public JobType getJobTypeByName(String name)
	{
		List<JobType> jobTypes = getSession().createCriteria(JobType.class).add(Restrictions.eq("name", name)).list();
		if(jobTypes.size() != 0)
			return jobTypes.get(0);
		else
			return null;
	}
	
	@Override
	protected Class getModelClass() {
		return JobType.class;
	}
}
