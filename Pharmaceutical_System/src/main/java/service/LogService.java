package service;

import javax.persistence.EntityManager;

import dao.LogDAO;
import model.LogAcesso;
import model.Usuario;

public class LogService {

	public static void registrar(Usuario u, String acao, String ip, String resultado, String detalhes,
			EntityManager em) {
		LogDAO dao = new LogDAO(em);
		LogAcesso log = new LogAcesso(u, acao, ip, resultado, detalhes);
		dao.salvar(log);
	}

	public static boolean ipBloqueado(String ip, EntityManager em) {
		LogDAO dao = new LogDAO(em);
		Long falhas = dao.contarFalhasRecentes(ip);

		return falhas >= 5;
	}
}