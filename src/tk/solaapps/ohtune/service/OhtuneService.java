package tk.solaapps.ohtune.service;

import java.io.File;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import tk.solaapps.ohtune.model.Dummy;
import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.OhtuneDocument;
import tk.solaapps.ohtune.model.Order;
import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.ProductLog;
import tk.solaapps.ohtune.model.ProductRate;
import tk.solaapps.ohtune.model.ProductionLog;
import tk.solaapps.ohtune.model.Role;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.JsonResponse;
import tk.solaapps.ohtune.pattern.OhtuneLogger;
import tk.solaapps.ohtune.pattern.SystemConfig;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class OhtuneService extends OhtuneDA implements IOhtuneService {

        private Gson gson = null;
        private SystemConfig config;

        @Override
        public boolean test(UserAC userac) throws Exception {
                List<Dummy> dummyList = dummyDao.getAllDummy();
                return dummyList == null;
        }

        @Override
        public SystemConfig getSystemConfig() {
                return config;
        }

        @Override
        public void setSystemConfig(SystemConfig config) {
                this.config = config;
                File file = new File(config.getCommonDocumentFolder());
                if(true)
                {
                        if(!file.exists())
                        {
                                file.mkdirs();
                        }
                }
                
                file = new File(config.getProductDrawingFolder());
                if(true)
                {
                        if(!file.exists())
                        {
                                file.mkdirs();
                        }
                }
                
                file = new File(config.getProductImageFolder());
                if(true)
                {
                        if(!file.exists())
                        {
                                file.mkdirs();
                        }
                }
        }

        @Override
        public synchronized Gson getGson() {
                if (gson == null) {
                        gson = new GsonBuilder().setDateFormat("yyyy-MM-dd").create();
                }
                return gson;
        }

        @Override
        public JsonResponse genJsonResponse(boolean success, String msg, Object data) {
                return new JsonResponse(success, msg, data);
        }

        @Override
        public UserAC login(String login_id, String password) {
                OhtuneLogger.info("User login, userid=" + login_id);
                
                UserAC user = this.getUserACByLoginId(login_id);
                if (user == null)
                        return null;

                if (user.getPassword().equals(password)) {
                        return user;
                } else {
                        return null;
                }
        }
        
        @Override
        public boolean createUser(UserAC user, UserAC operator) {
                Post post = user.getPost();
                this.addPost(post);
                post = this.getPostByName(post.getName());
                user.setPost(post);
                
                OhtuneLogger.info("Create new user, userid=" + user.getLogin_id() + " by " + operator.getLogin_id());
                
                return this.addUserAC(user);
        }

        @Override
        public boolean deleteUser(UserAC user, UserAC operator) {
                Post post = user.getPost();
                
                OhtuneLogger.info("Delete user, userid=" + user.getLogin_id() + " by " + operator.getLogin_id());
                
                return this.deleteUserAC(user) & this.deletePost(post);
        }

        @Override
        public boolean createOrder(Order order, List<Job> jobs, UserAC operator) {
                SimpleDateFormat sm = new SimpleDateFormat("yyyyMMdd");
                Date date = new Date();
                String number = sm.format(date) + "-";
                
                String[] columns = new String[] { Order.COLUMN_NUMBER };
                String[] values = new String[] { number + "001" };
                
                int counter = 1;
                while(this.searchOrder(columns, values, null,null, 0, 1, "id", true).size() > 0)
                {
                        counter++;
                        values = new String[] { number + ("" + (1000 + counter)).substring(1) };
                }
                
                number = values[0];
                order.setNumber(number);
                OhtuneLogger.info("Create order, number=" + order.getNumber() + " by " + operator.getLogin_id());
                if(this.addOrder(order))
                {
                        //Product product = this.getProductByName(order.getProduct_name());
                        //product.setFinished(product.getFinished() - order.getUse_finished());
                        //product.setSemi_finished(product.getSemi_finished() - order.getUse_semi_finished());
                        boolean success = true; //this.updateProduct(product);
                        
                        order = this.searchOrder(columns, values,null,null, 0, 1, "id", true).get(0);
                        for(int i = 0; i < jobs.size(); i++)
                        {
                                jobs.get(i).setComplete_date(null);
                                jobs.get(i).setDeadline(order.getDeadline());
                                jobs.get(i).setFinished(0);
                                jobs.get(i).setTotal_rejected(0);
                                jobs.get(i).setOrders(order);
                                jobs.get(i).setStart_date(order.getCreate_date());
                                jobs.get(i).setStatus(Job.STATUS_APPROVING);
                                jobs.get(i).setUserac(operator);
                                success = success & this.addJob(jobs.get(i));
                        }
                        return success;
                }
                else
                {
                        return false;
                }
        }
        
        public boolean updateOrder(Order order, Product product, List<Job> newJobs, List<Job> deleteJobs, UserAC operator)
        {
                OhtuneLogger.info("Update ordrer, by " + operator.getLogin_id());
                boolean success = true;
                success = success & updateOrder(order);
                success = success & updateProduct(product);
                return success;
        }
        
        public boolean deleteJobByOrder(Order order, UserAC operator)
        {
                OhtuneLogger.info("Delete job by order, userid=" + " by " + operator.getLogin_id());
                List<Job> jobs = this.getJobByOrder(order);
                
                boolean success = true;
                for(int i = 0 ; i < jobs.size(); i++)
                {
                        success = success & this.deleteJob(jobs.get(i));
                }
                
                return success;
                
        }

        @Override
        public List<Job> getMyJobList(UserAC user) {
                
                List<JobType> jobTypes = this.getAllJobType(false);
                List<Role> userRoles = user.getPost().getRole();
                List<JobType> includeJobTypes = new ArrayList<JobType>();
                for(int i = 0; i < userRoles.size(); i++)
                {
                        for(int j = 0 ; j < jobTypes.size(); j++)
                        {
                                if(userRoles.get(i).getName().equals(Role.SUPERUSER_ADMIN) || userRoles.get(i).getName().equals(Role.SUPERUSER_MANAGER)
                                                || userRoles.get(i).getName().equals(Role.SUPERUSER_MANAGER2) || userRoles.get(i).getName().equals(Role.SUPERUSER_MANAGER3))
                                {
                                        includeJobTypes = jobTypes;
                                        break;
                                }
                                else if(userRoles.get(i).getId().equals(jobTypes.get(j).getRole().getId()))
                                {
                                        includeJobTypes.add(jobTypes.get(j));
                                }
                        }
                }
                List<String> status = new ArrayList<String>();
                
                status.add(Job.STATUS_PAUSED);
                status.add(Job.STATUS_PROCESSING);
                
                String[] inClause = new String[] {Job.COLUMN_JOBTYPE, Job.COLUMN_STATUS };
                Collection[] in = new Collection[]{includeJobTypes, status};
                List<Job> jobs = this.searchJob(null , null, inClause, in ,  0, 10000, "id", true);
                
                return jobs;
        }

        @Override
        public boolean completeJob(Job job, UserAC assignedTo, String jobType, int complete_count, int disuse_count,
                        boolean isCompleted, boolean isRejected, String remark, UserAC operator) {
                
                boolean success = true;
                
                job.setFinish_remark(remark);
                job.setRemaining(job.getRemaining() - (complete_count + disuse_count));
                if(isRejected)
                {
                        job.setTotal(job.getTotal() - complete_count);
                        if(job.getTotal() < 0)
                        {
                                throw new RuntimeException("返工数不能大于生产总数");
                        }
                }
                else
                        job.setFinished(job.getFinished() + complete_count);
                
                if(isCompleted || job.getRemaining() == 0)
                {
                        job.setComplete_date(new Date());
                        job.setRemaining(0);
                        job.setStatus(Job.STATUS_DONE);
                }
                success = success && this.updateJob(job);
                
                JobType jt = this.getJobTypeByName(jobType);
                
                if(jt == null)
                {
                        OhtuneLogger.info("Unknow job type selected to complete job, job type=" + jobType + " by " + operator.getLogin_id());
                        return false;
                }
                
                List<Job> jobByOrder = this.getJobByOrder(job.getOrders());
                Job existingJob = null;
                for(int i = 0 ; i < jobByOrder.size(); i++)
                {
                        if(jt.getName().equals(jobByOrder.get(i).getJob_type().getName()))
                        {
                                if(assignedTo == null && jobByOrder.get(i).getAssigned_to() == null)
                                {
                                        existingJob = jobByOrder.get(i);
                                        break;
                                }
                                else if(assignedTo != null && jobByOrder.get(i).getAssigned_to() != null)
                                {
                                        if(assignedTo.getId().longValue() == jobByOrder.get(i).getAssigned_to().getId().longValue())
                                        {
                                                existingJob = jobByOrder.get(i);
                                                break;
                                        }
                                }
                        }
                }
                
                if(existingJob == null)
                {
                        if(isRejected)
                                throw new RuntimeException("返工失败,找不到需要返工的工作和员工");
                        
                        Job newJob = new Job();
                        newJob.setComplete_date(null);
                        newJob.setDeadline(job.getOrders().getDeadline());
                        newJob.setFinish_remark("");
                        newJob.setJob_type(jt);
                        newJob.setOrders(job.getOrders());
                        newJob.setRemaining(complete_count);
                        newJob.setStart_date(new Date());
                        newJob.setStatus(Job.STATUS_PROCESSING);
                        newJob.setTotal(complete_count);
                        newJob.setUserac(operator);
                        newJob.setAssigned_to(assignedTo);
                        newJob.setPrevious_jobid(job.getId());
                        newJob.setTotal_rejected(0);
                        newJob.setFinished(0);
                        
                        if(newJob.getJob_type().getName().equals(JobType.FINISH_DEPOT) || 
                                        newJob.getJob_type().getName().equals(JobType.FINISH_SEMI_FINISH))
                        {
                                newJob.setComplete_date(new Date());
                                newJob.setRemaining(0);
                                newJob.setFinished(complete_count);
                                newJob.setStatus(Job.STATUS_DONE);
                                
                                Product product = this.getProductByName(newJob.getOrders().getProduct_name());
                                if(product != null)
                                {
                                        if(newJob.getJob_type().getName().equals(JobType.FINISH_DEPOT))
                                        {
                                                product.setFinished(product.getFinished() + complete_count);
                                        }
                                        else
                                        {
                                                product.setSemi_finished(product.getSemi_finished() + complete_count);
                                        }
                                        this.updateProduct(product);
                                }
                        }
                        success = this.addJob(newJob);
                }
                else
                {
                        if(existingJob.getJob_type().getName().equals(JobType.FINISH_DEPOT) || 
                                        existingJob.getJob_type().getName().equals(JobType.FINISH_SEMI_FINISH))
                        {
                                existingJob.setStart_date(new Date());
                                existingJob.setRemaining(0);
                                existingJob.setTotal(existingJob.getTotal() + complete_count);
                        }
                        else
                        {
                                existingJob.setStart_date(new Date());
                                existingJob.setRemaining(existingJob.getRemaining() + complete_count);
                                existingJob.setComplete_date(null);
                                existingJob.setStatus(Job.STATUS_PROCESSING);
                                if(isRejected)
                                {
                                        existingJob.setTotal_rejected(existingJob.getTotal_rejected() + complete_count);
                                        existingJob.setFinished(existingJob.getFinished() - complete_count);
                                        
                                        if(existingJob.getFinished() < 0)
                                                throw new RuntimeException("返工数不能大于员工的生产总数");
                                }
                                else
                                {
                                        existingJob.setTotal(existingJob.getTotal() + complete_count);
                                }
                        }
                        success = this.updateJob(existingJob);
                }
                ProductLog log = new ProductLog();
                log.setHandled_by(job.getAssigned_to() == null ? "" : job.getAssigned_to().getName());
                log.setId(null);
                log.setJobid(job.getId());
                log.setProcess_date(new Date());
                log.setProcess_type(isRejected ? ProductLog.TYPE_REJECTED : ProductLog.TYPE_FINISHED);
                log.setProduct_name(job.getOrders().getProduct_name());
                log.setProduct_our_name(job.getOrders().getProduct_our_name());
                log.setQuantity(complete_count);
                log.setSection_name(job.getJob_type().getName());
                
                success = success & this.addProductLog(log);
                
                if(disuse_count > 0){
                        ProductLog dlog = new ProductLog();
                        dlog.setHandled_by(job.getAssigned_to() == null ? "" : job.getAssigned_to().getName());
                        dlog.setId(null);
                        dlog.setJobid(job.getId());
                        dlog.setProcess_date(new Date());
                        dlog.setProcess_type(ProductLog.TYPE_DISUSE);
                        dlog.setProduct_name(job.getOrders().getProduct_name());
                        dlog.setProduct_our_name(job.getOrders().getProduct_our_name());
                        dlog.setQuantity(disuse_count);
                        dlog.setSection_name(job.getJob_type().getName());
                        
                        success = success & this.addProductLog(dlog);
                }
                
                return success;
        }

        @Override
        public boolean completeOrder(Order order, UserAC operator) {
                order.setStatus(Order.STATUS_FINISHED);
                return this.updateOrder(order);
        }

        @Override
        public List<ProductRate> generateProductRateByProduct(Product product,
                        UserAC operator) {
                
                OhtuneLogger.info("Get product rate by user, product=" + product.getName() + " by " + operator.getLogin_id());
                
                String[] columns = new String[] { Order.COLUMN_PRODUCT_NAME, Order.COLUMN_STATUS };
                String[] values = new String[] { product.getName(), Order.STATUS_FINISHED };
                
                List<Order> orders = searchOrder(columns, values, null,null,0, 100, "id", false);
                List<ProductRate> rates = new ArrayList<ProductRate>();
                
                ProductRate rate;
                List<Job> jobs;
                int total;
                int finished;
                NumberFormat percentFormatter =
                        NumberFormat.getPercentInstance();
                for(int i = 0 ; i < orders.size(); i++)
                {
                        total = orders.get(i).getQuantity();
                        finished = 0;
                        jobs = this.getJobByOrder(orders.get(i));
                        for(int j = 0; j < jobs.size(); j++)
                        {
                                if(jobs.get(j).getJob_type().getName().equals(JobType.FINISH_DEPOT) ||
                                                jobs.get(j).getJob_type().getName().equals(JobType.FINISH_SEMI_FINISH))
                                {
                                        finished += jobs.get(j).getTotal();
                                }
                        }
                        if(total == 0)
                        {
                                rate = new ProductRate();
                                rate.setOrder(orders.get(i));
                                rate.setProduct(this.getProductByName(orders.get(i).getProduct_name()));
                                rate.setRate("N/A");
                        }
                        else
                        {
                                double dRate = (double)(((double)finished) / ((double)total));
                                rate = new ProductRate();
                                rate.setOrder(orders.get(i));
                                rate.setProduct(this.getProductByName(orders.get(i).getProduct_name()));
                                rate.setRate(percentFormatter.format(dRate));   
                        }
                        rates.add(rate);
                }
                return rates;
        }
        
        

        @Override
        public List<ProductionLog> generateProductLogByDateAndSection(String sDate, String sEndDate,
                        String sJobType, UserAC operator) {
                
                OhtuneLogger.info("Get product log by user, section =" + sJobType + ", date = " + sDate + " to " + sEndDate + " by " + operator.getLogin_id());
                
                Date date = null;
                Date endDate = null;
                JobType jobType = null;
                List<ProductLog> logs = new ArrayList<ProductLog>();
                ProductLog log = null;
                ArrayList<ProductionLog> plogs = new ArrayList<ProductionLog>();
                ProductionLog plog = new ProductionLog();
                
                ProductionLog totalLog = new ProductionLog();
                totalLog.setProduct_our_name("总计");
                try
                {
                        date = new SimpleDateFormat("yyyy-MM-dd").parse(sDate);
                        endDate = new SimpleDateFormat("yyyy-MM-dd").parse(sEndDate);
                        jobType = this.getJobTypeByName(sJobType);
                        if(jobType == null)
                                return new ArrayList<ProductionLog>();
                        
                        logs = this.getProductLogByDateAndSection(date, endDate, jobType.getName());
                        String preProductName = "";
                        String orders = "";
                        String deadlines = "";
                        Order order = null;
                        Job job = null;
                        for(int i = 0 ; i < logs.size(); i++)
                        {
                                job = this.getJobById(logs.get(i).getJobid());
                                if(job != null)
                                {
                                        if(orders.indexOf(job.getOrders().getNumber()) < 0)
                                        {
                                                orders = job.getOrders().getNumber() + " " + orders;
                                                deadlines = new SimpleDateFormat("yyyy-MM-dd").format(job.getOrders().getDeadline()) + " " + deadlines;
                                        }
                                }
                                        
                                if(!logs.get(i).getProduct_name().equals(preProductName))
                                {
                                        if(!plog.getProduct_our_name().equals(""))
                                        {
                                                plog.deadlines = deadlines;
                                                plog.orders = orders;
                                                plogs.add(plog);
                                        }
                                        //totalLog.setDisuse(plog.getDisuse());
                                        //totalLog.setFinished(plog.getFinished());
                                        //totalLog.setRejected(plog.getRejected());
                                        
                                        plog = new ProductionLog();                                     
                                        preProductName = logs.get(i).getProduct_name();
                                        deadlines = "";
                                        orders = "";
                                }
                                
                                plog.setProduct_our_name(logs.get(i).getProduct_our_name());
                                plog.setProduct_name(logs.get(i).getProduct_name());
                                
                                if(logs.get(i).getProcess_type().equals(ProductLog.TYPE_FINISHED))
                                {
                                        plog.setFinished(plog.getFinished() + logs.get(i).getQuantity());
                                }
                                else if(logs.get(i).getProcess_type().equals(ProductLog.TYPE_REJECTED))
                                {
                                        plog.setRejected(plog.getRejected() + logs.get(i).getQuantity());
                                }
                                else if(logs.get(i).getProcess_type().equals(ProductLog.TYPE_DISUSE))
                                {
                                        plog.setDisuse(plog.getDisuse() + logs.get(i).getQuantity());
                                }
                        }
                        
                        if(plog.getProduct_our_name().equals(""))
                                plog.setProduct_our_name("没有数据");
                        plog.deadlines = deadlines;
                        plog.orders = orders;
                        plogs.add(plog);
                        
                        //totalLog.setDisuse(plog.getDisuse());
                        //totalLog.setFinished(plog.getFinished());
                        //totalLog.setRejected(plog.getRejected());
                        for(int i = 0; i < plogs.size(); i++)
                        {
                                totalLog.setDisuse(totalLog.getDisuse() + plogs.get(i).getDisuse());
                                totalLog.setFinished(totalLog.getFinished() + plogs.get(i).getFinished());
                                totalLog.setRejected(totalLog.getRejected() + plogs.get(i).getRejected());
                        }
                        plogs.add(totalLog);
                        
                }
                catch(Exception e)
                {
                        OhtuneLogger.error(e, "Get product log by user failure");
                        plogs = new ArrayList<ProductionLog>();
                        ProductionLog errLog = new ProductionLog();
                        errLog.setProduct_our_name("出现错误");
                        plogs.add(errLog);
                        return plogs;
                }
                return plogs;
        }

        @Override
        public boolean approveOrder(Order order, List<Job> jobs, UserAC operator) {
                
                OhtuneLogger.info("Save order, number=" + order.getNumber() + " by " + operator.getLogin_id());
                order.setStatus(Order.STATUS_PROCESSING);
                if(this.updateOrder(order))
                {
                        Product product = this.getProductByName(order.getProduct_name());
                        product.setFinished(product.getFinished() - order.getUse_finished());
                        product.setSemi_finished(product.getSemi_finished() - order.getUse_semi_finished());
                        boolean success = this.updateProduct(product);
                        
                        for(int i = 0; i < jobs.size(); i++)
                        {
                                jobs.get(i).setComplete_date(null);
                                jobs.get(i).setDeadline(order.getDeadline());
                                jobs.get(i).setFinished(0);
                                jobs.get(i).setTotal_rejected(0);
                                jobs.get(i).setOrders(order);
                                jobs.get(i).setStart_date(new Date());
                                jobs.get(i).setStatus(Job.STATUS_PROCESSING);
                                jobs.get(i).setUserac(operator);
                                success = success & this.addJob(jobs.get(i));
                        }
                        if(order.getUse_semi_finished() > 0)
                        {
                                Job job = new Job();
                                job.setAssigned_to(null);
                                job.setComplete_date(null);
                                job.setDeadline(order.getDeadline());
                                job.setFinish_remark("");
                                job.setFinished(0);
                                job.setJob_type(this.getJobTypeByName(JobType.FINISH_SEMI_FINISH));
                                job.setOrders(order);
                                job.setPrevious_jobid(null);
                                job.setRemaining(order.getUse_semi_finished());
                                job.setStart_date(new Date());
                                job.setStatus(Job.STATUS_PROCESSING);
                                job.setTotal(order.getUse_semi_finished());
                                job.setTotal_rejected(0);
                                job.setUserac(operator);
                                success = success & this.addJob(job);
                        }
                        return success;
                }
                else
                {
                        return false;
                }
        }

        @Override
        public boolean pauseOrder(Order order, UserAC operator) {
                OhtuneLogger.info("Pause order, number=" + order.getNumber() + " by " + operator.getLogin_id());
                order.setStatus(Order.STATUS_PAUSED);
                boolean success = this.updateOrder(order);
                List<Job> jobByOrder = this.getJobByOrder(order);
                for(int i = 0; i < jobByOrder.size(); i++)
                {
                        if(jobByOrder.get(i).getStatus().equals(Job.STATUS_PROCESSING))
                        {
                                jobByOrder.get(i).setStatus(Job.STATUS_PAUSED);
                                success = success & this.updateJob(jobByOrder.get(i));
                        }
                }
                return success;
        }

        @Override
        public boolean cancelOrder(Order order, UserAC operator) {
                OhtuneLogger.info("Cancel order, number=" + order.getNumber() + " by " + operator.getLogin_id());
                
                Product product = this.getProductByName(order.getProduct_name());
                if(product != null && !order.getStatus().equals(Order.STATUS_APPROVING) && !order.getStatus().equals(Order.STATUS_FINISHED))
                {
                        product.setFinished(product.getFinished() + order.getUse_finished());
                        product.setSemi_finished(product.getSemi_finished() + order.getUse_semi_finished());
                        this.updateProduct(product);
                }
                
                order.setStatus(Order.STATUS_CANCELED);
                
                boolean success = this.updateOrder(order);
                List<Job> jobByOrder = this.getJobByOrder(order);
                for(int i = 0; i < jobByOrder.size(); i++)
                {
                        jobByOrder.get(i).setStatus(Job.STATUS_CANCELED);
                        success = success & this.updateJob(jobByOrder.get(i));
                }
                return success;
        }

        @Override
        public boolean resumeOrder(Order order, UserAC operator) {
                OhtuneLogger.info("Resume order, number=" + order.getNumber() + " by " + operator.getLogin_id());
                order.setStatus(Order.STATUS_PROCESSING);
                boolean success = this.updateOrder(order);
                List<Job> jobByOrder = this.getJobByOrder(order);
                for(int i = 0; i < jobByOrder.size(); i++)
                {
                        if(jobByOrder.get(i).getStatus().equals(Job.STATUS_PAUSED))
                        {
                                jobByOrder.get(i).setStatus(Job.STATUS_PROCESSING);
                                success = success & this.updateJob(jobByOrder.get(i));
                        }
                }
                return success;
        }

        @Override
        public boolean addJobToOrder(Order order, JobType jobType, int iQuantity, UserAC assignedTo,
                        UserAC operator) {
                
                OhtuneLogger.info("Add job to order, number=" + order.getNumber() + " by " + operator.getLogin_id());
                
                List<Job> jobByOrder = this.getJobByOrder(order);
                Job existingJob = null;
                boolean success = true;
                
                for(int i = 0 ; i < jobByOrder.size(); i++)
                {
                        if(jobType.getName().equals(jobByOrder.get(i).getJob_type().getName()))
                        {
                                if(assignedTo == null && jobByOrder.get(i).getAssigned_to() == null)
                                {
                                        existingJob = jobByOrder.get(i);
                                        break;
                                }
                                else if(assignedTo != null && jobByOrder.get(i).getAssigned_to() != null)
                                {
                                        if(assignedTo.getId().longValue() == jobByOrder.get(i).getAssigned_to().getId().longValue())
                                        {
                                                existingJob = jobByOrder.get(i);
                                                break;
                                        }
                                }
                        }
                }
                
                if(existingJob == null)
                {       
                        Job newJob = new Job();
                        newJob.setComplete_date(null);
                        newJob.setDeadline(order.getDeadline());
                        newJob.setFinish_remark("");
                        newJob.setJob_type(jobType);
                        newJob.setOrders(order);
                        newJob.setRemaining(iQuantity);
                        newJob.setStart_date(new Date());
                        newJob.setStatus(Job.STATUS_PROCESSING);
                        newJob.setTotal(iQuantity);
                        newJob.setUserac(operator);
                        newJob.setAssigned_to(assignedTo);
                        newJob.setPrevious_jobid(null);
                        newJob.setTotal_rejected(0);
                        newJob.setFinished(0);
                        
                        if(newJob.getJob_type().getName().equals(JobType.FINISH_DEPOT) || 
                                        newJob.getJob_type().getName().equals(JobType.FINISH_SEMI_FINISH))
                        {
                                newJob.setComplete_date(new Date());
                                newJob.setRemaining(0);
                                newJob.setFinished(iQuantity);
                                newJob.setStatus(Job.STATUS_DONE);
                                
                                Product product = this.getProductByName(newJob.getOrders().getProduct_name());
                                if(product != null)
                                {
                                        if(newJob.getJob_type().getName().equals(JobType.FINISH_DEPOT))
                                        {
                                                product.setFinished(product.getFinished() + iQuantity);
                                        }
                                        else
                                        {
                                                product.setSemi_finished(product.getSemi_finished() + iQuantity);
                                        }
                                        this.updateProduct(product);
                                }
                        }
                        success = this.addJob(newJob);
                }
                else
                {
                        if(existingJob.getJob_type().getName().equals(JobType.FINISH_DEPOT) || 
                                        existingJob.getJob_type().getName().equals(JobType.FINISH_SEMI_FINISH))
                        {
                                existingJob.setStart_date(new Date());
                                existingJob.setRemaining(0);
                                existingJob.setTotal(existingJob.getTotal() + iQuantity);
                        }
                        else
                        {
                                existingJob.setStart_date(new Date());
                                existingJob.setRemaining(existingJob.getRemaining() + iQuantity);
                                existingJob.setComplete_date(null);
                                existingJob.setStatus(Job.STATUS_PROCESSING);
                                existingJob.setTotal(existingJob.getTotal() + iQuantity);
                        }
                        success = this.updateJob(existingJob);
                }
                
                return success;
        }
        
        public List<OhtuneDocument> getAllDocument(UserAC operator)
        {
                List<OhtuneDocument> docs = new ArrayList<OhtuneDocument>();
                try
                {
                        File file = new File(getSystemConfig().getCommonDocumentFolder());
                        if(file.exists() && file.isDirectory())
                        {
                                File[] files = file.listFiles();
                                for(int i = 0 ; i < files.length; i++)
                                {
                                        OhtuneDocument doc = new OhtuneDocument();
                                        doc.setFull_path(files[i].getAbsolutePath());
                                        doc.setName(files[i].getName());
                                        if(files[i].getName().lastIndexOf(".") > 0)
                                        {
                                                doc.setType(files[i].getName().substring(files[i].getName().lastIndexOf(".") + 1).toUpperCase());
                                        }
                                        else
                                        {
                                                continue;
                                        }
                                        docs.add(doc);
                                }
                        }
                        else
                        {
                                OhtuneDocument doc = new OhtuneDocument();
                                doc.setFull_path("请查看日志");
                                doc.setName("出现错误");
                                doc.setType("");
                                docs.add(doc);
                        }
                }
                catch(Exception e)
                {
                        OhtuneLogger.error(e, "获取文件列表失败");
                        OhtuneDocument doc = new OhtuneDocument();
                        doc.setFull_path("请查看日志");
                        doc.setName("出现错误");
                        doc.setType("");
                        docs.add(doc);
                }
                
                return docs;
        }

        @Override
        public boolean deleteDocument(String name, UserAC operator) {
                OhtuneLogger.info("Delete document name =" + name + " by " + operator.getLogin_id());
                try
                {
                        String fileName = getSystemConfig().getCommonDocumentFolder() + "/" + name;
                        File file = new File(fileName);
                        if(!file.isDirectory() && file.exists())
                        {
                                return file.delete();
                        }
                }
                catch(Exception e)
                {
                        OhtuneLogger.error(e, "Delete document failed");
                        return false;
                }
                return false;
        }
        
        
}