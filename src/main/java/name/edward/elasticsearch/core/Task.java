package name.edward.elasticsearch.core;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import org.elasticsearch.client.Client;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import name.edward.elasticsearch.entity.JSONBody;
import name.edward.elasticsearch.entity.JdbcTypeEnum;
import name.edward.elasticsearch.util.DBUtil;
import name.edward.elasticsearch.util.ResultSetUtil;

public class Task implements Runnable {

	private static final Logger LOGGER = LoggerFactory.getLogger(Task.class);

	private JSONBody body;

	private String name; // 任务名称

	public Task(JSONBody body) {
		this.body = body;
		this.name = new StringBuilder(body.getIndex()).append("-").append(body.getType()).append("-Task").toString();
	}

	@Override
	public void run() {
		LOGGER.info("Starting to execute Task=====>{}<=====", name);
		Connection conn = null;
		try {
			conn = DBUtil.getConnection(body.getDataSourceDriver(), body.getDataSourceUrl(),
					body.getDataSourceUsername(), body.getDataSourcePassword());
			IDataDao dao = new DataDao(conn);
			IExecutor executor = new Executor();
			Client client = TransportClientManager.getClient();

			preHandle(client, body, executor);

			String primaryKey = null;
			List<Map<String, Object>> dataList = null;
			if (body.isCustomId()) { // 是否使用自定义主键id
				primaryKey = body.getPrimaryKey();
			}
			if (body.isPaging()) {
				int count = dao.count(body.getCountSql());
				int times, start, end;
				times = (count / body.getPagesize()) + (count % body.getPagesize() == 0 ? 0 : 1);
				for (int i = 1; i <= times; i++) {
					if (JdbcTypeEnum.MYSQL.toString().equalsIgnoreCase(body.getJdbcType())) {
						start = (i - 1) * body.getPagesize();
						end = body.getPagesize();
					} else if (JdbcTypeEnum.ORACLE.toString().equalsIgnoreCase(body.getJdbcType())) {
						start = (i - 1) * body.getPagesize() + 1;
						end = start + body.getPagesize();
					} else {
						throw new IllegalArgumentException(
								"Unknown jdbcType，just supports mysql and oracle, please check the relative json file");
					}
					dataList = ResultSetUtil.convertToMaps(dao.query(body.getDataSql(), start, end));
					executor.batchUpload(client, body.getIndex(), body.getType(), primaryKey, dataList);
				}
			} else {
				dataList = ResultSetUtil.convertToMaps(dao.query(body.getDataSql()));
				executor.batchUpload(client, body.getIndex(), body.getType(), primaryKey, dataList);
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("Occurring an exception when exectute the Task=====>{}<===== due to=====>{}<=====", name, e);
		} finally {
			if(null != conn) {
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
					LOGGER.error("Occurring an exception when to close conn due to=====>{}<===== due to=====>{}<=====", name, e);
				}
			}
		}
	}

	/**
	 * 上传数据前预处理
	 * 
	 * @param client
	 * @param body
	 * @param executor
	 */
	private static void preHandle(Client client, JSONBody body, IExecutor executor) {
		String index = body.getIndex();
		String type = body.getType();
		Map<String, Object> mapping = body.getMapping();

		// 校验索引是否存在，不存在则创建
		if (!executor.isExisted(client, index)) {
			executor.createIndex(client, index);
		}

		// 判断body中mapping是否为空或空集合，是则不做处理，否则插入/更新mapping
		if (mapping != null && !mapping.isEmpty()) {
			executor.createMapping(client, index, type, mapping);
		}

		// 清楚原有数据
		executor.deleteDocuments(client, index, type);
	}

}
