package name.edward.elasticsearch.config;

import java.util.List;

import name.edward.elasticsearch.entity.JSONBody;

/**
 * 配置文件的静态变量配置类
 * 
 * @author edward
 * @date 2017-07-21
 */
public class Constant {

	// ES集群服务器地址集合
	public static List<String> es_cluster_server;

	// ES集群名称
	public static String es_cluster_name;

	// 集群嗅探模式(false:关闭 true:开启)
	public static String es_client_transport_sniff;
	
	//JSONBody实例集合
	public static List<JSONBody> bodies;
	
	//线程池大小
	public static int thread_pool_size;

}
