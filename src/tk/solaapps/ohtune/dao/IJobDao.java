package tk.solaapps.ohtune.dao;

import java.util.Collection;
import java.util.Date;
import java.util.List;

import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.Order;

public interface IJobDao extends IBaseDao{
	boolean addJob(Job job);
	boolean updateJob(Job job);
	boolean deleteJob(Job job);
	List<Job> getJobByOrder(Order order);
	List<Job> searchJob(String[] columns, Object[] values, String[] inClause, Collection[] in, int start, int limit, String orderby, boolean sortAsc);
	Job getJobById(Long id);
	List<Job> getJobByCompDateAndSection(Date compDate, JobType jobType);
}
