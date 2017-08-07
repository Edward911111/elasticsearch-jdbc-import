package name.edward.elasticsearch.core;

import java.net.InetAddress;
import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.settings.Settings;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.elasticsearch.transport.client.PreBuiltTransportClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import name.edward.elasticsearch.config.Constant;

public class TransportClientManager {

	private static final Logger LOGGER = LoggerFactory.getLogger(TransportClientManager.class);

	private static TransportClient client = null;

	private static boolean initialized = false;

	/**
	 * 初始化TransportClient实例
	 */
	public static void init() throws Exception {
		LOGGER.info("TransportClientManager is starting to initialize");
		try {
			// 加载配置
			Map<String, Object> setting = new HashMap<String, Object>();
			setting.put("cluster.name", Constant.es_cluster_name);
			setting.put("client.transport.sniff", Constant.es_client_transport_sniff);
			Settings settings = Settings.builder().put(setting).build();

			// 创建client，并遍历设置ES节点地址(该处只设置主节点机器，如果此处设置的所有主节点都发生故障，则ES无法提供服务)
			client = new PreBuiltTransportClient(settings);
			for (String ipAndPort : Constant.es_cluster_server) {
				client.addTransportAddress(new InetSocketTransportAddress(
						InetAddress.getByName(ipAndPort.split(":")[0]), Integer.valueOf(ipAndPort.split(":")[1])));
			}

			initialized = true;
			LOGGER.info("Initialize client successfully");
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.error("There is an exception when to Initialize transportClient instance due to {}", e.getMessage());
			throw e;
		}
	}

	public static TransportClient getClient() throws Exception {
		if (!initialized) {
			init();
		}
		return client;
	}
}
