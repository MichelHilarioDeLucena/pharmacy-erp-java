package dao;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;

import model.LogAcesso;

public class LogDAO {

	private EntityManager em;

	public LogDAO(EntityManager em) {
		this.em = em;
	}

	public void salvar(LogAcesso log) {
		em.persist(log);
	}

	public Long contarFalhasRecentes(String ip) {
		// A transação já está aberta no Servlet quando este método é chamado.
		// Não abrimos uma nova aqui — participamos da transação existente.
		return em
				.createQuery("SELECT COUNT(l) FROM LogAcesso l " + "WHERE l.ip = :ip "
						+ "AND l.resultado LIKE 'FALHA%' " + "AND l.dataHora >= :limite", Long.class)
				.setParameter("ip", ip).setParameter("limite", LocalDateTime.now().minusMinutes(10)).getSingleResult();
	}
}