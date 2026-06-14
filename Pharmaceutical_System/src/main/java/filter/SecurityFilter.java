package filter;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import util.XSSRequestWrapper;

@WebFilter("/*")
public class SecurityFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// Converte os requests/responses para o padrão HTTP (necessário para usar sessão)
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		// 1. Resolve problemas de acentuação (UTF-8)
		httpRequest.setCharacterEncoding("UTF-8");
		httpResponse.setCharacterEncoding("UTF-8");

		// --- INÍCIO DA PROTEÇÃO CSRF ---
		
		// A. Se não houver um Token CSRF na sessão do usuário, cria um novo aleatório
		if (httpRequest.getSession().getAttribute("csrfToken") == null) {
			httpRequest.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());
		}

		// B. Se o usuário estiver enviando dados (usando POST, que é o padrão de formulários)
		if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && !httpRequest.getRequestURI().contains("LoginServlet")) {
			String tokenDaSessao = (String) httpRequest.getSession().getAttribute("csrfToken");
			String tokenDoFormulario = httpRequest.getParameter("csrfToken");

			// Se o token não vier no formulário ou for diferente do que está na sessão, bloqueia!
			if (tokenDaSessao == null || !tokenDaSessao.equals(tokenDoFormulario)) {
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado: Token CSRF inválido ou ausente.");
				return; // Para a execução aqui e não deixa a requisição chegar no Servlet
			}
		}
		
		// --- FIM DA PROTEÇÃO CSRF ---

		// 2. Aplica a blindagem contra XSS em todos os inputs e segue em frente
		chain.doFilter(new XSSRequestWrapper(httpRequest), httpResponse);
	}

	public void destroy() {
	}

	public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
	}
}