package tk.solaapps.ohtune.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import tk.solaapps.ohtune.model.Mold;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.JsonProduct;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;
import tk.solaapps.ohtune.util.UtilityFunc;

import com.google.gson.Gson;

/**
 * Servlet implementation class ProductController
 */
@WebServlet("/ProductController")
public class ProductController extends HttpServlet implements IOhtuneController {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ProductController() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String actionName = request.getParameter("action");
		process(actionName, request, response);
	}

	@Override
	public void process(String actionName, HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		if (actionName == null || actionName.equals("")) {
			OhtuneLogger.error("Unknow action name in ProductController");
		} else if (actionName.equals("getValidProduct")) {
			getValidProduct(request, response);
		} else if (actionName.equals("getAllProduct")){
			getAllProduct(request, response);
		} else if (actionName.equals("addProduct")) {
			addProduct(request, response);
		} else if (actionName.equals("getProductImage"))
			getProductImage(request, response);
		else if (actionName.equals("getProductDrawing"))
			getProductDrawing(request, response);
		else if (actionName.equals("deleteProduct"))
			deleteProduct(request, response);
		else if (actionName.equals("updateProduct"))
			updateProduct(request, response);
		else if (actionName.equals("getProductByName"))
			getProductByName(request, response);
		else if (actionName.equals("sellProduct"))
			sellProduct(request, response);
		else {
			OhtuneLogger
					.error("Unknow action name in ProductController, action name="
							+ actionName);
		}

	}
	
	private void getProductByName(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		String name = request.getParameter("name");
		JsonProduct product = new JsonProduct(service.getProductByName(name));
		Gson gson = service.getGson();
		response.getOutputStream().write(gson.toJson(product).getBytes("utf-8"));
	}

	private void getValidProduct(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		List<Product> products = service.getAllProduct(false);
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(products,
				JsonDataWrapper.TYPE_PRODUCT);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	private void getAllProduct(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		List<Product> products = service.getAllProduct(true);
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(products,
				JsonDataWrapper.TYPE_PRODUCT);
		UtilityFunc.fillImageDrawingForProduct(dw.getData(), service);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	private void updateProduct(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");

		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();

		fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB

		fileItemFactory.setRepository(new File(service.getSystemConfig()
				.getProductImageFolder()));
		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);

		try {

			Product product = new Product();
			product.setImage("");
			product.setDrawing("");
			boolean updateImage = false;
			boolean updateDrawing = false;
			
			List<FileItem> items = uploadHandler.parseRequest(request);

			Iterator<FileItem> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();

				if (item.isFormField()) {
					if(item.getFieldName().equals("name"))
						product.setName(item.getString("utf-8"));
					else if(item.getFieldName().equals("name_eng"))
						product.setName_eng(item.getString("utf-8"));
					else if(item.getFieldName().equals("status"))
						product.setStatus(item.getString("utf-8"));
					else if(item.getFieldName().equals("our_name"))
						product.setOur_name(item.getString("utf-8"));
					else if(item.getFieldName().equals("mold_rate"))
						product.setMold_rate(item.getString("utf-8"));
					else if(item.getFieldName().equals("machining_pos"))
						product.setMachining_pos(item.getString("utf-8"));
					else if(item.getFieldName().equals("handwork_pos"))
						product.setHandwork_pos(item.getString("utf-8"));
					else if(item.getFieldName().equals("polishing"))
						product.setPolishing(item.getString("utf-8"));
					else if(item.getFieldName().equals("mold_code"))
					{
						Mold mold = new Mold();
						mold.setCode(item.getString("utf-8"));
						product.setMold(mold);
					}
					else if(item.getFieldName().equals("finished"))
					{
						try
						{
							product.setFinished(Integer.parseInt(item.getString("utf-8")));
						}catch(Exception e)
						{
							product.setFinished(0);
						}
					}
					else if(item.getFieldName().equals("semi_finished"))
					{
						try
						{
							product.setSemi_finished(Integer.parseInt(item.getString("utf-8")));
						}catch(Exception e)
						{
							product.setSemi_finished(0);
						}
					}
				}

				else {
					try
					{
						String fileName = new String(item.getName().getBytes(), "utf-8");
						long size = item.getSize();
						fileName = System.currentTimeMillis() + "." +  fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
						if(item.getFieldName().equals("image") && size > 0)
						{
							File file = new File(service.getSystemConfig()
									.getProductImageFolder(), fileName);
							item.write(file);
							product.setImage(file.getAbsolutePath());
							updateImage = true;
							try
							{
								UtilityFunc.compressImage(1024, 768, file.getAbsolutePath());
							}
							catch(Exception e)
							{
								OhtuneLogger.error(e, "Compress image :" + file.getAbsolutePath() + " failed");
							}
						}
						else if(item.getFieldName().equals("drawing") && size > 0)
						{	
							File file = new File(service.getSystemConfig()
									.getProductDrawingFolder(), fileName);
							item.write(file);
							product.setDrawing(file.getAbsolutePath());
							updateDrawing = true;
							try
							{
								UtilityFunc.compressImage(1024, 768, file.getAbsolutePath());
							}
							catch(Exception e)
							{
								OhtuneLogger.error(e, "Compress image :" + file.getAbsolutePath() + " failed");
							}
						}
					}
					catch(Exception ex)
					{
						product.setImage("");
						product.setDrawing("");
					}
				}
			}
			
			Gson gson = service.getGson();
			response.setContentType("text/html");
			
			Product oldProduct = service.getProductByName(product.getName());
			if(oldProduct == null)
			{
				response.setContentType("text/html");
				JsonResponse jr = service.genJsonResponse(false,
						"修改产品失败， 产品不存在", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
			else
			{
				if(product.getFinished() == null)
					product.setFinished(0);
				if(product.getSemi_finished() == null)
					product.setSemi_finished(0);
				
				Mold mold = service.getMoldByCode(product.getMold().getCode());
				product.setMold(mold);
				
				if(updateImage)
				{
					File file = new File("" + oldProduct.getImage());
					if(file.exists())
						file.delete();
				}
				else
				{
					product.setImage(oldProduct.getImage());
				}
				
				if(updateDrawing)
				{
					File file = new File("" + oldProduct.getDrawing());
					if(file.exists())
						file.delete();
				}
				else
				{
					product.setDrawing(oldProduct.getDrawing());
				}
				boolean success = service.updateProduct(product);
				
				response.setContentType("text/html");
				response.setCharacterEncoding("utf-8");
				JsonResponse jr = service.genJsonResponse(success,
						"修改产品成功", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			}
		} catch (Exception e) {
			OhtuneLogger.error(e,"Add product failed");
			Gson gson = service.getGson();
			JsonResponse jr = service.genJsonResponse(false,
					"修改产品失败", null);
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}

	private void addProduct(HttpServletRequest request,
			HttpServletResponse response) throws IOException {

		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");

		DiskFileItemFactory fileItemFactory = new DiskFileItemFactory();

		fileItemFactory.setSizeThreshold(1 * 1024 * 1024); // 1 MB

		fileItemFactory.setRepository(new File(service.getSystemConfig()
				.getProductImageFolder()));
		ServletFileUpload uploadHandler = new ServletFileUpload(fileItemFactory);

		try {

			Product product = new Product();
			product.setImage("");
			product.setDrawing("");
			
			List<FileItem> items = uploadHandler.parseRequest(request);

			Iterator<FileItem> itr = items.iterator();
			while (itr.hasNext()) {
				FileItem item = (FileItem) itr.next();

				if (item.isFormField()) {
					if(item.getFieldName().equals("name"))
						product.setName(item.getString("utf-8"));
					else if(item.getFieldName().equals("name_eng"))
						product.setName_eng(item.getString("utf-8"));
					else if(item.getFieldName().equals("status"))
						product.setStatus(item.getString("utf-8"));
					else if(item.getFieldName().equals("our_name"))
						product.setOur_name(item.getString("utf-8"));
					else if(item.getFieldName().equals("mold_rate"))
						product.setMold_rate(item.getString("utf-8"));
					else if(item.getFieldName().equals("machining_pos"))
						product.setMachining_pos(item.getString("utf-8"));
					else if(item.getFieldName().equals("handwork_pos"))
						product.setHandwork_pos(item.getString("utf-8"));
					else if(item.getFieldName().equals("polishing"))
						product.setPolishing(item.getString("utf-8"));
					else if(item.getFieldName().equals("mold_code"))
					{
						Mold mold = new Mold();
						mold.setCode(item.getString("utf-8"));
						product.setMold(mold);
					}
					else if(item.getFieldName().equals("finished"))
					{
						try
						{
							product.setFinished(Integer.parseInt(item.getString("utf-8")));
						}catch(Exception e)
						{
							product.setFinished(0);
						}
					}
					else if(item.getFieldName().equals("semi_finished"))
					{
						try
						{
							product.setSemi_finished(Integer.parseInt(item.getString("utf-8")));
						}catch(Exception e)
						{
							product.setSemi_finished(0);
						}
					}
				}

				else {
					try
					{
						String fileName = new String(item.getName().getBytes(), "utf-8");
						long size = item.getSize();
						fileName = System.currentTimeMillis() + "." +  fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
						if(item.getFieldName().equals("image") && size > 0)
						{
							File file = new File(service.getSystemConfig()
									.getProductImageFolder(), fileName);
							item.write(file);
							product.setImage(file.getAbsolutePath());
							
							try
							{
								UtilityFunc.compressImage(1280, 800, file.getAbsolutePath());
							}
							catch(Exception e)
							{
								OhtuneLogger.error(e, "Compress image :" + file.getAbsolutePath() + " failed");
							}
						}
						else if(item.getFieldName().equals("drawing") && size > 0)
						{	
							File file = new File(service.getSystemConfig()
									.getProductDrawingFolder(), fileName);
							item.write(file);
							product.setDrawing(file.getAbsolutePath());
							
							try
							{
								UtilityFunc.compressImage(1280, 800, file.getAbsolutePath());
							}
							catch(Exception e)
							{
								OhtuneLogger.error(e, "Compress image :" + file.getAbsolutePath() + " failed");
							}
						}
					}
					catch(Exception ex)
					{
						product.setImage("");
						product.setDrawing("");
					}	
				}
			}
			
			if(product.getFinished() == null)
				product.setFinished(0);
			if(product.getSemi_finished() == null)
				product.setSemi_finished(0);
			
			Mold mold = service.getMoldByCode(product.getMold().getCode());
			product.setMold(mold);
			boolean success = service.addProduct(product);
			JsonResponse jr = service.genJsonResponse(success,
					"添加产品成功", null);
			Gson gson = service.getGson();
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			
		} catch (Exception e) {
			OhtuneLogger.error(e,"Add product failed");
			Gson gson = service.getGson();
			JsonResponse jr = service.genJsonResponse(false,
					"添加产品失败", null);
			response.setContentType("text/html");
			response.setCharacterEncoding("utf-8");
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
	private void getProductImage(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		String name = request.getParameter("name");
		Product product = service.getProductByName(name);
		if(product != null && product.getImage() != null)
		{
			File file = new File(product.getImage());
			if(!file.exists())
			{
				response.getOutputStream().write("产品图片不存在".getBytes("utf-8"));
			}
			else
			{
				ServletContext sc = getServletContext();
				String mimeType = sc.getMimeType(file.getName());
			    if (mimeType == null) {
			        OhtuneLogger.error("Could not get MIME type of product image, filename=" + file.getName());
			        response.getOutputStream().write("产品图片格式无法显示".getBytes("utf-8"));
			    }
			    else
			    {
			    	// Set content type
			        response.setContentType(mimeType);

			        // Set content size
			        response.setContentLength((int)file.length());

			        // Open the file and output streams
			        FileInputStream in = new FileInputStream(file);
			        OutputStream out = response.getOutputStream();

			        // Copy the contents of the file to the output stream
			        byte[] buf = new byte[1024];
			        int count = 0;
			        while ((count = in.read(buf)) >= 0) {
			            out.write(buf, 0, count);
			        }
			        in.close();
			        out.close();
			    }
			    
			}
		}
		else
		{
			response.getOutputStream().write("找不到该产品,或该产品图片不存在".getBytes("utf-8"));
		}
	}
	
	private void getProductDrawing(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		String name = request.getParameter("name");
		Product product = service.getProductByName(name);
		if(product != null && product.getDrawing() != null)
		{
			File file = new File(product.getDrawing());
			if(!file.exists())
			{
				response.getOutputStream().write("产品图纸不存在".getBytes("utf-8"));
			}
			else
			{
				ServletContext sc = getServletContext();
				String mimeType = sc.getMimeType(file.getName());
			    if (mimeType == null) {
			        OhtuneLogger.error("Could not get MIME type of product drawing, filename=" + file.getName());
			        response.getOutputStream().write("产品图纸格式无法显示".getBytes("utf-8"));
			    }
			    else
			    {
			    	// Set content type
			        response.setContentType(mimeType);

			        // Set content size
			        response.setContentLength((int)file.length());

			        // Open the file and output streams
			        FileInputStream in = new FileInputStream(file);
			        OutputStream out = response.getOutputStream();

			        // Copy the contents of the file to the output stream
			        byte[] buf = new byte[1024];
			        int count = 0;
			        while ((count = in.read(buf)) >= 0) {
			            out.write(buf, 0, count);
			        }
			        in.close();
			        out.close();
			    }
			    
			}
		}
		else
		{
			response.getOutputStream().write("找不到该产品,或该产品图纸不存在".getBytes("utf-8"));
		}
	}
	
	private void deleteProduct(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		
		String name = request.getParameter("name");
		Product product = service.getProductByName(name);
		if(product != null)
		{
			boolean success = service.deleteProduct(product);
			if(success && product.getImage() != null)
			{
				File file = new File(product.getImage());
				if(file.exists())
					file.delete();
			}
			if(success && product.getDrawing() != null)
			{
				File file = new File(product.getDrawing());
				if(file.exists())
					file.delete();
			}
			JsonResponse jr = service.genJsonResponse(success, success ? "删除成功":"删除失败", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
		else
		{
			JsonResponse jr = service.genJsonResponse(false, "删除失败， 产品不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
	
	
	private void sellProduct(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		
		String name = request.getParameter("name");
		String sQuantity = "0" + request.getParameter("quantity");
		
		int iQuantity = 0;
		
		try{
			iQuantity = Integer.parseInt(sQuantity);
		}catch(Exception e)
		{
			JsonResponse jr = service.genJsonResponse(false, "出仓数量错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		OhtuneLogger.info("Sell product by user, product = " + name + ", user =" + sessionUser.getName() + ", quantity = " + iQuantity);
		
		Product product = service.getProductByName(name);
		if(product != null)
		{
			boolean success = false;
			if(product.getFinished() >= iQuantity)
			{
				product.setFinished(product.getFinished() - iQuantity);
				success = service.updateProduct(product);
			}
			
			JsonResponse jr = service.genJsonResponse(success, success ? "出仓成功":"出仓失败", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
		else
		{
			JsonResponse jr = service.genJsonResponse(false, "出仓失败， 产品不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
}
