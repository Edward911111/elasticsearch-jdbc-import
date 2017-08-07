package name.edward.elasticsearch.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.Objects;

public class DBUtil {

	public static Connection getConnection(String driver, String url, String username, String password) throws Exception {
		Connection connection = null;
		Objects.requireNonNull(driver, "Data source driver is not allowed null");
		Objects.requireNonNull(url, "Data source url is not allowed null");
		Objects.requireNonNull(username, "Data source username is not allowed null");
		Objects.requireNonNull(password, "Data source password is not allowed null");
		Class.forName(driver);
		connection = DriverManager.getConnection(url, username, password);
		return connection;
	}

}
