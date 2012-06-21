package tk.solaapps.ohtune.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import tk.solaapps.ohtune.dao.ICustomerDao;
import tk.solaapps.ohtune.dao.IDepartmentDao;
import tk.solaapps.ohtune.dao.IDivisionDao;
import tk.solaapps.ohtune.dao.IDummyDao;
import tk.solaapps.ohtune.dao.IJobDao;
import tk.solaapps.ohtune.dao.IJobTypeDao;
import tk.solaapps.ohtune.dao.IMoldDao;
import tk.solaapps.ohtune.dao.IOrderDao;
import tk.solaapps.ohtune.dao.IPostDao;
import tk.solaapps.ohtune.dao.IProductDao;
import tk.solaapps.ohtune.dao.IProductLogDao;
import tk.solaapps.ohtune.dao.IRoleDao;
import tk.solaapps.ohtune.dao.ISectionDao;
import tk.solaapps.ohtune.dao.ISequenceDao;
import tk.solaapps.ohtune.dao.IUserACDao;
import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Department;
import tk.solaapps.ohtune.model.Division;
import tk.solaapps.ohtune.model.Dummy;
import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.Mold;
import tk.solaapps.ohtune.model.Order;
import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.Product;
import tk.solaapps.ohtune.model.ProductLog;
import tk.solaapps.ohtune.model.Role;
import tk.solaapps.ohtune.model.Section;
import tk.solaapps.ohtune.model.Sequence;
import tk.solaapps.ohtune.model.UserAC;
import tk.solaapps.ohtune.pattern.OhtuneLogger;

public class OhtuneDA implements IOhtuneDA{
        protected IDummyDao dummyDao = null;
        protected IUserACDao userACDao = null;
        protected IDepartmentDao deptDao = null;
        protected ISequenceDao seqDao = null;
        protected IDivisionDao divDao = null;
        protected ISectionDao secDao = null;
        protected IRoleDao roleDao = null;
        protected IPostDao postDao = null;
        protected IOrderDao orderDao = null;
        protected IProductDao productDao = null;
        protected IJobTypeDao jobTypeDao = null;
        protected IJobDao jobDao = null;
        protected ICustomerDao customerDao = null;
        protected IMoldDao moldDao = null;
        protected IProductLogDao productLogDao = null;
        
        public void setUserACDao(IUserACDao userACDao)
        {
                this.userACDao = userACDao;
        }
        public void setDummyDao(IDummyDao dummyDao)
        {
                this.dummyDao = dummyDao;
        }
        public void setDepartmentDao(IDepartmentDao deptDao)
        {
                this.deptDao = deptDao;
        }
        public void setSequenceDao(ISequenceDao seqDao)
        {
                this.seqDao = seqDao;
        }
        public void setDivisionDao(IDivisionDao divDao)
        {
                this.divDao = divDao;
        }
        public void setSectionDao(ISectionDao secDao)
        {
                this.secDao = secDao;
        }
        public void setRoleDao(IRoleDao roleDao)
        {
                this.roleDao = roleDao;
        }
        public void setPostDao(IPostDao postDao)
        {
                this.postDao = postDao;
        }
        public void setOrderDao(IOrderDao orderDao)
        {
                this.orderDao = orderDao;
        }
        public void setProductDao(IProductDao productDao)
        {
                this.productDao = productDao;
        }
        public void setJobTypeDao(IJobTypeDao jobTypeDao)
        {
                this.jobTypeDao = jobTypeDao;
        }
        public void setJobDao(IJobDao jobDao)
        {
                this.jobDao = jobDao;
        }
        public void setCustomerDao(ICustomerDao customerDao)
        {
                this.customerDao = customerDao;
        }
        public void setMoldDao(IMoldDao moldDao)
        {
                this.moldDao = moldDao;
        }
        public void setProductLogDao(IProductLogDao productLogDao)
        {
                this.productLogDao = productLogDao;
        }
        
        @Override
        public List<Department> getAllDepartment()
        {
                return deptDao.getAllDepartment();
        }
        
        @Override
        public boolean addDepartment(Department dept)
        {
                if(dept == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_DEPARTMENT);
                
                if(seq != null)
                {
                        dept.setId(seq.getValue());
                        return deptDao.addDepartment(dept);
                }
                else
                {
                        return false;
                }
        }
        
        @Override
        public Department getDepartmentById(Long id)
        {
                return deptDao.getDepartmentById(id);
        }
        
        @Override
        public Department getDepartmentByName(String name)
        {
                return deptDao.getDepartmentByName(name);
        }
        
        @Override
        public boolean addDivision(Division division)
        {
                if(division == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_DIVISION);
                
                if(seq != null)
                {
                        division.setId(seq.getValue());
                        return divDao.addDivision(division);
                }
                else
                {
                        return false;
                }
        }
        
        @Override
        public List<Division> getAllDivision()
        {
                return divDao.getAllDivision();
        }
        
        @Override
        public List<Division> getDivisionByDepartment(Department dept)
        {
                return divDao.getDivisionByDepartment(dept);
        }
        
        @Override
        public Division getDivisionByName(String name)
        {
                return divDao.getDivisionByName(name);
        }
        
        @Override
        public Division getDivisionById(Long id)
        {
                return divDao.getDivisionById(id);
        }
        
        @Override
        public boolean addSection(Section section)
        {
                if(section == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_SECTION);
                
                if(seq != null)
                {
                        section.setId(seq.getValue());
                        return secDao.addSection(section);
                }
                else
                {
                        return false;
                }
        }
        
        @Override
        public List<Section> getAllSection()
        {
                return secDao.getAllSection();
        }
        
        @Override
        public List<Section> getSectionByDivision(Division division)
        {
                return secDao.getSectionByDivision(division);
        }
        
        @Override
        public Section getSectionByName(String name)
        {
                return secDao.getSectionByName(name);
        }
        
        @Override
        public Section getSectionById(Long id)
        {
                return secDao.getSectionById(id);
        }
        
        @Override
        public boolean addRole(Role role)
        {
                if(role == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_ROLE);
                if(seq != null)
                {
                        role.setId(seq.getValue());
                        return roleDao.addRole(role);
                }
                else
                        return false;
                
        }
        
        @Override
        public List<Role> getAllRole()
        {
                return roleDao.getAllRole();
        }
        
        @Override
        public Role getRoleByName(String name)
        {
                return roleDao.getRoleByName(name);
        }
        
        @Override
        public Role getRoleById(Long id)
        {
                return roleDao.getRoleById(id);
        }
        
        @Override
        public boolean addPost(Post post)
        {
                if(post == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_POST);
                if(seq != null)
                {
                        post.setId(seq.getValue());
                        return postDao.addPost(post);
                }
                else
                        return false;
        }
        
        @Override
        public List<Post> getAllPost()
        {
                return postDao.getAllPost();
        }
        
        @Override
        public List<Post> getPostBySection(Section section)
        {
                return postDao.getPostBySection(section);
        }
        
        @Override
        public Post getPostById(Long id)
        {
                return postDao.getPostById(id);
        }
        
        @Override
        public Post getPostByName(String name)
        {
                return postDao.getPostByName(name);
        }
        @Override
        public boolean deletePost(Post post)
        {
                return postDao.deletePost(post);
        }
        
        @Override
        public List<Dummy> getAllDummy() {
                return dummyDao.getAllDummy();
        }
        
        @Override
        public boolean addUserAC(UserAC userac) {
                if(userac == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_USERAC);
                if(seq == null)
                        return false;
                else
                {
                        userac.setId(seq.getValue());
                        return userACDao.addUserAC(userac); 
                }
        }
        
        @Override
        public List<UserAC> getAllUserAC() {
                return userACDao.getAllUserAC();
        }
        
        @Override
        public UserAC getUserACByPost(Post post) {
                return userACDao.getUserACByPost(post);
        }
        
        @Override
        public UserAC getUserACById(Long id) {
                return userACDao.getUserACById(id);
        }
        
        @Override
        public UserAC getUserACByLoginId(String login_id) {
                return userACDao.getUserACByLoginId(login_id);
        }
        
        @Override
        public boolean updateUserAC(UserAC userac) {
                return userACDao.updateUserAC(userac);
        }
        
        @Override
        public boolean deleteUserAC(UserAC userac)
        {
                String[] columns = new String[]{ "userac" };
                Object[] values = new Object[]{userac};
                String[] values2 = new String[]{userac.getName()};
                
                List<Job> jobs = searchJob(columns, values, null, null, 0, 1, null, false);
                if(jobs.size() > 0)
                {
                        OhtuneLogger.error("Delete userac failed, some jobs finished by the user is still in progress");
                        return false;
                }
                
                columns = new String[]{ "assigned_to" };
                jobs = searchJob(columns, values, null, null, 0, 1, null, false);
                if(jobs.size() > 0)
                {
                        OhtuneLogger.error("Delete userac failed, some jobs assigned to the user is still in progress");
                        return false;
                }
                
                columns = new String[]{ "creator" };
                List<Order> orders = searchOrder(columns, values2, null, null, 0, 1, null, false);
                if(orders.size() > 0)
                {
                        OhtuneLogger.error("Delete userac failed, some order created by the user is still in progress");
                        return false;
                }
                
                return userACDao.deleteUserAC(userac);
        }
        
        @Override
        public Sequence getNextValueByName(String name) {
                return seqDao.getNextValueByName(name);
        }
        
        
        @Override
        public boolean addOrder(Order order) {
                if(order == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_ORDER);
                if(seq == null)
                        return false;
                else
                {
                        order.setId(seq.getValue());
                        return orderDao.addOrder(order);
                }
        }
        @Override
        public boolean updateOrder(Order order) {
                return orderDao.updateOrder(order);
        }
        @Override
        public boolean deleteOrder(Order order) {
                Product product = this.getProductByName(order.getProduct_name());
                if(product != null && !order.getStatus().equals(Order.STATUS_APPROVING) && !order.getStatus().equals(Order.STATUS_FINISHED))
                {
                        product.setFinished(product.getFinished() + order.getUse_finished());
                        product.setSemi_finished(product.getSemi_finished() + order.getUse_semi_finished());
                        this.updateProduct(product);
                }
                return orderDao.deleteOrder(order);
        }
        @Override
        public List<Order> searchOrder(String[] columns, Object[] values, String[] inClause, Collection[] in,
                        int start, int limit, String orderby, boolean sortAsc) {
                return orderDao.searchOrder(columns, values, inClause, in, start, limit, orderby, sortAsc);
        }
        @Override 
        public Order getOrderById(Long id)
        {
                return orderDao.getOrderById(id);
        }
        @Override
        public List<Product> getAllProduct(boolean includeDisabled) {
                return productDao.getAllProduct(includeDisabled);
        }
        @Override
        public boolean updateProduct(Product product) {
                return productDao.updateProduct(product);
        }
        @Override
        public boolean addProduct(Product product)
        {
                return productDao.addProduct(product);
        }
        @Override
        public Product getProductByName(String name)
        {
                return productDao.getProductByName(name);
        }
        @Override
        public boolean deleteProduct(Product product)
        {
                String[] columns = new String[]{ "product_name" };
                Object[] values = new Object[]{ product.getName() };
                List<Order> orders = searchOrder(columns, values, null, null, 0, 1,null, false);
                if(orders.size() > 0)
                {
                        OhtuneLogger.error("Delete product failed, order with this product is created");
                        return false;
                }
                return productDao.deleteProduct(product);
        }
        
        @Override
        public List<JobType> getAllJobType(boolean includeDisabled) {
                return jobTypeDao.getAllJobType(includeDisabled);
        }
        @Override
        public boolean updateJobType(JobType jobType) {
                return jobTypeDao.updateJobType(jobType);
        }
        @Override
        public JobType getJobTypeByName(String name)
        {
                return jobTypeDao.getJobTypeByName(name);
        }
        @Override
        public boolean addJob(Job job) {
                if(job == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_JOB);
                if(seq == null)
                        return false;
                else
                {
                        job.setId(seq.getValue());
                        return jobDao.addJob(job);
                }
        }
        @Override
        public boolean updateJob(Job job) {
                return jobDao.updateJob(job);
        }
        @Override
        public boolean deleteJob(Job job)
        {
                return jobDao.deleteJob(job);
        }
        @Override
        public List<Job> getJobByOrder(Order order) {
                return jobDao.getJobByOrder(order);
        }
        @Override
        public List<Job> searchJob(String[] columns, Object[] values,String[] inClause, Collection[] in, int start,
                        int limit, String orderby, boolean sortAsc) {
                return jobDao.searchJob(columns, values, inClause, in, start, limit, orderby, sortAsc);
        }
        @Override
        public Job getJobById(Long id)
        {
                return jobDao.getJobById(id);
        }
        
        
        @Override
        public List<Job> getJobByCompDateAndSection(Date compDate, JobType jobType) {
                return jobDao.getJobByCompDateAndSection(compDate, jobType);
        }
        @Override
        public List<Customer> getAllCustomer() {
                return customerDao.getAllCustomer();
        }
        @Override
        public Customer getCustomerById(Long id) {
                return customerDao.getCustomerById(id);
        }
        @Override
        public boolean addCustomer(Customer customer) {
                if(customer == null)
                        return false;
                
                Sequence seq = seqDao.getNextValueByName(Sequence.SEQ_NAME_CUSTOMER);
                if(seq == null)
                        return false;
                else
                {
                        customer.setId(seq.getValue());
                        return customerDao.addCustomer(customer);
                }
        }
        @Override
        public boolean updateCustomer(Customer customer) {
                return customerDao.updateCustomer(customer);
        }
        @Override
        public boolean deleteCustomer(Customer customer)
        {
                String[] columns = new String[]{ "customer_code" };
                Object[] values = new Object[]{ customer.getCode() };
                List<Order> order = orderDao.search(columns, values, null, null, 0, 1, null, false);
                if(order.size() > 0)
                {
                        OhtuneLogger.error("Delete customer failed, some order were created with this customer");
                        return false;
                }
                return customerDao.deleteCustomer(customer);
        }
        
        @Override
        public boolean addMold(Mold mold) {
                return moldDao.addMold(mold);
        }
        @Override
        public boolean deleteMold(Mold mold) {
                String[] columns = new String[]{ "mold" };
                Object[] values = new Object[]{ mold };
                List<Product> products = productDao.search(columns, values, null, null, 0, 1, null, false);
                if(products.size() > 0)
                {
                        OhtuneLogger.error("Delete mold failed, mold is in use");
                        return false;
                }
                return moldDao.deleteMold(mold);
        }
        
        @Override
        public List<Mold> getAllMold() {
                return moldDao.getAllMold();
        }
        @Override
        public Mold getMoldByCode(String code) {
                return moldDao.getMoldByCode(code);
        }
        
        @Override
        public List search(String[] columns, Object[] values, String[] inClause,
                        Collection[] in, int start, int limit, String orderby, boolean sortAsc) {
                return new ArrayList();
        }
        
        @Override
        public boolean addProductLog(ProductLog log) {
                return productLogDao.addProductLog(log);
        }
        
        @Override
        public List<ProductLog> getProductLogByDateAndSection(Date date, Date endDate,
                        String sectionName) {
                return productLogDao.getProductLogByDateAndSection(date, endDate, sectionName);
        }
        
        
}