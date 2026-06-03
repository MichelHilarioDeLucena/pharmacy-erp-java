package filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;

import util.XSSRequestWrapper;

// O "/*" indica que ele protege TODAS as páginas e servlets do sistema
@WebFilter("/*")
public class SecurityFilter implements Filter {

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {

		// 1. Resolve 50% dos problemas de acentuação (UTF-8)
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");

		// 2. Aplica a blindagem contra XSS em todos os inputs
		// Agora, qualquer request.getParameter() já virá "limpo"
		chain.doFilter(new XSSRequestWrapper((HttpServletRequest) request), response);
	}

	public void destroy() {
	}

	public void init(javax.servlet.FilterConfig filterConfig) throws ServletException {
	}
}