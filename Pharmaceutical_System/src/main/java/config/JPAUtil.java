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
			System.out.println("=== [JPAUtil] Iniciando configuração do Hibernate ===");

			// Tenta carregar o .env da raiz do projeto (./)
			Dotenv dotenv = Dotenv.configure().directory("./").ignoreIfMalformed().ignoreIfMissing().load();

			// Se não achou na raiz, o dotenv.get retornará null.
			// Vamos validar e avisar no console.
			String url = dotenv.get("DB_URL");
			String user = dotenv.get("DB_USER");
			String pass = dotenv.get("DB_PASSWORD");

			if (url == null) {
				System.err.println("=== [JPAUtil] AVISO: .env não encontrado na raiz. Verifique o diretório! ===");
			} else {
				System.out.println("=== [JPAUtil] .env carregado com sucesso da raiz ===");
			}

			Map<String, String> properties = new HashMap<>();
			properties.put("javax.persistence.jdbc.url", url);
			properties.put("javax.persistence.jdbc.user", user);
			properties.put("javax.persistence.jdbc.password", pass);

			// Tenta criar a fábrica de conexões
			FACTORY = Persistence.createEntityManagerFactory("erp", properties);
			System.out.println("=== [JPAUtil] Hibernate conectado com sucesso! ===");

		} catch (Exception e) {
			System.err.println("=== [JPAUtil] ERRO FATAL ao iniciar Hibernate ===");
			e.printStackTrace();
			throw new RuntimeException("Erro ao configurar JPA: " + e.getMessage());
		}
	}

	public static EntityManager getEntityManager() {
		if (FACTORY == null) {
			throw new RuntimeException("A Factory do JPA não foi inicializada corretamente.");
		}
		return FACTORY.createEntityManager();
	}
}