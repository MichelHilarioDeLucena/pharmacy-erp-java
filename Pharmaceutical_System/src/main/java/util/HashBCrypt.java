package util;

import org.mindrot.jbcrypt.BCrypt;

public class HashBCrypt {

	public static String hash(String senhaPura) {
		return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
	}

	public static boolean check(String senhaDigitada, String senhaDoBanco) {
		return BCrypt.checkpw(senhaDigitada, senhaDoBanco);
	}
}