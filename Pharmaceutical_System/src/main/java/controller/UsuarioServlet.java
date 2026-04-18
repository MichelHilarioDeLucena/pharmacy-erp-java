package controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.ConnectionFactory;

@WebServlet("/UsuarioServlet")
public class UsuarioServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// Dizemos ao navegador que vamos enviar um HTML
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		// Começamos a "desenhar" a página HTML antiga que você tinha
		out.println("<!DOCTYPE html>");
		out.println("<html>");
		out.println("<head><title>Lista de Usuários</title></head>");
		out.println("<body>");
		out.println("<h2>Usuários Cadastrados no Banco (Tomcat)</h2>");
		out.println("<table border='1'>");
		out.println("<tr><th>ID</th><th>Login</th><th>Perfil</th></tr>");

		try {
			// Puxa a conexão da ConnectionFactory (agora com o driver do MySQL arrumado)
			Connection conn = ConnectionFactory.getConnection();

			// Faz a busca na tabela usuários que criamos no MySQL Workbench
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios");
			ResultSet rs = stmt.executeQuery();

			// Enquanto houver usuários no banco, cria uma linha na tabela HTML
			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getInt("id") + "</td>");
				out.println("<td>" + rs.getString("login") + "</td>");
				out.println("<td>" + rs.getString("perfil") + "</td>");
				out.println("</tr>");
			}

			conn.close();

		} catch (Exception e) {
			// Se der erro de senha ou de driver, ele mostra aqui na tela
			out.println("<tr><td colspan='3'>Erro de conexão: " + e.getMessage() + "</td></tr>");
			e.printStackTrace();
		}

		out.println("</table>");
		out.println("<br><a href='index.html'>Voltar</a>");
		out.println("</body>");
		out.println("</html>");
	}
}