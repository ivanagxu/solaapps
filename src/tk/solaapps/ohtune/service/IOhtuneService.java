package tk.solaapps.ohtune.service;

import java.util.List;

import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.OhtuneDocument;
import tk.solaapps.ohtune.model.Order;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.ProductRate;
import tk.solaapps.ohtune.model.ProductionLog;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.SystemConfig;

import com.google.gson.Gson;


public interface IOhtuneService extends IOhtuneDA{
        boolean test(UserAC userac) throws Exception;
        SystemConfig getSystemConfig();
        void setSystemConfig(SystemConfig config);
        
        //Business Functions
        UserAC login(String login_id, String password);
        Gson getGson();
        JsonResponse genJsonResponse(boolean success, String msg, Object Data);
        
        boolean createUser(UserAC user, UserAC operator);
        boolean deleteUser(UserAC user, UserAC operator);
        boolean createOrder(Order order, List<Job> jobs, UserAC operator);
        boolean approveOrder(Order order, List<Job> jobs, UserAC operator);
        boolean updateOrder(Order order, Product product, List<Job> newJobs, List<Job> deleteJobs, UserAC operator);
        boolean pauseOrder(Order order, UserAC operator);
        boolean cancelOrder(Order order, UserAC operator);
        boolean resumeOrder(Order order, UserAC operator);
        
        boolean deleteJobByOrder(Order order, UserAC operator);
        List<Job> getMyJobList(UserAC user);
        
        boolean completeJob(Job job, UserAC assignedTo, String jobType, int complete_count, int iDisuse_count, boolean isCompleted, boolean isRejected, String remark, UserAC operator);
        boolean addJobToOrder(Order order,JobType jobType, int iQuantity, UserAC assignedTo, UserAC operator);
        boolean completeOrder(Order order, UserAC operator);
        
        //Document fucntions
        List<OhtuneDocument> getAllDocument(UserAC operator);
        boolean deleteDocument(String name, UserAC operator);
        
        //Report Functions
        List<ProductRate> generateProductRateByProduct(Product product, UserAC operator);
        List<ProductionLog> generateProductLogByDateAndSection(String sDate, String sEndDate, String sJobType, UserAC operator);
}