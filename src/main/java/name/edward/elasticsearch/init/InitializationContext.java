package name.edward.elasticsearch.init;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.alibaba.fastjson.JSON;
import name.edward.elasticsearch.config.Constant;
import name.edward.elasticsearch.core.TransportClientManager;
import name.edward.elasticsearch.entity.JSONBody;
import name.edward.elasticsearch.util.ConfigUtil;
import name.edward.elasticsearch.util.FileUtil;

/**
 * 用于系统初始化的上下文
 * 
 * @author edward
 * @date 2017-07-21
 */
public class InitializationContext {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(InitializationContext.class);
	

	public static void init() throws Exception {
		LOGGER.info("Starting to initialize InitializationContext");
		
		ConfigUtil.initConfig("conf/constant.properties", Constant.class);

		TransportClientManager.init();
		
		Constant.bodies = getJsonBodies();
		
		LOGGER.info("Initialize InitializationContext successfully");
		
	}
	
	/**
	 * 读取json文件实例化JSONbody
	 * @return
	 * @throws Exception 
	 */
	public static List<JSONBody> getJsonBodies() throws Exception {
		List<JSONBody> bodies = new ArrayList<JSONBody>();
		try {
			FilenameFilter filter = new FilenameFilter() {

				@Override
				public boolean accept(File dir, String name) {

					return name.endsWith(".json");
				}
			};
			File[] files = FileUtil.getFiles("json", filter);
			for (File file : files) {
				bodies.add(JSON.parseObject(IOUtils.toString(file.toURI()), JSONBody.class));
			}
		} catch (Exception e) {
			LOGGER.error("Occurring an exception when to execute method 'getJsonBodies' due to:=====>{}<=====", e);
			throw e;
		}
		return bodies;
	}

}
