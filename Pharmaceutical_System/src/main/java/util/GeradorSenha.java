package util;

public class GeradorSenha {
	public static void main(String[] args) {
		String hashCorreto = HashBCrypt.hash("123456");

		System.out.println("=============================================");
		System.out.println("COPIE O HASH ABAIXO E COLOQUE NO BANCO:");
		System.out.println(hashCorreto);
		System.out.println("=============================================");
	}
}