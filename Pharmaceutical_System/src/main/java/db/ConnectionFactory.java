package db;

import java.sql.Connection;
import java.sql.DriverManager;

import config.EnvConfig;

public final class ConnectionFactory {

	private ConnectionFactory() {
	}

	public static Connection getConnection() throws Exception {
		Class.forName("com.mysql.cj.jdbc.Driver");

		String url = EnvConfig.getRequired("DB_URL");
		String user = EnvConfig.getRequired("DB_USER");
		String password = EnvConfig.getRequired("DB_PASSWORD");

		return DriverManager.getConnection(url, user, password);
	}
}