package tk.solaapps.ohtune.dao;

import java.util.Collection;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Job;

public abstract class BaseDao {
	protected SessionFactory sessionFactory;

	protected Session getSession()
	{
		return this.sessionFactory.getCurrentSession();
	}
    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }
    
    protected abstract Class getModelClass();
    
    public List search(String[] columns, Object[] values, String[] inClause, Collection[] in, int start,
			int limit, String orderby, boolean sortAsc) {
		List data;
		
		Criteria c = getSession().createCriteria(getModelClass());
		
		if(columns != null && values != null)
		{
			for(int i = 0; i < columns.length; i++)
			{
				c.add(Restrictions.eq(columns[i], values[i]));
			}
		}
		
		if(in != null && inClause != null)
		{
			for(int i = 0; i < inClause.length; i++)
			{
				c.add(Restrictions.in(inClause[i], in[i]));
			}
		}
		
		c.setMaxResults(limit);
		if(orderby != null && !orderby.equals(""))
		{
			c.setFirstResult(start);
			
			if(sortAsc)
				c.addOrder(Property.forName(orderby).asc());
			else
				c.addOrder(Property.forName(orderby).desc());
		}
		
		data = c.list();
		
		return data;
	}
}
