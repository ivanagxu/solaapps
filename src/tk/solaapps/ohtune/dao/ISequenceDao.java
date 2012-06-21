package tk.solaapps.ohtune.dao;

import tk.solaapps.ohtune.model.Sequence;

public interface ISequenceDao extends IBaseDao{
	Sequence getNextValueByName(String name);
}
