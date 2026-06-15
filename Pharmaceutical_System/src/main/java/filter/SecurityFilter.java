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

		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;

		httpRequest.setCharacterEncoding("UTF-8");
		httpResponse.setCharacterEncoding("UTF-8");

		if (httpRequest.getSession().getAttribute("csrfToken") == null) {
			httpRequest.getSession().setAttribute("csrfToken", UUID.randomUUID().toString());
		}

		if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && !httpRequest.getRequestURI().contains("LoginServlet")) {
			String tokenDaSessao = (String) httpRequest.getSession().getAttribute("csrfToken");
			String tokenDoFormulario = httpRequest.getParameter("csrfToken");

			if (tokenDaSessao == null || !tokenDaSessao.equals(tokenDoFormulario)) {
				httpResponse.sendError(HttpServletResponse.SC_FORBIDDEN, "Acesso negado: Token CSRF inválido ou ausente.");
				return; 
			}
		}
		
		chain.doFilter(new XSSRequestWrapper(httpRequest), httpResponse);
	}

	public void destroy() {
	}

	public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
	}
}