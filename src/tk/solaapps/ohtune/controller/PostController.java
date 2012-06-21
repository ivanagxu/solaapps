package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.Role;
import tk.solaapps.ohtune.model.Section;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;

import com.google.gson.Gson;


/**
 * Servlet implementation class PostController
 */
@WebServlet("/PostController")
public class PostController extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public PostController() {
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
		
		List<Role> roles = new ArrayList<Role>();
		Role role = service.getRoleById(10001l);
		roles.add(role);
		role = service.getRoleById(10002l);
		roles.add(role);
		
		Section section = service.getSectionById(10001l);
		Post post = new Post();
		post.setName(System.currentTimeMillis() + "");
		post.setRemark("Testing Post");
		post.setSection(section);
		post.setRole(roles);
		//service.addPost(post);
		
		List<Post> posts = service.getAllPost();
		
		Gson gson = new Gson();
		response.getOutputStream().write(gson.toJson(posts).getBytes("utf-8"));
	}

}
