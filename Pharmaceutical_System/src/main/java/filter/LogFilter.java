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

// A anotação abaixo faz o filtro interceptar o sistema inteiro. 
// Você pode mudar para "/area-restrita/*" se quiser logar apenas páginas específicas.
@WebFilter("/*")
public class LogFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest req = (HttpServletRequest) request;
		HttpServletResponse res = (HttpServletResponse) response;

		// 1. Monta a "Ação" (Ex: "GET /Pharmaceutical_System/dashboard.jsp")
		String acao = req.getMethod() + " " + req.getRequestURI();

		// Evita logar carregamento de CSS, JS e Imagens (polui o banco)
		if (acao.contains(".css") || acao.contains(".js") || acao.contains(".png")) {
			chain.doFilter(request, response);
			return;
		}

		// 2. Captura o IP
		String ip = req.getHeader("X-Forwarded-For"); // Caso use proxy/load balancer
		if (ip == null || ip.isEmpty()) {
			ip = request.getRemoteAddr();
		}

		// 3. Captura o usuário logado (Ajuste "usuarioLogado" para o nome que você usa
		// no seu login)
		Usuario usuario = (Usuario) req.getSession().getAttribute("usuarioLogado");

		String resultado = "";
		String detalhes = null;

		try {
			// Continua a execução normal do Servlet/JSP
			chain.doFilter(request, response);

			// Pós-execução: Pega o status HTTP para saber se deu certo
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
			detalhes = e.getMessage(); // Salva a mensagem do erro para depuração
			throw e; // Repassa o erro para não quebrar a aplicação
		} finally {
			// 4. Salva o Log no Banco de Dados
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
			e.printStackTrace(); // Apenas printa no console para o log não quebrar o sistema
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