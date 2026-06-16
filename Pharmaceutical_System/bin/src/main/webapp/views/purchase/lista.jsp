<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Purchase, model.PurchaseItem, model.Usuario, model.Perfil"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    Perfil perfil = logado.getPerfil();
    boolean temAcesso = (perfil == Perfil.ADMIN || perfil == Perfil.GERENTE);
    String filtro = (String) request.getAttribute("filtro");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Compras - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .badge-pendente   { background: #ffc107; color: #333; padding: 2px 8px; border-radius: 10px; font-size: 0.8em; }
        .badge-confirmado { background: #28a745; color: white; padding: 2px 8px; border-radius: 10px; font-size: 0.8em; }
        .badge-cancelado  { background: #dc3545; color: white; padding: 2px 8px; border-radius: 10px; font-size: 0.8em; }
    </style>
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2>Histórico de Compras</h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong> (<%=logado.getPerfil()%>)</span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div style="background: #d4edda; border: 1px solid #28a745; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #155724;">
                ✅ <%=request.getAttribute("mensagem")%>
            </div>
        <% } %>

        <div style="display: flex; gap: 10px; margin-bottom: 20px;">
            <% if (temAcesso) { %>
                <a href="PurchaseServlet?action=novo" class="btn btn-novo">➕ Nova Compra</a>
                <a href="PurchaseServlet?action=automatico" class="btn btn-ativar"
                   onclick="return confirm('Gerar pedidos automáticos para produtos com estoque baixo?');">
                   Pedidos Automáticos
                </a>
                <a href="PurchaseServlet?action=pendentes" class="btn">Só Pendentes</a>
            <% } %>
            <a href="PurchaseServlet" class="btn">Todos</a>
            <a href="SupplierServlet" class="btn">← Fornecedores</a>
            <a href="<%= request.getContextPath() %>/home.jsp" class="btn">Home</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Fornecedor</th>
                    <th>Operador</th>
                    <th>Data</th>
                    <th>Total</th>
                    <th>Status</th>
                    <th>Itens</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                List<Purchase> purchases = (List<Purchase>) request.getAttribute("purchases");
                if (purchases != null && !purchases.isEmpty()) {
                    for (Purchase p : purchases) {
                        String badgeClass = "badge-pendente";
                        if ("CONFIRMADO".equals(p.getOrderStatus())) badgeClass = "badge-confirmado";
                        else if ("CANCELADO".equals(p.getOrderStatus())) badgeClass = "badge-cancelado";
                %>
                <tr>
                    <td><%=p.getId()%></td>
                    <td><%=p.getSupplier().getCompanyName()%></td>
                    <td><%=p.getOperator().getNome()%></td>
                    <td><%=p.getPurchaseDate().toLocalDate()%></td>
                    <td>R$ <%=p.getTotalAmount()%></td>
                    <td><span class="<%=badgeClass%>"><%=p.getOrderStatus()%></span></td>
                    <td><%=p.getItems().size()%> item(s)</td>
                    <td>
                        <div style="display: flex; gap: 5px;">
                            <% if (temAcesso && "PENDENTE".equals(p.getOrderStatus())) { %>
                                <a href="PurchaseServlet?action=confirmar&id=<%=p.getId()%>"
                                   class="btn btn-ativar"
                                   onclick="return confirm('Confirmar compra e dar entrada no estoque?');">
                                   ✅ Confirmar
                                </a>
                                <a href="PurchaseServlet?action=cancelar&id=<%=p.getId()%>"
                                   class="btn btn-desativar"
                                   onclick="return confirm('Cancelar esta compra?');">
                                   ❌ Cancelar
                                </a>
                            <% } %>
                        </div>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="8" style="text-align: center;">Nenhuma compra encontrada.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </main>
</body>
</html>
