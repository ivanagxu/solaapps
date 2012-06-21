<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.io.*" %>
<%@ page import="tk.solaapps.ohtune.service.*" %>
<%@ page import="tk.solaapps.ohtune.pattern.*" %>
<%@ page import="java.text.*" %>
<%@ page import="java.util.*" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>SOLAAPP</title>

	<%@ include file="master.jsp" %>
	
	<%
	
	IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
	String fileName = request.getParameter("name");
	File downFile = new File(service.getSystemConfig()
			.getCommonDocumentFolder(), fileName);
	
	FileInputStream fin = new FileInputStream(downFile);
	BufferedInputStream in = new BufferedInputStream(fin);

    byte data[] = new byte[1024];
    int c = 0;
    
    ServletOutputStream op = response.getOutputStream();
    ServletContext context  = getServletConfig().getServletContext();
    String mimetype = context.getMimeType( fileName );
    response.setContentType( (mimetype != null) ? mimetype : "application/octet-stream" );
    response.setContentLength( (int)downFile.length());
    response.setHeader( "Content-Disposition", "attachment; filename=\"" + new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()) + "\"" );
    
    c = in.read(data);
    while(c > 0)
    {
    	
    	op.write(data, 0, c);
        c = in.read(data);
    }
	
    in.close();
	fin.close();
	op.flush();
	op.close();
	
	%>
</head>
<body>

</body>
</html>