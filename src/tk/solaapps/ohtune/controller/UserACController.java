package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.Role;
import tk.solaapps.ohtune.model.Section;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;

/**
 * Servlet implementation class UserACController
 */
@WebServlet("/UserACController")
public class UserACController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;

    /**
     * Default constructor. 
     */
    public UserACController() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String actionName = request.getParameter("action");
		process(actionName, request, response);
	}

	@Override
	public void process(String actionName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		if(actionName == null || actionName.trim().equals(""))
		{
			OhtuneLogger.error("Unknow action name in UserACController");
		}
		else if(actionName.equals("login"))
		{
			login(request, response);
		}
		else if(actionName.equals("getAllUser"))
		{
			getAllUser(request, response);
		}
		else if(actionName.equals("getSessionUser"))
		{
			getSessionUser(request, response);
		}
		else if(actionName.equals("addUser"))
		{
			addUser(request, response);
		}
		else if(actionName.equals("deleteUser"))
		{
			deleteUser(request, response);
		}
		else if(actionName.equals("logout"))
		{
			logout(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in UserACController, action name = " + actionName);
		}
	}
	private void login(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String login_id = request.getParameter("login_id");
		String password = request.getParameter("password");
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		UserAC user = service.login(login_id, password);
		Gson gson = service.getGson();
		
		JsonResponse jr;
		if(user != null)
		{
			request.getSession().setAttribute("user", user);
			jr = service.genJsonResponse(true, "登入成功", user);
			OhtuneLogger.info("UserController login successfully, login_id=" + login_id);
		}
		else
		{
			jr = service.genJsonResponse(false, "登入失败,用户名或密码错误.", null);
			OhtuneLogger.info("UserController login failed, login_id=" + login_id);
		}
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	private void getAllUser(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		List<UserAC> users = service.getAllUserAC();
		JsonDataWrapper dw = new JsonDataWrapper(users, JsonDataWrapper.TYPE_USER);
		Gson gson = service.getGson();
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	private void getSessionUser(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		UserAC user = null;
		if(request.getSession().getAttribute("user") != null)
			user = (UserAC)request.getSession().getAttribute("user");
		Gson gson = service.getGson();
		JsonResponse jr = service.genJsonResponse(true, "Return session user", user);
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	
	private void addUser(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		String sName = request.getParameter("name");
		String sLogin_id = request.getParameter("login_id");
		String sPassword = request.getParameter("password");
		String sPost = request.getParameter("post");
		String sSection = request.getParameter("section");
		String sEmail = request.getParameter("email");
		String sSex = request.getParameter("sex");
		String sBirthday = request.getParameter("birthday");
		String sEmploy_date = request.getParameter("employ_date");
		Date create_date = new Date();
		Date update_date = new Date();
		String sSalary = request.getParameter("salary");
		String sStatus = request.getParameter("status");
		String sRole = request.getParameter("role");
		String sRole2 = request.getParameter("role2");

		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		JsonResponse jr = null;
		Gson gson = service.getGson();
		
		Section section = service.getSectionById(Long.parseLong(sSection));
		
		if(section == null)
		{
			jr = service.genJsonResponse(false, "添加失败, 部门不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Role role = service.getRoleById(Long.parseLong(sRole));
		Role role2 = null;
		try
		{
			 role2 = service.getRoleById(Long.parseLong(sRole2));
		}catch(Exception e)
		{
			role2 = null;
		}
		
		if(role == null)
		{
			jr = service.genJsonResponse(false, "添加失败, 角色不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Post post = service.getPostByName(sPost);
		if(post != null)
		{
			jr = service.genJsonResponse(false, "添加失败, 职位已经存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		post = new Post();
		post.setName(sPost);
		post.setSection(section);
		post.setRole(new ArrayList<Role>());
		post.getRole().add(role);
		if(role2 != null)
			post.getRole().add(role2);
		
		
		UserAC existingUser = service.getUserACByLoginId(sLogin_id);
		if(existingUser != null)
		{
			jr = service.genJsonResponse(false, "添加失败, 用户ID已经存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		UserAC user = new UserAC();
		
		try {
			Date birthday = new SimpleDateFormat("yyyy-MM-dd").parse(sBirthday);
			user.setBirthday(birthday);
		} catch (Exception e) {
			user.setBirthday(null);
		}
		
		try {
			Date employ_date = new SimpleDateFormat("yyyy-MM-dd").parse(sEmploy_date);
			user.setEmploy_date(employ_date);
		} catch (Exception e) {
			user.setEmploy_date(null);
		}
		
		user.setCreate_date(create_date);
		user.setEmail(sEmail);
		user.setLogin_id(sLogin_id);
		user.setName(sName);
		user.setPassword(sPassword);
		user.setPost(post);
		
		try
		{
			user.setSalary(Float.parseFloat(sSalary));
		}catch(Exception e)
		{
			user.setSalary(null);
		}
		user.setSex(sSex);
		user.setStatus(sStatus);
		user.setUpdate_date(update_date);
		
		boolean success = service.createUser(user, sessionUser);
		
		jr = service.genJsonResponse(success, "添加成功", null);
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	
	private void deleteUser(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		String id = request.getParameter("id");
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		UserAC user = service.getUserACById(Long.parseLong(id));
		Gson gson = service.getGson();	
		JsonResponse jr = null;
		if(user == null)
		{
			jr = service.genJsonResponse(false, "删除失败， 用户不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		else
		{
			boolean success = service.deleteUser(user, sessionUser);
			jr = service.genJsonResponse(success, success ? "删除成功" : "删除失败", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
	}
	
	private void logout(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		request.getSession().setAttribute("user", null);
		response.sendRedirect("login.jsp");
	}
}
