package tk.solaapps.ohtune.dao;

import java.util.Date;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.ProductLog;

public class ProductLogDaoImpl extends BaseDao implements IProductLogDao{

        @Override
        protected Class getModelClass() {
                return ProductLog.class;
        }

        @Override
        public boolean addProductLog(ProductLog log) {
                getSession().save(log);
                return true;
        }

        @Override
        public List<ProductLog> getProductLogByDateAndSection(Date date, Date endDate,
                        String sectionName) {
                
                Date from = new Date();
                from.setYear(date.getYear());
                from.setMonth(date.getMonth());
                from.setDate(date.getDate());
                from.setHours(0);
                from.setMinutes(0);
                from.setSeconds(0);
                
                Date to = new Date();
                to.setYear(endDate.getYear());
                to.setMonth(endDate.getMonth());
                to.setDate(endDate.getDate());
                to.setHours(23);
                to.setMinutes(59);
                to.setSeconds(59);
                
                List<ProductLog> logs = getSession().createCriteria(ProductLog.class).add(Restrictions.eq("section_name", sectionName))
                                .add(Restrictions.between("process_date", from, to))
                                .addOrder(org.hibernate.criterion.Order.asc("product_name")).list();
                
                return logs;
        }
        
        
}