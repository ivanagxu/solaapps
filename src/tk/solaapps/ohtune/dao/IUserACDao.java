package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Post;
import tk.solaapps.ohtune.model.UserAC;

public interface IUserACDao extends IBaseDao{
	boolean addUserAC(UserAC userac);
	List<UserAC> getAllUserAC();
	UserAC getUserACByPost(Post post);
	UserAC getUserACById(Long id);
	UserAC getUserACByLoginId(String login_id);
	boolean updateUserAC(UserAC userac);
	boolean deleteUserAC(UserAC userac);
}
