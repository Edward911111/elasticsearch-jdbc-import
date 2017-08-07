package name.edward.elasticsearch.core;

import java.util.List;
import java.util.Map;
import org.elasticsearch.client.Client;

public interface IExecutor {
	
	/**
	 * 创建索引
	 * @param index
	 * @return
	 */
	public boolean createIndex(Client client, String index);
	
	/**
	 * 校验一个或多索引是否存在(有一个不存在，返回false)
	 * @param indexes
	 * @return
	 */
	public boolean isExisted(Client client, String... indexes);
	
	/**
	 * 删除一个或者索引
	 * @param index
	 * @return
	 */
	public boolean deleteIndex(Client client, String... index);
	
	/**
	 * 删除所有文档
	 * @return
	 */
	public boolean deleteDocuments(Client client);
	
	/**
	 * 删除某一或多个指定索引下所有文档
	 * @param index
	 * @return
	 */
	public boolean deleteDocuments(Client client, String... indexes);
	
	/**
	 * 删除指定索引、类型下所有文档
	 * @param index
	 * @param type
	 * @return
	 */
	public boolean deleteDocuments(Client client, String index, String type);
	
	/**
	 * 创建mapping
	 * @param idnex 索引
	 * @param type 文档类型
	 * @param mapping 属性类型映射
	 * @return
	 */
	public boolean createMapping(Client client, String index, String type, Map<String, Object> mappingMap);
	
	/**
	 * 获取指定索引和类型的mapping
	 * @param client
	 * @param index
	 * @param type
	 * @return
	 */
	public String getMapping(Client client, String index, String type);
	
	/**
	 * 批量上传数据
	 * @param client
	 * @param index 索引
	 * @param type 文档类型
	 * @param primaryKey 主键名称
	 * @param dataList 数据集合
	 */
	public void batchUpload(Client client, String index, String type, String primaryKey, List<Map<String, Object>> dataList);
	
}
