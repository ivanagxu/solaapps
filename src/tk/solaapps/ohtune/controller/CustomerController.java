package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;

/**
 * Servlet implementation class CustomerController
 */
@WebServlet("/CustomerController")
public class CustomerController extends HttpServlet implements IOhtuneController{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CustomerController() {
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
			OhtuneLogger.error("action name is null in CustomerController");
		}
		else if(actionName.equals("getAllCustomer"))
		{
			getAllCustomer(request, response);
		}
		else if(actionName.equals("updateCustomer"))
		{
			updateCustomer(request, response);
		}
		else if(actionName.equals("getCustomerById"))
		{
			getCustomerById(request, response);
		}
		else if(actionName.equals("addCustomer"))
		{
			addCustomer(request, response);
		}
		else if(actionName.equals("deleteCustomer"))
		{
			deleteCustomer(request, response);
		}
		else
		{
			OhtuneLogger.error("Unknow action name in CustomerController, actionName=" + actionName);
		}
	}

	private void getAllCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		List<Customer> customers = service.getAllCustomer();
		Gson gson = service.getGson();
		JsonDataWrapper dw = new JsonDataWrapper(customers, JsonDataWrapper.TYPE_DEFAULT);
		response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
	}
	private void addCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String fax = request.getParameter("fax");
		String email = request.getParameter("email");
		String contact_person = request.getParameter("contact_person");
		String code = request.getParameter("code");
		
		Customer customer = new Customer();
		customer.setName(name);
		customer.setPhone(phone);
		customer.setFax(fax);
		customer.setEmail(email);
		customer.setContact_person(contact_person);
		customer.setCode(code);
		
		boolean success = service.addCustomer(customer);
		
		JsonResponse jr = service.genJsonResponse(success, "添加客户成功", customer);
		Gson gson = service.getGson();
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	private void updateCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		String id = request.getParameter("id");
		String name = request.getParameter("name");
		String phone = request.getParameter("phone");
		String fax = request.getParameter("fax");
		String email = request.getParameter("email");
		String contact_person = request.getParameter("contact_person");
		String code = request.getParameter("code");
		
		Customer customer = new Customer();
		customer.setId(new Long(id));
		customer.setName(name);
		customer.setPhone(phone);
		customer.setFax(fax);
		customer.setEmail(email);
		customer.setContact_person(contact_person);
		customer.setCode(code);
		
		boolean success = service.updateCustomer(customer);
		
		JsonResponse jr = service.genJsonResponse(success, "修改客户成功", customer);
		Gson gson = service.getGson();
		response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
	}
	private void getCustomerById(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		String id = request.getParameter("id");
		Customer customer = service.getCustomerById(new Long(id));
		Gson gson = service.getGson();
		response.getOutputStream().write(gson.toJson(customer).getBytes("utf-8"));
	}
	private void deleteCustomer(HttpServletRequest request, HttpServletResponse response) throws IOException
	{
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		String id = request.getParameter("id");
		Customer customer = service.getCustomerById(new Long(id));
		
		Gson gson = service.getGson();
		if(customer == null)
		{
			JsonResponse jr = service.genJsonResponse(false, "客户已删除或不存在", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
		else
		{
			boolean success = service.deleteCustomer(customer);
			JsonResponse jr = service.genJsonResponse(success, success ? "删除客户成功" : "删除客户失败", null);
			response.getOutputStream().write(gson.toJson(jr).getBytes("utf-8"));
		}
	}
}
