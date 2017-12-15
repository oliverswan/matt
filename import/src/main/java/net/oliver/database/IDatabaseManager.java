package net.oliver.database;

import java.util.List;
import java.util.Map;

public interface IDatabaseManager {

	public List<Map> select(String sql)throws Exception;
	public int update(String sql)throws Exception;
	void close() throws Exception;
}
