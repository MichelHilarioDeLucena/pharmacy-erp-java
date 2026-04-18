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
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();

		out.println("<!DOCTYPE html>");
		out.println("<html lang='pt-BR'>");
		out.println("<head><title>Lista de Funcionários</title>");
		out.println(
				"<style>table { width: 100%; border-collapse: collapse; } th, td { padding: 10px; border: 1px solid #ddd; text-align: left; } th { background-color: #f4f4f4; }</style>");
		out.println("</head><body>");

		out.println("<h2>Lista de Funcionários (Usuários do Sistema)</h2>");
		out.println("<table>");
		out.println("<tr><th>ID</th><th>Nome</th><th>E-mail</th><th>Login</th><th>Perfil</th></tr>");

		try {
			Connection conn = ConnectionFactory.getConnection();

			// Busca na nova tabela de usuários
			PreparedStatement stmt = conn.prepareStatement("SELECT * FROM usuarios");
			ResultSet rs = stmt.executeQuery();

			while (rs.next()) {
				out.println("<tr>");
				out.println("<td>" + rs.getInt("id") + "</td>");
				// Agora puxamos as colunas novas que você criou no script apocalíptico!
				out.println("<td>" + rs.getString("nome") + "</td>");
				out.println("<td>" + rs.getString("email") + "</td>");
				out.println("<td>" + rs.getString("login") + "</td>");
				out.println("<td>" + rs.getString("perfil") + "</td>");
				out.println("</tr>");
			}

			conn.close();

		} catch (Exception e) {
			out.println("<tr><td colspan='5' style='color:red;'>Erro de conexão: " + e.getMessage() + "</td></tr>");
			e.printStackTrace();
		}

		out.println("</table>");
		out.println(
				"<br><br><a href='index.html' style='padding: 10px; background-color: #333; color: white; text-decoration: none; border-radius: 5px;'>⬅ Voltar ao Dashboard</a>");
		out.println("</body>");
		out.println("</html>");
	}
}