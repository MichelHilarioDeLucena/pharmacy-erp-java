package config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import io.github.cdimascio.dotenv.Dotenv;

public class JPAUtil {

	private static EntityManagerFactory FACTORY;

	static {
		try {
			// Tenta carregar do classpath de forma explícita
			Dotenv dotenv = Dotenv.configure().load();

			String url = dotenv.get("DB_URL");
			System.out.println("DEBUG - Valor de DB_URL: " + url); // OLHE ISSO NO CONSOLE DO ECLIPSE

			if (url == null) {
				// Se ainda for null, vamos tentar ler sem o Dotenv só para testar
				System.err.println(
						"ERRO: Dotenv não conseguiu ler as chaves. Verifique se o arquivo .env está em src/main/resources");
				throw new RuntimeException("Variáveis de ambiente não encontradas no .env!");
			}

			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.url", url);
			properties.put("javax.persistence.jdbc.user", dotenv.get("DB_USER"));
			properties.put("javax.persistence.jdbc.password", dotenv.get("DB_PASSWORD"));

			FACTORY = Persistence.createEntityManagerFactory("erp", properties);
			System.out.println("HIBERNATE: Factory criada com sucesso!");

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException("Erro ao configurar JPA: " + e.getMessage());
		}
	}

	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}
}