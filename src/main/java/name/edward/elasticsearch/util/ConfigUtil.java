package name.edward.elasticsearch.util;

import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ConfigUtil {

	private static final Logger LOGGER = LoggerFactory.getLogger(ConfigUtil.class);

	/**
	 * 初始化配置文件
	 * @param configFileName
	 * @param mappingClass
	 * @throws Exception 
	 */
	public static void initConfig(String configFileName, Class<?> mappingClass) throws Exception {
		Map<String, String> map = convertToMap(configFileName);
		mapping(map, mappingClass);
		LOGGER.info("Loading config file successfully");
	}

	private static Map<String, String> convertToMap(String configFileName) throws Exception {
		Map<String, String> map = new HashMap<String, String>();
		try {
			Properties prop = new Properties();
			prop.load(new InputStreamReader(ConfigUtil.class.getClassLoader().getResourceAsStream(configFileName),
					"utf-8"));
			for (Object k : prop.keySet()) {
				Object v = prop.get(k);
				String key = String.valueOf(k), value = String.valueOf(v);
				map.put(key, value);
			}

		} catch (Exception e) {
			LOGGER.error("Occurring an exception when to execute the method 'convertToMap' for {}", e);
			throw e;
		}
		return map;
	}

	private static void mapping(Map<String, String> map, Class<?> mappingClass) {
		try {
			Field[] fields = mappingClass.getFields();
			for (Field f : fields) {
				String val = map.get(f.getName());
				if (val == null || val.length() == 0) {
					continue;
				}
				if (f.getType() == Integer.class || f.getType() == int.class) {
					f.set(mappingClass, Integer.valueOf(val));
				} else if (f.getType() == Byte.class || f.getType() == byte.class) {
					f.set(mappingClass, Byte.valueOf(val));
				} else if (f.getType() == Long.class || f.getType() == long.class) {
					f.set(mappingClass, Long.valueOf(val));
				} else if (f.getType() == Short.class || f.getType() == short.class) {
					f.set(mappingClass, Short.valueOf(val));
				} else if (f.getType() == String.class) {
					f.set(mappingClass, String.valueOf(val));
				} else if (f.getType() == Double.class || f.getType() == double.class) {
					f.set(mappingClass, Double.valueOf(val));
				} else if (f.getType() == Float.class || f.getType() == float.class) {
					f.set(mappingClass, Float.valueOf(val));
				} else if (f.getType() == Boolean.class || f.getType() == boolean.class) {
					f.set(mappingClass, Boolean.valueOf(val));
				} else if (f.getType() == List.class || f.getType() == ArrayList.class
						|| f.getType() == LinkedList.class) {
					List<String> list = new ArrayList<String>();
					Collections.addAll(list, val.split(";"));
					f.set(mappingClass, list);
				} else {
					throw new Exception("unsupported data type");
				}
			}

		} catch (Exception e) {
			LOGGER.error("Occurring an exception when to execute the method 'mapping' for {}", e);
		}

	}

}
