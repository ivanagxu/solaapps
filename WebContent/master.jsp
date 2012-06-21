<%@page import="tk.solaapps.ohtune.model.*"%>
<%@page import="java.util.List"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

<link rel="stylesheet" type="text/css" href="resources/css/ohtune.css" />

<script type="text/javascript" src="master/app.js"></script>

<%	//Permission checking
	boolean isNotLoginPage = request.getRequestURL().toString().indexOf("login.jsp") < 0;
	if(request.getSession().getAttribute("user") == null && !isNotLoginPage)
	{
	}
	else if(request.getSession().getAttribute("user") == null && isNotLoginPage)
	{
		response.sendRedirect("login.jsp");
	}
	else{
		UserAC sessionUser = (UserAC)request.getSession().getAttribute("user");
		List<Role> roles = sessionUser.getPost().getRole();
		boolean isAdminUser = false;
		boolean isOrderUser = false;
		boolean isProductUser = false;
		for(int i = 0; i < roles.size(); i++)
		{
			if(roles.get(i).getName().equals("管理员") || roles.get(i).getName().equals("经理") || 
					roles.get(i).getName().equals("厂长"))
			{
				isAdminUser = true;
				isOrderUser = true;
			}
			if(roles.get(i).getName().equals("销售部"))
			{
				isOrderUser = true;
			}
			
			if(roles.get(i).getName().equals("生产部"))
			{
				isProductUser = true;
			}
		}
		
		if(request.getRequestURL().toString().indexOf("order.jsp") >=0 && !isOrderUser && !isAdminUser && isNotLoginPage)
		{
			response.sendRedirect("login.jsp");
			return;
		}
		
		else if(request.getRequestURL().toString().indexOf("admin.jsp") >=0 && !isAdminUser && isNotLoginPage)
		{
			response.sendRedirect("login.jsp");
			return;
		}
		
		else if(request.getRequestURL().toString().indexOf("production.jsp") >=0 && !isAdminUser && !isProductUser && isNotLoginPage)
		{
			response.sendRedirect("login.jsp");
			return;
		}
		
		else if(request.getRequestURL().toString().indexOf("inventory.jsp") >=0 && !isOrderUser && !isProductUser && !isAdminUser && isNotLoginPage)
		{
			response.sendRedirect("login.jsp");
			return;
		}
	}
%>