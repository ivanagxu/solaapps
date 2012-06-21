package tk.solaapps.ohtune.pattern;

import tk.solaapps.ohtune.model.JobType;

public class JsonJobType {
	private String name;
	private String role;
	private String status;
	private String remark;
	
	public JsonJobType(JobType jobType)
	{
		name = jobType.getName();
		
		if(jobType.getRole() == null)
			role = "";
		else
			role = jobType.getRole().getName();
		
		status = jobType.getStatus();
		remark = jobType.getRemark();
	}
}
