package name.edward.elasticsearch;

import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import name.edward.elasticsearch.core.Handler;
import name.edward.elasticsearch.init.InitializationContext;

public class Application {

	private static final Logger LOGGER = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) throws Exception {
		Date start = new Date();
		LOGGER.info("Start at：{}", start);
		
		// 初始化上下文
		InitializationContext.init();
		
		// 数据同步
		Handler.handle();
		
		Date end = new Date();
		long spendTime = (end.getTime() - start.getTime())/1000;
		LOGGER.info("Finish at：{}", end);
		LOGGER.info("It has spent {} seconds for updating data", spendTime);
		
	}

}
