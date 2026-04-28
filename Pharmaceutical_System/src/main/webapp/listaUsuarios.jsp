<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="model.Usuario" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Gerenciar Usuários</title>
    
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <h2>Gestão de Usuários</h2>
    
    <table>
        <tr>
            <th>ID</th>
            <th>Nome</th>
            <th>Status</th>
            <th>Ações</th>
        </tr>
        
        <% 
            List<Usuario> lista = (List<Usuario>) request.getAttribute("listaUsuarios");
            if (lista != null && !lista.isEmpty()) {
                for (Usuario u : lista) {
                    String statusClass = u.isAtivo() ? "ativo" : "inativo";
                    String statusTexto = u.isAtivo() ? "Ativo" : "Desativado";
                    String textoBotao = u.isAtivo() ? "Desativar" : "Ativar";
                    String classeBotao = u.isAtivo() ? "btn btn-desativar" : "btn btn-ativar";
        %>
        <tr>
            <td><%= u.getId() %></td>
            <td><%= u.getNome() %></td>
            <td class="<%= statusClass %>"><%= statusTexto %></td>
            <td>
                <a href="UsuarioServlet?acao=alternar&id=<%= u.getId() %>" 
                   class="<%= classeBotao %>" 
                   onclick="return confirmarAcao('<%= textoBotao %>', '<%= u.getNome() %>');">
                   <%= textoBotao %>
                </a>
            </td>
        </tr>
        <% 
                } 
            } else { 
        %>
            <tr><td colspan="4" style="text-align:center;">Nenhum usuário encontrado.</td></tr>
        <% } %>
    </table>

    <br>
    <a href="index.html" class="btn">Voltar para Início</a>

    <script src="js/script.js"></script>
</body>
</html>