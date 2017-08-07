package name.edward.elasticsearch.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import name.edward.elasticsearch.config.Constant;
import name.edward.elasticsearch.entity.JSONBody;

/**
 * 数据同步处理器
 * 
 * @author edward
 * @date 2017-8-02
 */
public class Handler {

	private static final ExecutorService threadExecutor = Executors.newFixedThreadPool(Constant.thread_pool_size);

	public static void handle() {
		Task task = null;
		for (JSONBody body : Constant.bodies) {
			task = new Task(body);
			threadExecutor.submit(task);
		}
		//当任务队列执行完后关闭线程池
		threadExecutor.shutdown();
	}

}
