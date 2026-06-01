package test;

import java.sql.Connection;
import java.sql.DriverManager;

import io.github.cdimascio.dotenv.Dotenv;

public class TestConnection {
	public static void main(String[] args) {
		try {
			Dotenv dotenv = Dotenv.load();

			String url = dotenv.get("DB_URL");
			String user = dotenv.get("DB_USER");
			String password = dotenv.get("DB_PASSWORD");

			System.out.println("Tentando conectar...");

			Connection conn = DriverManager.getConnection(url, user, password);

			System.out.println("✅ Conectado com sucesso!");
			conn.close();

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}