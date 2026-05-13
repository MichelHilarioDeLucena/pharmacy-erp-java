package config;

import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public final class JPAUtil {

	private static final String PERSISTENCE_UNIT = "erp";
	private static final EntityManagerFactory FACTORY = buildFactory();

	private JPAUtil() {
	}

	private static EntityManagerFactory buildFactory() {
		String url = EnvConfig.getRequired("DB_URL");
		String user = EnvConfig.getRequired("DB_USER");
		String password = EnvConfig.getRequired("DB_PASSWORD");

		Map<String, String> properties = new HashMap<>();
		properties.put("javax.persistence.jdbc.url", url);
		properties.put("javax.persistence.jdbc.user", user);
		properties.put("javax.persistence.jdbc.password", password);

		return Persistence.createEntityManagerFactory(PERSISTENCE_UNIT, properties);
	}

	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}

	public static void close() {
		if (FACTORY.isOpen()) {
			FACTORY.close();
		}
	}
}