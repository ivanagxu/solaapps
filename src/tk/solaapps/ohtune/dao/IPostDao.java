package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.Section;

public interface IPostDao extends IBaseDao{
	boolean addPost(Post post);
	List<Post> getAllPost();
	List<Post> getPostBySection(Section section);
	Post getPostById(Long id);
	Post getPostByName(String name);
	boolean deletePost(Post post);
}
