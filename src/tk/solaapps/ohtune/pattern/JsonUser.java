package tk.solaapps.ohtune.pattern;

import java.util.Date;

import tk.solaapps.ohtune.model.UserAC;

public class JsonUser {
	private Long id;
	private String login_id;
	private String name;
	private String password;
	private String post;
	private String department;
	private String division;
	private String section;
	private String email;
	private String sex;
	private Date birthday;
	private Date employ_date;
	private Date create_date;
	private Date update_date;
	private Float salary;
	private String status;
	private String role;
	
	public JsonUser(UserAC user)
	{
		id = user.getId();
		login_id = user.getLogin_id();
		name = user.getName();
		password = user.getPassword();
		
		if(user.getPost() == null)
			post = "";
		else
			post = user.getPost().getName();
		
		if(post.equals(""))
			section = "";
		else
		{
			if(user.getPost().getSection() == null)
				section = "";
			else
				section = user.getPost().getSection().getName();//+86 13632083890
		}	
		
		if(section.equals(""))
			division = "";
		else
		{
			if(user.getPost().getSection().getDivision() == null)
				division = "";
			else
				division = user.getPost().getSection().getDivision().getName();
		}
		
		if(division.equals(""))
			department = "";
		else
		{
			if(user.getPost().getSection().getDivision().getDepartment() == null)
				department = "";
			else
				department = user.getPost().getSection().getDivision().getDepartment().getName();
		}
		
		email = user.getEmail();
		sex = user.getSex();
		birthday = user.getBirthday();
		employ_date = user.getEmploy_date();
		create_date = user.getCreate_date();
		update_date = user.getUpdate_date();
		salary = user.getSalary();
		status = user.getStatus();
		
		role = "";
		if(post.equals(""))
			role = "";
		else
		{
			if(user.getPost().getRole() == null)
			{
				role = "";
			}
			else
			{
				for(int i = 0 ; i < user.getPost().getRole().size(); i++)
				{
					role += user.getPost().getRole().get(i).getName() + " ";
				}
			}
		}
		role = role.trim();
	}
}
