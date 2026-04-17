package br.com.uerj.erp;

import org.mindrot.jbcrypt.BCrypt;

public class HashBCrypt {

	// Transforma "senha123" em algo como "$2a$10$vI8Z..."
	public String criptografarSenha(String senhaPura) {
		return BCrypt.hashpw(senhaPura, BCrypt.gensalt());
	}

	// Verifica se a senha que o usuário digitou bate com a do banco
	public boolean verificarSenha(String senhaDigitada, String senhaDoBanco) {
		return BCrypt.checkpw(senhaDigitada, senhaDoBanco);
	}
}