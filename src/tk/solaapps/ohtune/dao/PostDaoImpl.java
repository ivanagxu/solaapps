package tk.solaapps.ohtune.dao;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.criterion.Restrictions;

import tk.solaapps.ohtune.model.Customer;
import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.Section;

public class PostDaoImpl extends BaseDao implements IPostDao{

	@Override
	public boolean addPost(Post post) {
		getSession().save(post);
		return true;
	}

	@Override
	public List<Post> getAllPost() {
		return getSession().createCriteria(Post.class).list();
	}

	@Override
	public List<Post> getPostBySection(Section section) {
		if(section == null)
			return new ArrayList<Post>();
		List<Post> posts = getSession().createCriteria(Post.class).add(Restrictions.eq("section", section.getId())).list();
		return posts;
	}

	@Override
	public Post getPostById(Long id) {
		List<Post> posts = getSession().createCriteria(Post.class).add(Restrictions.eq("id", id)).list();
		if(posts.size() != 0)
			return posts.get(0);
		else
			return null;
	}

	@Override
	public Post getPostByName(String name) {
		List<Post> posts = getSession().createCriteria(Post.class).add(Restrictions.eq("name", name)).list();
		if(posts.size() != 0)
			return posts.get(0);
		else
			return null;
	}

	@Override
	public boolean deletePost(Post post)
	{
		getSession().delete(post);
		return true;
	}
	
	@Override
	protected Class getModelClass() {
		return Post.class;
	}
}
