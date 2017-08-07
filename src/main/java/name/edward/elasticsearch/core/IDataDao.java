package name.edward.elasticsearch.core;

import java.sql.ResultSet;

public interface IDataDao {
	
	/**
	 * 查询总数以便分页
	 * @param sql
	 * @return
	 */
	public int count(String sql);
	
	/**
	 * 一次性查询结果集
	 * @param sql
	 * @return
	 */
	public ResultSet query(String sql);
	
	/**
	 * 分页查询
	 * @param sql
	 * @param start Oracle中表示起始行，MySQL中表示页码
	 * @param end Oracle中表示终止行，MySQL中表示页规格
	 * @return
	 */
	public ResultSet query(String sql, int start, int end);

}
