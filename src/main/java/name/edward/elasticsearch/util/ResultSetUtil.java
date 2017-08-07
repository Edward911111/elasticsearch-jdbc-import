package name.edward.elasticsearch.util;

import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.alibaba.fastjson.JSON;

/**
 * 处理查询结果集工具类
 * 
 * @author edward
 * @date 2017-08-01
 */
public class ResultSetUtil {

	/**
	 * 获取resultSet的结果集元素并转化为List集合
	 * 
	 * @param resultSet
	 * @return
	 * @throws SQLException 
	 */
	public static List<Map<String, Object>> convertToMaps(ResultSet resultSet) throws SQLException {
		try {
			ResultSetMetaData metaData = resultSet.getMetaData();
			List<Map<String, Object>> maps = new ArrayList<Map<String, Object>>();
			Map<String, Object> tempMap = new HashMap<String, Object>();
			int i;
			while (resultSet.next()) {
				for (i = 1; i <= metaData.getColumnCount(); i++) {
					tempMap.put(metaData.getColumnLabel(i), resultSet.getObject(i));
				}
				maps.add(JSON.parseObject(JSON.toJSONString(tempMap)));
				tempMap.clear();
			}
			return maps;
		} catch (SQLException e) {
			e.printStackTrace();
			throw e;
		}
	}

}
