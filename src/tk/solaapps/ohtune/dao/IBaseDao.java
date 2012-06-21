package tk.solaapps.ohtune.dao;

import java.util.Collection;
import java.util.List;

public interface IBaseDao{
	List search(String[] columns, Object[] values, String[] inClause, Collection[] in, int start,
			int limit, String orderby, boolean sortAsc);
}
