package test;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.ejb.Startup;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import model.Usuario;

@Singleton
@Startup // Faz esse código rodar assim que o servidor ligar
public class TesteConexao {

	@PersistenceContext(unitName = "FarmaciaPU")
	private EntityManager em;

	@PostConstruct
	public void testar() {
		try {
			System.out.println(">>> TENTANDO CONECTAR AO MYSQL...");

			Usuario teste = new Usuario();
			teste.setLogin("admin");
			teste.setSenhaHash("123"); // Depois usaremos o BCrypt aqui
			teste.setPerfil("ADMIN");

			em.persist(teste);

			System.out.println(">>> CONEXÃO SUCESSO: USUARIO SALVO NO BANCO!");
		} catch (Exception e) {
			System.err.println(">>> ERRO NA CONEXÃO: " + e.getMessage());
		}
	}
}