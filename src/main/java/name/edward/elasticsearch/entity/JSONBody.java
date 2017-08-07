package name.edward.elasticsearch.entity;

import java.io.Serializable;
import java.util.Map;
import com.alibaba.fastjson.JSON;

/**
 * 将json数据映射到该Body
 * 
 * @author edward
 * @date 2017-08-01
 */
public class JSONBody implements Serializable {

	private static final long serialVersionUID = -3199700836332265064L;

	private String jdbcType; // 数据库类型

	private String dataSourceDriver; // 数据库驱动类名称

	private String dataSourceUrl; // 数据库连接地址

	private String dataSourceUsername; // 数据库用户名

	private String dataSourcePassword; // 数据库连接密码

	private boolean paging; // 是否分页，false则只需执行dataSql而无需执行countSql

	private int pagesize; // 分页规格

	private String dataSql; // 获取数据的sql

	private String countSql; // 获取总记录数的sql

	private String index; // 数据存储到ES所在索引

	private String type; // 数据存储到ES所在类型

	private boolean customId; // false表示ES自定义生成文档_id,true表示数据库中主键值作为文档_id值

	private String primaryKey; // 数据库中主键属性名(当属性customId为true时，用该属性值从ResultSet中获取主键值)

	private Map<String, Object> mapping;

	public String getJdbcType() {
		return jdbcType;
	}

	public void setJdbcType(String jdbcType) {
		this.jdbcType = jdbcType;
	}

	public String getDataSourceDriver() {
		return dataSourceDriver;
	}

	public void setDataSourceDriver(String dataSourceDriver) {
		this.dataSourceDriver = dataSourceDriver;
	}

	public String getDataSourceUrl() {
		return dataSourceUrl;
	}

	public void setDataSourceUrl(String dataSourceUrl) {
		this.dataSourceUrl = dataSourceUrl;
	}

	public String getDataSourceUsername() {
		return dataSourceUsername;
	}

	public void setDataSourceUsername(String dataSourceUsername) {
		this.dataSourceUsername = dataSourceUsername;
	}

	public String getDataSourcePassword() {
		return dataSourcePassword;
	}

	public void setDataSourcePassword(String dataSourcePassword) {
		this.dataSourcePassword = dataSourcePassword;
	}

	public boolean isPaging() {
		return paging;
	}

	public void setPaging(boolean paging) {
		this.paging = paging;
	}

	public int getPagesize() {
		return pagesize;
	}

	public void setPagesize(int pagesize) {
		this.pagesize = pagesize;
	}

	public String getDataSql() {
		return dataSql;
	}

	public void setDataSql(String dataSql) {
		this.dataSql = dataSql;
	}

	public String getCountSql() {
		return countSql;
	}

	public void setCountSql(String countSql) {
		this.countSql = countSql;
	}

	public String getIndex() {
		return index;
	}

	public void setIndex(String index) {
		this.index = index;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public boolean isCustomId() {
		return customId;
	}

	public void setCustomId(boolean customId) {
		this.customId = customId;
	}

	public String getPrimaryKey() {
		return primaryKey;
	}

	public void setPrimaryKey(String primaryKey) {
		this.primaryKey = primaryKey;
	}

	public Map<String, Object> getMapping() {
		return mapping;
	}

	public void setMapping(Map<String, Object> mapping) {
		this.mapping = mapping;
	}

	@Override
	public String toString() {
		return JSON.toJSONString(this);
	}

}
