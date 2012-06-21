package tk.solaapps.ohtune.dao;

import java.util.List;

import org.hibernate.SessionFactory;
import org.hibernate.criterion.Restrictions;
import org.springframework.transaction.annotation.Transactional;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.UserAC;

public class UserACDaoImpl extends BaseDao implements IUserACDao{

	@Override
	public boolean addUserAC(UserAC userac) {
		getSession().save(userac);
		return true;
	}

	@Override
	public List<UserAC> getAllUserAC() {
		return getSession().createCriteria(UserAC.class).list();
	}

	@Override
	public UserAC getUserACByPost(Post post) {
		if(post == null)
			return null;
		
		List<UserAC> users = getSession().createCriteria(UserAC.class).add(Restrictions.eq("post", post.getId())).list();
		
		if(users.size() != 0)
			return users.get(0);
		else
			return null;
	}

	@Override
	public UserAC getUserACById(Long id) {
		List<UserAC> users = getSession().createCriteria(UserAC.class).add(Restrictions.eq("id", id)).list();
		
		if(users.size() != 0)
			return users.get(0);
		else
			return null;
	}

	@Override
	public UserAC getUserACByLoginId(String login_id) {
		List<UserAC> users = getSession().createCriteria(UserAC.class).add(Restrictions.eq("login_id", login_id)).list();
		
		if(users.size() != 0)
			return users.get(0);
		else
			return null;
	}
	
	@Override
	public boolean updateUserAC(UserAC userac)
	{
		getSession().saveOrUpdate(userac);
		return true;
	}
	
	@Override
	public boolean deleteUserAC(UserAC userac)
	{
		getSession().delete(userac);
		return true;
	}
	
	@Override
	protected Class getModelClass() {
		return UserAC.class;
	}
}
