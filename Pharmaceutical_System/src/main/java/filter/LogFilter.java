package filter;

import java.io.IOException;

import javax.persistence.EntityManager;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import config.JPAUtil;
import model.LogAcesso;
import model.Usuario;

@WebFilter("/*")
public class LogFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// Ignora arquivos estáticos
		String uri = req.getRequestURI();
		if (uri.contains(".css") || uri.contains(".js") || uri.contains(".png") || uri.contains(".jpg")) {
			chain.doFilter(request, response);
			return;
		}

		String acao = req.getMethod() + " " + uri;
		String ip = req.getHeader("X-Forwarded-For");
		if (ip == null || ip.isEmpty()) {
			ip = request.getRemoteAddr();
		}

		Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");
		String resultado = "";
		String detalhes = null;
		boolean erro = false;

		try {
			chain.doFilter(request, response);
			int status = res.getStatus();
			if (status >= 200 && status < 300) {
				resultado = "SUCESSO";
			} else if (status == 403 || status == 401) {
				resultado = "ACESSO NEGADO";
			} else {
				resultado = "STATUS " + status;
			}
		} catch (Exception e) {
			erro = true;
			resultado = "ERRO SISTEMA";
			detalhes = e.getMessage() != null ? e.getMessage().substring(0, Math.min(e.getMessage().length(), 255)) : null;
			throw e; // repassa a exceção
		} finally {
			// Salva o log em uma transação separada, SEMPRE, mesmo em caso de erro
			salvarLogNoBanco(usuario, acao, ip, resultado, detalhes);
		}
	}

	private void salvarLogNoBanco(Usuario usuario, String acao, String ip, String resultado, String detalhes) {
		EntityManager em = null;
		try {
			em = JPAUtil.getEntityManager();
			em.getTransaction().begin();
			LogAcesso log = new LogAcesso(usuario, acao, ip, resultado, detalhes);
			em.persist(log);
			em.getTransaction().commit();
		} catch (Exception e) {
			// Não podemos lançar exceção aqui, apenas logamos
			System.err.println("Falha ao salvar log de acesso: " + e.getMessage());
			if (em != null && em.getTransaction().isActive()) {
				try { em.getTransaction().rollback(); } catch (Exception ex) { }
			}
		} finally {
			if (em != null && em.isOpen()) {
				em.close();
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {}

	@Override
	public void destroy() {}
}