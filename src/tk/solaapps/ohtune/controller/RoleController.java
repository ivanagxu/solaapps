package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;

import tk.solaapps.ohtune.model.Role;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

/**
 * Servlet implementation class RoleController
 */
@WebServlet("/RoleController")
public class RoleController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RoleController() {
        super();
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
			OhtuneLogger.error("Unknow action name in RoleController");
		}
		else if(actionName.equals("getAllRole"))
		{
			getAllRole(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in RoleController, action name = " + actionName);
		}
	}
	
	private void getAllRole(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		
		List<Role> roles = service.getAllRole();
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(roles, JsonDataWrapper.TYPE_DEFAULT);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
}
