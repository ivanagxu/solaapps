package tk.solaapps.ohtune.controller;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tk.solaapps.ohtune.model.OhtuneDocument;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;

/**
 * Servlet implementation class DocumentController
 */
@WebServlet("/DocumentController")
public class DocumentController extends HttpServlet implements IOhtuneController{
	

	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DocumentController() {
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
		if(actionName == null || actionName.equals(""))
		{
			OhtuneLogger.error("action name is null in DocumentController");
		}
		else if(actionName.equals("uploadDoc"))
		{
			uploadDoc(request, response);
		}
		else if(actionName.equals("getAllDocument"))
		{
			getAllDocument(request, response);
		}
		else if(actionName.equals("deleteDocument"))
		{
			deleteDocument(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in DocumentController, actionName=" + actionName);
		}
		
	}
	
	private void uploadDoc(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		
		response.setContentType("text/html");
		response.setCharacterEncoding("utf-8");
		
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");

		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();

		fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB

		fileItemFactory.setRepository(new File(service.getSystemConfig()
				.getProductImageFolder()));
		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);
		Gson gson = service.getGson();
		
		try {
			List<FileItem> items = uploadHandler.parseRequest(request);
			Iterator<FileItem> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();
				if (!item.isFormField()) {
					if(item.getFieldName().equals("file"))
					{
						String fileName = new String(item.getName().getBytes(), "utf-8");
						File file = new File(service.getSystemConfig()
								.getCommonDocumentFolder(), fileName);
						if(!file.exists())
						{
							item.write(file);
							JsonResponse jr = service.genJsonResponse(true,
									"上传文档成功", null);
							response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
							return;
						}
						else
						{
							JsonResponse jr = service.genJsonResponse(false,
									"上传文档失败， 文档已经存在", null);
							response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
							return;
						}
					}
				}
			}
			
		}catch(Exception e)
		{
			JsonResponse jr = service.genJsonResponse(false,
					"上传文档失败, 请查看日志", null);
			OhtuneLogger.error(e, "Upload document failed");
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
	}
	
	private void getAllDocument(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		
		List<OhtuneDocument> docs = service.getAllDocument(sessionUser);
		
		Gson gson = service.getGson();
		
		JsonDataWrapper dw = new JsonDataWrapper(docs, JsonDataWrapper.TYPE_DEFAULT);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	public void deleteDocument(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		String name = request.getParameter("name");
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		
		boolean success = service.deleteDocument(name, sessionUser);
		
		Gson gson = service.getGson();
		
		JsonResponse jr = service.genJsonResponse(success,
				success ? "删除文档成功" : "删除文档失败，请查看日志", null);
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
}
