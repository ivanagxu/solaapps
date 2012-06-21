package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;

/**
 * Servlet implementation class JobTypeController
 */
@WebServlet("/JobTypeController")
public class JobTypeController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JobTypeController() {
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
			OhtuneLogger.error("Unknow action name in OrderController");
		}
		else if(actionName.equals("getValidJobType"))
		{
			getValidJobType(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in OrderController, action name = " + actionName);
		}
	}
	
	private void getValidJobType(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		List<JobType> jobTypes = service.getAllJobType(false);
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(jobTypes, JsonDataWrapper.TYPE_JOB_TYPE);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
}
