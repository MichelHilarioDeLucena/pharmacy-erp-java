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

		String acao = req.getMethod() + " " + req.getRequestURI();

		if (acao.contains(".css") || acao.contains(".js") || acao.contains(".png")) {
			chain.doFilter(request, response);
			return;
		}

		String ip = req.getHeader("X-Forwarded-For"); 
		if (ip == null || ip.isEmpty()) {
			ip = request.getRemoteAddr();
		}

		Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

		String resultado = "";
		String detalhes = null;

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
			resultado = "ERRO SISTEMA";
			detalhes = e.getMessage(); 
			throw e; 
		} finally {
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
			if (em != null && em.getTransaction().isActive()) {
				em.getTransaction().rollback();
			}
			e.printStackTrace(); 
		} finally {
			if (em != null) {
				em.close();
			}
		}
	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	}

	@Override
	public void destroy() {
	}
}