package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Section;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;

/**
 * Servlet implementation class SectionController
 */
@WebServlet("/SectionController")
public class SectionController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SectionController() {
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
			OhtuneLogger.error("Unknow action name in SectionController");
		}
		else if(actionName.equals("getAllSection"))
		{
			getAllSection(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in SectionController, action name = " + actionName);
		}
	}
	
	private void getAllSection(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		List<Section> sections = service.getAllSection();
		
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(sections, JsonDataWrapper.TYPE_DEFAULT);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
}
