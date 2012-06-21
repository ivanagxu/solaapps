package tk.solaapps.ohtune.dao;

import java.util.List;

import tk.solaapps.ohtune.model.Mold;

public interface IMoldDao extends IBaseDao{
	boolean addMold(Mold mold);
	boolean deleteMold(Mold mold);
	List<Mold> getAllMold();
	Mold getMoldByCode(String code);
}
