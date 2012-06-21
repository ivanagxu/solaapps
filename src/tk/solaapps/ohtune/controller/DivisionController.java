package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Department;
import tk.solaapps.ohtune.model.Division;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;

/**
 * Servlet implementation class DivisionController
 */
@WebServlet("/DivisionController")
public class DivisionController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DivisionController() {
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
		IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
		Division div = new Division();
		
		Department dept = service.getDepartmentById(10001l);
		div.setName(System.currentTimeMillis() + "");
		div.setRemark("Testing division");
		div.setDepartment(dept);
		
		service.addDivision(div);
		
		List<Division> divs = service.getAllDivision();
		
		Gson gson = new Gson();
		response.getOutputStream().write(gson.toJson(divs).getBytes());
	}

}
