package config;

import io.github.cdimascio.dotenv.Dotenv;

public final class EnvConfig {

	private static final Dotenv dotenv = Dotenv.configure().directory("./") // Local padrão onde o Docker também lê
			.ignoreIfMalformed().ignoreIfMissing().load();

	private EnvConfig() {
	}

	public static String get(String key) {
		String value = System.getenv(key);
		if (value != null && !value.isBlank()) {
			return value;
		}

		value = dotenv.get(key);
		if (value != null && !value.isBlank()) {
			return value;
		}

		return null;
	}

	public static String getRequired(String key) {
		String value = get(key);
		if (value == null || value.isBlank()) {
			throw new IllegalStateException("Missing environment variable: " + key);
		}
		return value;
	}
}