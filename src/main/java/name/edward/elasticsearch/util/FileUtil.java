package name.edward.elasticsearch.util;

import java.io.File;
import java.io.FilenameFilter;
import java.io.IOException;
import java.net.URL;
import java.util.Objects;

/**
 * 文件工具类
 * 
 * @author edward
 * @date 2017-08-02
 */
public class FileUtil {

	/**
	 * 获取指定资源目录下的文件集合
	 * 
	 * @param resourceDirectory 资源目录名称
	 * @param filter 文件过滤器
	 * @throws IOException 
	 */
	public static File[] getFiles(String resourceDirectory, FilenameFilter filter) {
		URL url = FileUtil.class.getClassLoader().getResource(resourceDirectory);
		Objects.requireNonNull(url, "can't find the directory:=====>" + resourceDirectory + "<=====");
		File parentFile = new File(url.getFile());
		File[] files = null;
		if(null == filter) {
			files = parentFile.listFiles();
		} else {
			files = parentFile.listFiles(filter);
		}
		return files;
	}

}
