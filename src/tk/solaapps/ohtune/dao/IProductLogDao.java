package tk.solaapps.ohtune.dao;

import java.util.Date;
import java.util.List;

import tk.solaapps.ohtune.model.Job;
import tk.solaapps.ohtune.model.JobType;
import tk.solaapps.ohtune.model.ProductLog;

public interface IProductLogDao {
        boolean addProductLog(ProductLog log);
        List<ProductLog> getProductLogByDateAndSection(Date date, Date endDate, String sectionName);
}