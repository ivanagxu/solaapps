package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.Order;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.JsonOrder;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;
import tk.solaapps.ohtune.util.UtilityFunc;

import com.google.gson.Gson;

/**
 * Servlet implementation class OrderController
 */
@WebServlet("/OrderController")
public class OrderController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public OrderController() {
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
	public void process(String actionName, HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(actionName == null || actionName.trim().equals(""))
		{
			OhtuneLogger.error("Unknow action name in OrderController");
		}
		else if(actionName.equals("searchAll"))
		{
			searchAll(request, response);
		}
		else if(actionName.equals("createOrder"))
		{
			createOrder(request, response);
		}
		else if(actionName.equals("updateOrder"))
		{
			updateOrder(request, response);
		}
		else if(actionName.equals("deleteOrder"))
		{
			deleteOrder(request, response);
		}
		else if(actionName.equals("getMyOrderList"))
		{
			getMyOrderList(request, response);
		}
		else if(actionName.equals("getCompletedOrderList"))
		{
			getCompletedOrderList(request, response);
		}
		else if(actionName.equals("getOrderById"))
		{
			getOrderById(request, response);
		}
		else if(actionName.equals("preCreateOrder"))
		{
			preCreateOrder(request, response);
		}
		else if(actionName.equals("pauseOrder"))
		{
			pauseOrder(request, response);
		}
		else if(actionName.equals("cancelOrder"))
		{
			cancelOrder(request, response);
		}
		else if(actionName.equals("resumeOrder"))
		{
			resumeOrder(request, response);
		}
		else if(actionName.equals("addJobToOrder"))
		{
			addJobToOrder(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in OrderController, action name = " + actionName);
		}
	}
	
	private void searchAll(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");

		Gson gson = service.getGson();

		//String[] columns = new String[] { Order.COLUMN_CREATOR };
		//String[] values = new String[] { "ivan" };
		
		List<Order> orders = service.searchOrder(null, null,null,null, 0, 10000, "id", true);
		JsonDataWrapper dw = new JsonDataWrapper(orders, JsonDataWrapper.TYPE_ORDER);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	private void getMyOrderList(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");

		Gson gson = service.getGson();

		
		List<String> status = new ArrayList<String>();
		status.add(Order.STATUS_APPROVING);
		status.add(Order.STATUS_PROCESSING);
		status.add(Order.STATUS_PAUSED);
		String[] inClause = new String[] { Order.COLUMN_STATUS };
		Collection[] in = new Collection[] { status };
		
		List<Order> orders = service.searchOrder(null, null ,inClause,in, 0, 10000, "id", true);
		JsonDataWrapper dw = new JsonDataWrapper(orders, JsonDataWrapper.TYPE_ORDER);
		
		UtilityFunc.fillImageDrawingForOrder(dw.getData(), service);
		
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	private void getCompletedOrderList(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");

		Gson gson = service.getGson();

		
		List<String> status = new ArrayList<String>();
		status.add(Order.STATUS_FINISHED);
		String[] inClause = new String[] { Order.COLUMN_STATUS };
		Collection[] in = new Collection[] { status };
		
		List<Order> orders = service.searchOrder(null, null ,inClause,in, 0, 10000, "id", true);
		JsonDataWrapper dw = new JsonDataWrapper(orders, JsonDataWrapper.TYPE_ORDER);
		
		UtilityFunc.fillImageDrawingForOrder(dw.getData(), service);
		
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	private void updateOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{	
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		String sOrderId = request.getParameter("orderid");
		String sCreator = request.getParameter("creator");
		String sCustomerName = request.getParameter("customer_name");
		String sCustomerCode = request.getParameter("customer_code");
		String sProductName = request.getParameter("product_name");
		String sUseFinished = "0" + request.getParameter("use_finished");
		String sUseSemiFinished = "0" + request.getParameter("use_semi_finished");
		String sRequirement1 = request.getParameter("requirement1");
		String sRequirement2 = request.getParameter("requirement2");
		String sRequirement4 = request.getParameter("requirement4");
		String sDeadline = request.getParameter("deadline");
		String sC_Deadline = request.getParameter("c_deadline");
		String sEQuantity = "0" + request.getParameter("e_quantity");
		String sQuantity = "0" + request.getParameter("quantity");
		String sProductRate = "0" + request.getParameter("product_rate");
		String sPriority = request.getParameter("priority");
		
		int iUseFinished,iUseSemiFinished,iEQuantity, iQuantity, iPriority;
		float fProductRate;
		
		Gson gson = service.getGson();
		JsonResponse jr = null;
		
		Order order = null;
		try
		{
			order = service.getOrderById(Long.parseLong(sOrderId));
			if(order == null)
			{
				throw new Exception("");
			}
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "订单不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		try
		{
			iUseFinished = Integer.parseInt(sUseFinished);
			iUseSemiFinished = Integer.parseInt(sUseSemiFinished);
			iEQuantity = Integer.parseInt(sEQuantity); 
			iQuantity = Integer.parseInt(sQuantity);
			fProductRate = Float.parseFloat(sProductRate);
			iPriority = (sPriority.equals("紧急") || sPriority.equals("1")) ? 1: 0;
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "成品,半成品,生产数,成品率 数据错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Product product = service.getProductByName(sProductName);
		if(product == null)
		{
			jr = service.genJsonResponse(false, "产品不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		else
		{
			if((product.getFinished() - (iUseFinished - order.getUse_finished())) < 0)
			{
				jr = service.genJsonResponse(false, "成品数量不足", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
			if((product.getSemi_finished() - (iUseSemiFinished - order.getUse_semi_finished())) < 0)
			{
				jr = service.genJsonResponse(false, "半成品数量不足", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
		}
		
		if(!order.getStatus().equals(Order.STATUS_APPROVING))
		{
			product.setSemi_finished(product.getSemi_finished() - (iUseSemiFinished - order.getUse_semi_finished()));
			product.setFinished(product.getFinished() - (iUseFinished - order.getUse_finished()));
		}
		
		order.setUse_finished(iUseFinished);
		order.setUse_semi_finished(iUseSemiFinished);
		order.setProduct_rate(fProductRate);
		order.setQuantity(iQuantity);
		order.setE_quantity(iEQuantity);
		order.setPriority(iPriority);
		
		order.setCreator(sCreator);
		//order.setCustomer_code(sCustomerName);
		//order.setCustomer_name(sCustomerCode);
		order.setCustomer_code(sCustomerCode);
		order.setCustomer_name(sCustomerName);
		try
		{
			order.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(sDeadline));
			order.setC_deadline(new SimpleDateFormat("yyyy-MM-dd").parse(sC_Deadline));
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "交货日期错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		order.setProduct_name(product.getName());
		order.setProduct_our_name(product.getOur_name());
		order.setRequirement_1(sRequirement1);
		order.setRequirement_2(sRequirement2);
		order.setRequirement_4(sRequirement4);
		
		boolean success = service.updateOrder(order, product, null, null, sessionUser);
		
		jr = service.genJsonResponse(success, success ? "修改订单成功" : "修改订单失败", null);

		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	
	private void preCreateOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		String sCreator = request.getParameter("creator");
		String sCustomerName = request.getParameter("customer_name");
		String sCustomerCode = request.getParameter("customer_code");
		String sProductName = request.getParameter("product_name");
		String sProductOurName = request.getParameter("product_our_name");
		String sUseFinished = "0" + request.getParameter("use_finished");
		String sUseSemiFinished = "0" + request.getParameter("use_semi_finished");
		String sRequirement1 = request.getParameter("requirement1");
		String sRequirement2 = request.getParameter("requirement2");
		String sRequirement3 = request.getParameter("requirement3");
		String sRequirement4 = request.getParameter("requirement4");
		String sCDeadline = request.getParameter("c_deadline");
		String sEQuantity = "0" + request.getParameter("e_quantity");
		
		int iUseFinished,iUseSemiFinished, iEQuantity;
		
		Enumeration<String> parms = request.getParameterNames();
		
		Date create_date = new Date();
		
		
		Gson gson = service.getGson();
		JsonResponse jr = null;
		
		try
		{
			iUseFinished = Integer.parseInt(sUseFinished);
			iUseSemiFinished = Integer.parseInt(sUseSemiFinished);
			iEQuantity = Integer.parseInt(sEQuantity);
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "订单数量成品半成品数量错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		UserAC creator = service.getUserACById(Long.parseLong(sCreator));
		if(creator == null)
		{
			jr = service.genJsonResponse(false, "跟单人员错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Product product = service.getProductByName(sProductName);
		if(product == null)
		{
			jr = service.genJsonResponse(false, "产品不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		else
		{
			if(product.getFinished() < iUseFinished)
			{
				jr = service.genJsonResponse(false, "成品数量不足", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
			if(product.getSemi_finished() < iUseSemiFinished)
			{
				jr = service.genJsonResponse(false, "半成品数量不足", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
		}
		
		Customer customer = service.getCustomerById(Long.parseLong(sCustomerName));
		if(customer == null)
		{
			jr = service.genJsonResponse(false, "客户不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Order order = new Order();
		order.setCreate_date(create_date);
		order.setCreator(creator.getName());
		order.setCustomer_code(customer.getCode());
		order.setCustomer_name(customer.getName());
		try
		{
			order.setC_deadline(new SimpleDateFormat("yyyy-MM-dd").parse(sCDeadline) );
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "日期错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		order.setProduct_name(product.getName());
		order.setProduct_our_name(product.getOur_name());
		
		order.setRequirement_1(sRequirement1);
		order.setRequirement_2(sRequirement2);
		order.setRequirement_3(sRequirement3);
		order.setRequirement_4(sRequirement4);
		order.setPriority(0);
		order.setQuantity(0);
		order.setE_quantity(iEQuantity);
		order.setProduct_rate(0f);
		
		List<JobType> jobTypes = new ArrayList<JobType>();
		List<Job> jobs = new ArrayList<Job>();

		for(int i = 0; i < jobTypes.size(); i++)
		{
			if(jobTypes.get(i) != null)
			{
				if(jobTypes.get(i).getName().equals("仓库") || jobTypes.get(i).getName().equals("半成品"))
				{
					jr = service.genJsonResponse(false, "初始工作分配无法分配到 仓库 与 半成品仓", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
					return;
				}
			}
		}
		order.setUse_finished(iUseFinished);
		order.setUse_semi_finished(iUseSemiFinished);
		order.setStatus(Order.STATUS_APPROVING);
		
		boolean success = service.createOrder(order, jobs, sessionUser);
		
		jr = service.genJsonResponse(success, success ? "添加订单成功" : "添加订单失败", null);
		
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	
	//For apporval
	private void createOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		//String sRequirement1 = request.getParameter("requirement1");
		//String sRequirement2 = request.getParameter("requirement2");
		//String sRequirement3 = request.getParameter("requirement3");
		String sRequirement4 = request.getParameter("requirement4");
		String sJobType1 = request.getParameter("job_type1");
		String sJobType2 = request.getParameter("job_type2");
		String sJobType3 = request.getParameter("job_type3");
		String sQuantity1 = "0" + request.getParameter("quantity1");
		String sQuantity2 = "0" + request.getParameter("quantity2");
		String sQuantity3 = "0" + request.getParameter("quantity3");
		String sQuantity = "0" + request.getParameter("quantity");
		String sProductRate = request.getParameter("product_rate");
		String sOrderId = request.getParameter("orderid");
		String sPriority = request.getParameter("priority");
		String sDeadline = request.getParameter("deadline");
		
		int iQuantity,iQuantity1,iQuantity2,iQuantity3,iUseFinished,iUseSemiFinished;
		float fProductRate;
		
		Gson gson = service.getGson();
		JsonResponse jr = null;
		
		Order order = service.getOrderById(Long.parseLong(sOrderId));
		if(order == null)
		{
			jr = service.genJsonResponse(false, "审核失败， 订单不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		if(!order.getStatus().equals(Order.STATUS_APPROVING))
		{
			jr = service.genJsonResponse(false, "审核失败， 订单已开始或已经完成", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		try
		{
			iQuantity = Integer.parseInt(sQuantity);
			iQuantity1 = Integer.parseInt(sQuantity1);
			iQuantity2 = Integer.parseInt(sQuantity2);
			iQuantity3 = Integer.parseInt(sQuantity3);
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "部门分配或成品分配数据错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		try
		{
			order.setDeadline(new SimpleDateFormat("yyyy-MM-dd").parse(sDeadline));
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "日期错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		try
		{
			fProductRate = Float.parseFloat(sProductRate);
			if(fProductRate > 1f)
			{
				throw new Exception();
			}
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "成品率不能大于1, 且必须为小数", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Product product = service.getProductByName(order.getProduct_name());
		if(product == null)
		{
			jr = service.genJsonResponse(false, "产品不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		else
		{
			if(product.getFinished() < order.getUse_finished())
			{
				jr = service.genJsonResponse(false, "成品数量不足", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
			if(product.getSemi_finished() < order.getUse_semi_finished())
			{
				jr = service.genJsonResponse(false, "半成品数量不足", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
		}
		
		order.setQuantity(iQuantity1 + iQuantity2 + iQuantity3);
		order.setProduct_rate(fProductRate);
		order.setQuantity(iQuantity);
		
		if(iQuantity != iQuantity1 + iQuantity2 + iQuantity3)
		{
			jr = service.genJsonResponse(false, "部门分配数量与生产数量不符, 各部门数量总和应为" + iQuantity, null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		order.setPriority(((sPriority.equals("紧急") || sPriority.equals("1"))) ? 1: 0);
		//order.setRequirement_1(sRequirement1);
		//order.setRequirement_2(sRequirement2);
		//order.setRequirement_3(sRequirement3);
		order.setRequirement_4(sRequirement4);
		
		List<JobType> jobTypes = new ArrayList<JobType>();
		List<Job> jobs = new ArrayList<Job>();
		Job job;
		JobType jobType = service.getJobTypeByName(sJobType1);
		if(jobType != null)
		{
			jobTypes.add(jobType);
			job = new Job();
			job.setJob_type(jobType);
			job.setTotal(iQuantity1);
			job.setRemaining(iQuantity1);
			jobs.add(job);
		}
		jobType = service.getJobTypeByName(sJobType2);
		if(jobType != null)
		{
			jobTypes.add(jobType);
			job = new Job();
			job.setJob_type(jobType);
			job.setTotal(iQuantity2);
			job.setRemaining(iQuantity2);
			jobs.add(job);
		}
		jobType = service.getJobTypeByName(sJobType3);
		if(jobType != null)
		{
			jobTypes.add(jobType);
			job = new Job();
			job.setJob_type(jobType);
			job.setTotal(iQuantity3);
			job.setRemaining(iQuantity3);
			jobs.add(job);
		}
		
		if(jobTypes.size() == 0)
		{
			jr = service.genJsonResponse(false, "请输入工作分配内容", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}

		for(int i = 0; i < jobTypes.size(); i++)
		{
			if(jobTypes.get(i) != null)
			{
				if(jobTypes.get(i).getName().equals("仓库") || jobTypes.get(i).getName().equals("半成品"))
				{
					jr = service.genJsonResponse(false, "初始工作分配无法分配到 仓库 与 半成品仓", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
					return;
				}
			}
		}
		service.deleteJobByOrder(order, sessionUser);
		boolean success = service.approveOrder(order, jobs, sessionUser);
		
		
		jr = service.genJsonResponse(success, success ? "审核订单成功" : "审核订单失败", null);
		
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	
	private void deleteOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		JsonResponse jr = null;

		try
		{
			Long id = Long.parseLong(request.getParameter("id"));

			Order order = service.getOrderById(id);
			if(order == null)
			{
				jr = service.genJsonResponse(false, "删除订单失败,订单不存在", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			}
			else
			{
				if(order.getStatus().equals(Order.STATUS_PROCESSING))
				{
					boolean success = true;
					List<Job> jobs = service.getJobByOrder(order);
					for(int i = 0; i < jobs.size(); i++)
					{
						if(jobs.get(i).getJob_type().getName().equals(JobType.FINISH_DEPOT) || 
								jobs.get(i).getJob_type().getName().equals(JobType.FINISH_SEMI_FINISH))
						{
							jr = service.genJsonResponse(false, "删除订单失败，改订单已进行并有成品到达仓库或半成品仓", null);
							response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
							return;
						}
					}
					
					if(success)
					{
						success = service.deleteJobByOrder(order, sessionUser);
						if(success)
							success = success & service.deleteOrder(order);
						jr = service.genJsonResponse(success, success ? "删除订单成功" : "删除订单失败", null);
						response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
					}
					else
					{
						jr = service.genJsonResponse(success, success ? "删除订单成功" : "删除进行中订单失败", null);
						response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
					}
					
					
				}
				else if(order.getStatus().equals(Order.STATUS_APPROVING) || order.getStatus().equals(Order.STATUS_FINISHED))
				{
					boolean success = service.deleteJobByOrder(order, sessionUser);
					if(success)
						success = success & service.deleteOrder(order);
					jr = service.genJsonResponse(success, success ? "删除订单成功" : "删除订单失败", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}
				else
				{
					jr = service.genJsonResponse(false, "删除订单失败， 订单已经在进行中", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}
			}
		}
		catch(Exception e)
		{
			jr = service.genJsonResponse(false, "删除订单错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
		
	}
	private void getOrderById(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		
		String sId = request.getParameter("id");
		Long id;
		try
		{
			 id = Long.parseLong(sId);
		}
		catch(Exception e)
		{
			id = 0l;
		}
		
		Order order = service.getOrderById(id);
		List<Order> orders = new ArrayList<Order>();
		
		if(order != null)
			orders.add(order);
		
		JsonDataWrapper dw = new JsonDataWrapper(orders, JsonDataWrapper.TYPE_ORDER);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	private void pauseOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		JsonResponse jr = null;

		try
		{
			Long id = Long.parseLong(request.getParameter("id"));

			Order order = service.getOrderById(id);
			if(order == null)
			{
				jr = service.genJsonResponse(false, "暂停订单失败,订单不存在", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			}
			else
			{
				if(!order.getStatus().equals(Order.STATUS_PROCESSING))
				{
					jr = service.genJsonResponse(false, "暂停订单失败,订单尚未开始或已经完成", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}
				else
				{
					boolean success = service.pauseOrder(order, sessionUser);
					jr = service.genJsonResponse(success, success ? "暂停订单成功" : "暂停订单失败", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}
			}
		}
		catch(Exception e)
		{
			jr = service.genJsonResponse(false, "暂停订单错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
	
	private void cancelOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		JsonResponse jr = null;

		try
		{
			Long id = Long.parseLong(request.getParameter("id"));

			Order order = service.getOrderById(id);
			if(order == null)
			{
				jr = service.genJsonResponse(false, "取消订单失败,订单不存在", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			}
			else
			{
				if(order.getStatus().equals(Order.STATUS_APPROVING) || order.getStatus().equals(Order.STATUS_FINISHED))
				{
					boolean success = service.cancelOrder(order, sessionUser);
					jr = service.genJsonResponse(success, success ? "取消订单成功" : "取消订单失败", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}else
				{
					jr = service.genJsonResponse(false, "取消订单失败， 订单已经在进行中", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}
			}
		}
		catch(Exception e)
		{
			jr = service.genJsonResponse(false, "取消订单错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
	
	private void resumeOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		JsonResponse jr = null;

		try
		{
			Long id = Long.parseLong(request.getParameter("id"));

			Order order = service.getOrderById(id);
			if(order == null)
			{
				jr = service.genJsonResponse(false, "恢复订单失败,订单不存在", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			}
			else
			{
				if(!order.getStatus().equals(Order.STATUS_PAUSED))
				{
					jr = service.genJsonResponse(false, "恢复订单失败,订单不在暂停状态", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}
				else
				{
					boolean success = service.resumeOrder(order, sessionUser);
					jr = service.genJsonResponse(success, success ? "恢复订单成功" : "恢复订单失败", null);
					response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				}
			}
		}
		catch(Exception e)
		{
			jr = service.genJsonResponse(false, "恢复订单错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
	
	private void addJobToOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		IOhtuneService service = (IOhtuneService) OhtuneServiceHolder
				.getInstence().getBeanFactory().getBean("uhtuneService");
		
		Gson gson = service.getGson();
		JsonResponse jr = null;
		
		String sQuantity = "0" + request.getParameter("quantity");
		String sOrderId = request.getParameter("order_id");
		String sAssignedTo = request.getParameter("assigned_to");
		String sJobType = request.getParameter("job_type");
		
		Long iOrderId = Long.parseLong(sOrderId);
		int iQuantity;
		
		try
		{
			iQuantity = Integer.parseInt(sQuantity);
		}
		catch(Exception e)
		{
			jr = service.genJsonResponse(false, "补数数量有错", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Order order = service.getOrderById(iOrderId);
		
		if(order == null)
		{
			jr = service.genJsonResponse(false, "订单不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		JobType jobType = service.getJobTypeByName(sJobType);
		if(jobType == null)
		{
			jr = service.genJsonResponse(false, "送交部门有误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		if(order.getStatus().equals(Order.STATUS_CANCELED) || order.getStatus().equals(Order.STATUS_FINISHED)
				|| order.getStatus().equals(Order.STATUS_APPROVING) || order.getStatus().equals(Order.STATUS_PAUSED))
		{
			jr = service.genJsonResponse(false, "只能对进行中的订单进行补数", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		UserAC assignedTo = null;
		try
		{
			assignedTo = service.getUserACById(Long.parseLong(sAssignedTo));
		}catch(Exception e)
		{
			assignedTo = null;
		}
		
		boolean success = service.addJobToOrder(order, jobType, iQuantity, assignedTo, sessionUser);
		
		if(success)
		{
			order.setQuantity(order.getQuantity() + iQuantity);
			order.setProduct_rate(((float)order.getE_quantity()) / ((float)order.getQuantity()));
			//order.setE_quantity(order.getE_quantity() + (int)Math.ceil(iQuantity * order.getProduct_rate()));
			success = success & service.updateOrder(order);
		}
		
		jr = service.genJsonResponse(success, success ? "补数成功" : "补数失败", null);
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		
	}
}
