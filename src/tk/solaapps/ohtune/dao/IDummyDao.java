package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Dummy;

public interface IDummyDao extends IBaseDao{
	List<Dummy> getAllDummy();
}
