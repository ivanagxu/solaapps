package tk.solaapps.ohtune.service;

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

public interface IOhtuneDA extends IDummyDao, IDepartmentDao, IDivisionDao, ISectionDao,
		IPostDao,IRoleDao, IUserACDao, ISequenceDao ,IOrderDao, IProductDao, IJobTypeDao, IJobDao, ICustomerDao,
		IMoldDao, IProductLogDao
{
}
