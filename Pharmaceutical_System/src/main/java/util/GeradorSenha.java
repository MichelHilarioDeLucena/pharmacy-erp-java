package util;

public class GeradorSenha {
	public static void main(String[] args) {
		// Usa a sua própria classe para gerar o hash de "123456"
		String hashCorreto = HashBCrypt.criptografarSenha("123456");

		System.out.println("=============================================");
		System.out.println("COPIE O HASH ABAIXO E COLOQUE NO BANCO:");
		System.out.println(hashCorreto);
		System.out.println("=============================================");
	}
}