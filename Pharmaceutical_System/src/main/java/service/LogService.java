package service;

import javax.persistence.EntityManager;

import dao.LogDAO;
import model.LogAcesso;
import model.Usuario;

public class LogService {

	// Registra qualquer ação (sucesso ou erro)
	public static void registrar(Usuario u, String acao, String ip, String resultado, String detalhes,
			EntityManager em) {
		LogDAO dao = new LogDAO(em);
		LogAcesso log = new LogAcesso(u, acao, ip, resultado, detalhes);
		dao.salvar(log);
	}

	// Verifica se o IP deve ser barrado na entrada
	public static boolean ipBloqueado(String ip, EntityManager em) {
		LogDAO dao = new LogDAO(em);
		Long falhas = dao.contarFalhasRecentes(ip);

		// Retorna true se houver 5 ou mais falhas nos últimos 10 min
		return falhas >= 5;
	}
}