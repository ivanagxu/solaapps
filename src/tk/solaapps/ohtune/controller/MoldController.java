package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Mold;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;

/**
 * Servlet implementation class MoldController
 */
@WebServlet("/MoldController")
public class MoldController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public MoldController() {
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
	public void process(String actionName, HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		if(actionName == null || actionName.equals(""))
		{
			OhtuneLogger.error("Unknow action in MoldController");
		}
		else if(actionName.equals("addMold"))
		{
			addMold(request, response);
		}
		else if(actionName.equals("deleteMold"))
		{
			deleteMold(request, response);
		}
		else if(actionName.equals("getAllMold"))
		{
			getAllMold(request, response);
		}
		else if(actionName.equals("getMoldByCode"))
		{
			getMoldByCode(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action in MoldController, action = " + actionName);
		}
	}

	private void addMold(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		String name = request.getParameter("name");
		String code = request.getParameter("code");
		String stand_no = request.getParameter("stand_no");
		
		Gson gson = service.getGson();
		JsonResponse jr = null;
		
		Mold mold = service.getMoldByCode(code);
		if(mold != null)
		{
			jr = service.genJsonResponse(false, "添加模具失败,相同模具可能已经存在", null);
		}
		else
		{
			mold = new Mold();
			mold.setName(name);
			mold.setCode(code);
			mold.setStand_no(stand_no);
			
			boolean success = service.addMold(mold);
			
			
			
			
			if(success)
			{
				jr = service.genJsonResponse(success, "添加模具成功", null);
			}
			else
			{
				jr = service.genJsonResponse(success, "添加模具失败,相同模具可能已经存在", null);
			}
		}
		
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	
	private void deleteMold(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		String code = request.getParameter("code");
		
		Gson gson = service.getGson();
		JsonResponse jr = null;
		
		Mold mold = service.getMoldByCode(code);
		
		if(mold == null)
		{
			jr = service.genJsonResponse(false, "模具不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		boolean success = service.deleteMold(mold);
		
		if(success)
		{
			jr = service.genJsonResponse(success, "删除模具成功", null);
		}
		else
		{
			jr = service.genJsonResponse(success, "删除模具失败", null);
		}
		
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	
	private void getAllMold(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		Gson gson = service.getGson();
		List<Mold> molds = service.getAllMold();
		JsonDataWrapper dw = new JsonDataWrapper(molds, JsonDataWrapper.TYPE_DEFAULT);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	private void getMoldByCode(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		String code = request.getParameter("code");
		Mold mold = service.getMoldByCode(code);
		Gson gson = service.getGson();
		response.getOutputStream().write(gson.toJson(mold).getBytes("utf-8"));
	}
}
