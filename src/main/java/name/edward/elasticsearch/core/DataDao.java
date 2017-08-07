package name.edward.elasticsearch.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DataDao implements IDataDao {

	private static final Logger LOGGER = LoggerFactory.getLogger(DataDao.class);

	private Connection conn;

	public DataDao(Connection conn) {
		this.conn = conn;
	}

	public int count(String sql) {
		LOGGER.info("Executing query sql=====>{}<=====", sql);
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return resultSet.getInt(1);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Occorring an exception when to execute query sql=====>{}<===== due to=====>{}<=====", sql, e);
		}
		return 0;
	}

	public ResultSet query(String sql) {
		LOGGER.info("Executing query sql=====>{}<=====", sql);
		try {
			Statement statement = conn.createStatement();
			ResultSet resultSet = statement.executeQuery(sql);
			return resultSet;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Occorring an exception when to execute query sql=====>{}<===== due to=====>{}<=====", sql, e);
		}
		return null;
	}

	public ResultSet query(String sql, int start, int end) {
		LOGGER.info("Executing query sql=====>{}<=====", sql);
		try {
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setInt(1, start);
			statement.setInt(2, end);
			ResultSet resultSet = statement.executeQuery();
			return resultSet;
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Occorring an exception when to execute query sql=====>{}<===== due to=====>{}<=====", sql, e);
		}
		return null;
	}

}
