package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.Order;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;
import tk.solaapps.ohtune.util.UtilityFunc;

import com.google.gson.Gson;

/**
 * Servlet implementation class JobController
 */
@WebServlet("/JobController")
public class JobController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JobController() {
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
	public void process(String actionName, HttpServletRequest request, HttpServletResponse response) throws IOException{
		if(actionName == null || actionName.trim().equals(""))
		{
			OhtuneLogger.error("Unknow action name in JobController");
		}
		else if(actionName.equals("getMyJobList"))
		{
			getMyJobList(request, response);
		}
		else if(actionName.equals("getJobByOrder"))
		{
			getJobByOrder(request, response);
		}
		else if(actionName.equals("completeJob"))
		{
			completeJob(request,response);
		}
		else if(actionName.equals("addJobToOrder"))
		{
			deleteJobFromOrder(request, response);
		}
		else if(actionName.equals("deleteJobFromOrder"))
		{
			deleteJobFromOrder(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in JobController, action name = " + actionName);
		}
	}
	
	private void getMyJobList(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		List<Job> jobs = service.getMyJobList(sessionUser);
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(jobs, JsonDataWrapper.TYPE_JOB);
		
		UtilityFunc.fillImageDrawingForJob(dw.getData(), service);
		
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	private void getJobByOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		String sOrderId = request.getParameter("orderid");
		
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		Order order = service.getOrderById(Long.parseLong(sOrderId));
		List<Job> jobs = service.getJobByOrder(order);
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(jobs, JsonDataWrapper.TYPE_JOB);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	
	private void completeJob(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		String sComplete_count = "0" + request.getParameter("complete_count");
		String sDisuse_count = "0" + request.getParameter("disuse_count");
		String job_type = request.getParameter("job_type");
		
		String sIsCompleted = request.getParameter("is_completed");
		String finish_remark = request.getParameter("finish_remark");
		String id = request.getParameter("id");
		String sAssigned_to = request.getParameter("assigned_to");
		String sIsRejected = request.getParameter("is_rejected");
		
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		
		JsonResponse jr = null;
		Gson gson = service.getGson();
		
		int iComplete_count = 0;
		int iDisuse_count = 0;
		try
		{
			iComplete_count = Integer.parseInt(sComplete_count);
			iDisuse_count = Integer.parseInt(sDisuse_count);
		}catch(Exception e)
		{
			jr = service.genJsonResponse(false, "完成数或废品数错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		JobType jobType = service.getJobTypeByName(job_type);
		if(jobType == null)
		{
			jr = service.genJsonResponse(false, "分配部门错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		Job job = service.getJobById(Long.parseLong(id));
		
		if(job == null)
		{
			jr = service.genJsonResponse(false, "工作不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		if(job.getStatus() == Job.STATUS_DONE)
		{
			jr = service.genJsonResponse(false, "工作已完成", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		if(!job.getOrders().getStatus().equals(Order.STATUS_PROCESSING))
		{
			jr = service.genJsonResponse(false, "无法完成，订单尚未开始或已经完成", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		if(job.getJob_type().getName().equals(job_type))
		{
			jr = service.genJsonResponse(false, "不能分配至相同部门", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		if(job.getRemaining() < (iComplete_count + iDisuse_count))
		{
			jr = service.genJsonResponse(false, "完成数加废品数不能大于总数", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		UserAC assignedTo = null;
		try
		{
			assignedTo = service.getUserACById(Long.parseLong(sAssigned_to));
		}catch(Exception e)
		{
			assignedTo = null;
		}
		
		boolean isCompleted = (sIsCompleted + "").equals("on");
		if(job.getTotal() == (iComplete_count + iDisuse_count))
			isCompleted = true;
		boolean isRejected = (sIsRejected + "").equals("on");
		
		if(isRejected && (job_type.equals(JobType.FINISH_DEPOT) || job_type.equals(JobType.FINISH_SEMI_FINISH)))
		{
			jr = service.genJsonResponse(false, "无法返工至成品仓或半成品仓", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		try
		{
			boolean success = service.completeJob(job, assignedTo, job_type, iComplete_count, iDisuse_count, isCompleted, isRejected, finish_remark, sessionUser);
			
			boolean alldone = true;
			List<Job> jobByOrder = service.getJobByOrder(job.getOrders());
			for(int i = 0 ; i < jobByOrder.size(); i++)
			{
				if(!jobByOrder.get(i).getStatus().equals(Job.STATUS_DONE))
				{
					alldone = false;
				}
			}
			if(alldone)
			{
				if(job.getOrders().getUse_finished() > 0)
				{
					Product p = service.getProductByName(job.getOrders().getProduct_name());
					p.setFinished(p.getFinished() + job.getOrders().getUse_finished());
					success = success & service.updateProduct(p);
				}
				success = success & service.completeOrder(job.getOrders(), sessionUser);
			}
			
			jr = service.genJsonResponse(success, success ? "工作移交成功" : "工作移交失败", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
		catch(RuntimeException e)
		{
			jr = service.genJsonResponse(false, e.getMessage(), null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
	
	private void deleteJobFromOrder(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		UserAC sessionUser = new UserAC();
		if(request.getSession().getAttribute("user") != null)
			sessionUser = (UserAC)request.getSession().getAttribute("user");
		
		String sId = request.getParameter("id");
		String sDeleteCount = "0" + request.getParameter("delete_count");
		int iDeleteCount;
		
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		
		JsonResponse jr = null;
		Gson gson = service.getGson();
		
		Job job = service.getJobById(Long.parseLong(sId));
		try
		{
			iDeleteCount = Integer.parseInt(sDeleteCount);
		}
		catch(Exception e)
		{
			jr = service.genJsonResponse(false, "减数数量错误", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
			return;
		}
		
		try
		{
			boolean success = true;
			Order order = service.getOrderById(job.getOrders().getId());
			
			if(!order.getStatus().equals(Order.STATUS_PROCESSING) || !job.getStatus().equals(Job.STATUS_PROCESSING))
			{
				jr = service.genJsonResponse(false, "减数的订单必须时进行中的订单而且被减的工作必须是进行中的", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
			
			JobType jt = job.getJob_type();
			if(jt.getName().equals(JobType.FINISH_DEPOT) || jt.getName().equals(JobType.FINISH_SEMI_FINISH))
			{
				jr = service.genJsonResponse(false, "减数的部门不能是仓库或者半成品仓库", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
			
			if(iDeleteCount > job.getRemaining() || order.getE_quantity() > (order.getQuantity() - iDeleteCount))
			{
				jr = service.genJsonResponse(false, "减数数量不能超过该工作剩余数量, 也不能让减数后生产数量小于订单数量", null);
				response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
				return;
			}
			
			order.setQuantity(order.getQuantity() - iDeleteCount);
			
			order.setProduct_rate(((float)order.getE_quantity()) / ((float)order.getQuantity()));
			
			success = success & service.updateOrder(order);
			job.setRemaining(job.getRemaining() - iDeleteCount);
			job.setTotal(job.getTotal() - iDeleteCount);
			
			if(job.getRemaining() == 0)
			{
				success = success & service.completeJob(job, null, JobType.FINISH_DEPOT, 0, 0, true, false, "减数", sessionUser);
			}
			else
			{
				success = success & service.updateJob(job);
			}
			
			boolean alldone = true;
			List<Job> jobByOrder = service.getJobByOrder(job.getOrders());
			for(int i = 0 ; i < jobByOrder.size(); i++)
			{
				if(!jobByOrder.get(i).getStatus().equals(Job.STATUS_DONE))
				{
					alldone = false;
				}
			}
			if(alldone)
			{
				if(job.getOrders().getUse_finished() > 0)
				{
					Product p = service.getProductByName(job.getOrders().getProduct_name());
					p.setFinished(p.getFinished() + job.getOrders().getUse_finished());
					success = success & service.updateProduct(p);
				}
				success = success & service.completeOrder(job.getOrders(), sessionUser);
			}
			
			jr = service.genJsonResponse(success, success ? "减数成功" : "减数失败", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
		catch(RuntimeException e)
		{
			jr = service.genJsonResponse(false, e.getMessage(), null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}

}
