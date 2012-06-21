package tk.solaapps.ohtune.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.CellStyle;

import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.ProductRate;
import tk.solaapps.ohtune.model.ProductionLog;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonDataWrapper;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.OhtuneServiceHolder;
import tk.solaapps.ohtune.service.IOhtuneService;
import tk.solaapps.ohtune.util.UtilityFunc;

import com.google.gson.Gson;

/**
 * Servlet implementation class ReportController
 */
@WebServlet("/ReportController")
public class ReportController extends HttpServlet implements IOhtuneController{
        private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ReportController() {
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
                        OhtuneLogger.error("action name is null in ReportController");
                }
                else if(actionName.equals("generateProductRateReportByProduct"))
                {
                        generateProductRateReportByProduct(request, response);
                }
                else if(actionName.equals("generateProductLogReportByDateAndSection"))
                {
                        generateProductLogReportByDateAndSection(request, response);
                }
                else if(actionName.equals("generateProductLogCSVReportByDate"))
                {
                        generateProductLogCSVReportByDate(request, response);
                }
                else
                {
                        OhtuneLogger.error("Unknow action name in ReportController, actionName=" + actionName);
                }
        }

        private void generateProductRateReportByProduct(HttpServletRequest request,     HttpServletResponse response) throws IOException
        {
                UserAC sessionUser = new UserAC();
                if(request.getSession().getAttribute("user") != null)
                        sessionUser = (UserAC)request.getSession().getAttribute("user");
                
                String product_name = request.getParameter("product_name");
                
                IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
                Product product = service.getProductByName(product_name);
                List<ProductRate> rates = service.generateProductRateByProduct(product, sessionUser);
                
                Gson gson = service.getGson();
                
                JsonDataWrapper dw = new JsonDataWrapper(rates, JsonDataWrapper.TYPE_PRODUCT_RATE);
                response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
        }
        
        private void generateProductLogReportByDateAndSection(HttpServletRequest request,       HttpServletResponse response) throws IOException
        {
                UserAC sessionUser = new UserAC();
                if(request.getSession().getAttribute("user") != null)
                        sessionUser = (UserAC)request.getSession().getAttribute("user");
                
                request.setCharacterEncoding("utf-8");
                response.setCharacterEncoding("utf-8");
                
                String sDate = request.getParameter("date");
                String sEndDate = request.getParameter("end_date");
                String sJobType = request.getParameter("job_type");
                
                IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
                
                List<ProductionLog> logs = service.generateProductLogByDateAndSection(sDate, sEndDate, sJobType, sessionUser);
                
                Gson gson = service.getGson();
                
                JsonDataWrapper dw = new JsonDataWrapper(logs, JsonDataWrapper.TYPE_DEFAULT);
                UtilityFunc.fillImageDrawingForProductLog(dw.getData(), service);
                response.getOutputStream().write(gson.toJson(dw).getBytes("utf-8"));
        }
        
        private void generateProductLogCSVReportByDate(HttpServletRequest request,      HttpServletResponse response) throws IOException
        {
                UserAC sessionUser = new UserAC();
                if(request.getSession().getAttribute("user") != null)
                        sessionUser = (UserAC)request.getSession().getAttribute("user");
                
                request.setCharacterEncoding("utf-8");
                response.setCharacterEncoding("utf-8");
                
                String sDate = request.getParameter("date");
                String sEndDate = request.getParameter("end_date");
                
                IOhtuneService service = (IOhtuneService)OhtuneServiceHolder.getInstence().getBeanFactory().getBean("uhtuneService");
                
                int rowNum = 0;
                HSSFWorkbook wb = new HSSFWorkbook();
                HSSFSheet sheet;
                HSSFRow row;
                HSSFCell cell; 
                
                CellStyle cs = wb.createCellStyle();
            cs.setWrapText(true);
            
                List<JobType> jobTypes = service.getAllJobType(false);
                List<ProductionLog> logs;
                
                for(int ji = 0; ji < jobTypes.size(); ji++)
                {
                        if(jobTypes.get(ji).getName().equals(JobType.FINISH_DEPOT) || 
                                        jobTypes.get(ji).getName().equals(JobType.FINISH_SEMI_FINISH))
                                continue;
                        
                        rowNum = 0;
                        sheet = wb.createSheet(jobTypes.get(ji).getName());
                        row = sheet.createRow((short)(rowNum++));
                        cell = row.createCell((short)0); 
                        cell.setCellValue("统计数据从 " + sDate + " 到 " + sEndDate);
                        
                        logs = service.generateProductLogByDateAndSection(sDate, sEndDate, jobTypes.get(ji).getName(), sessionUser);
                        
                        row = sheet.createRow((short)(rowNum++));
                        row.createCell((short)0).setCellValue("部门");
                        row.createCell((short)1).setCellValue(jobTypes.get(ji).getName());
                        
                        row = sheet.createRow((short)rowNum++);
                        row.createCell((short)0).setCellValue("产品名称");
                        row.createCell((short)1).setCellValue("完成总数");
                        row.createCell((short)2).setCellValue("废品数");
                        row.createCell((short)3).setCellValue("返工数");
                        row.createCell((short)4).setCellValue("订单");
                        row.createCell((short)5).setCellValue("生产期限");
                        
                        for (int i = 0; i < logs.size(); i++) {
                                row = sheet.createRow((short)rowNum++);
                                row.createCell((short)0).setCellValue(logs.get(i).getProduct_our_name());
                                row.createCell((short)1).setCellValue(logs.get(i).getFinished().toString());
                                row.createCell((short)2).setCellValue(logs.get(i).getDisuse().toString());
                                row.createCell((short)3).setCellValue(logs.get(i).getRejected().toString());
                                
                    String[] orderStr = logs.get(i).orders.split(" ");
                    String order = ""; 
                    for(int n = 0; n < orderStr.length; n++)
                    {
                        order += orderStr[n] + "\n";
                    }
                    cell = row.createCell((short)4);
                    cell.setCellStyle(cs);
                    cell.setCellValue(order);
                    
                    String[] deadlineStr = logs.get(i).deadlines.split(" ");
                    String deadline = "";
                    for(int n = 0; n < deadlineStr.length; n++)
                    {
                        deadline += deadlineStr[n] + "\n";
                    }
                    cell = row.createCell((short)5);
                    cell.setCellStyle(cs);
                    cell.setCellValue(deadline);
                    
                    row.setHeightInPoints((( deadlineStr.length + 1) *sheet.getDefaultRowHeightInPoints()));
                }
                        row = sheet.createRow((short)rowNum++);
                        sheet.autoSizeColumn((short)4);
                        sheet.autoSizeColumn((short)5);
                }
        
        response.setHeader("Content-Disposition", "attachment; filename=data.xls");
                wb.write(response.getOutputStream());
                response.getOutputStream().flush();
                response.getOutputStream().close();
        }
}