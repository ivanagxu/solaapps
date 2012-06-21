package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.JobType;

public interface IJobTypeDao extends IBaseDao{
	List<JobType> getAllJobType(boolean includeDisabled);
	boolean updateJobType(JobType jobType);
	JobType getJobTypeByName(String name);
}
