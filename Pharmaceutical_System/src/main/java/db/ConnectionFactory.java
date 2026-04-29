package db;

import java.sql.Connection;
import java.sql.DriverManager;

import config.EnvConfig;

public class ConnectionFactory {

	public static Connection getConnection() throws Exception {

		Class.forName("com.mysql.cj.jdbc.Driver");

		return DriverManager.getConnection(EnvConfig.get("DB_URL"), EnvConfig.get("DB_USER"),
				EnvConfig.get("DB_PASSWORD"));
	}
}