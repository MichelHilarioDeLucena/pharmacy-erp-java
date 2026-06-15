<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Supplier, model.Usuario, model.Perfil"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    Perfil perfil = logado.getPerfil();
    boolean temAcesso = (perfil == Perfil.ADMIN || perfil == Perfil.GERENTE);
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Fornecedores - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2>Gestão de Fornecedores</h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong> (<%=logado.getPerfil()%>)</span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div style="background: #fff3cd; border: 1px solid #ffc107; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #856404;">
                <%=request.getAttribute("mensagem")%>
            </div>
        <% } %>

        <div style="display: flex; gap: 10px; margin-bottom: 20px;">
            <% if (temAcesso) { %>
                <a href="SupplierServlet?action=novo" class="btn btn-novo">Novo Fornecedor</a>
                <a href="PurchaseServlet" class="btn">Histórico de Compras</a>
                <a href="PurchaseServlet?action=automatico" class="btn btn-ativar"
                   onclick="return confirm('Gerar pedidos automáticos para produtos com estoque baixo?');">
                   Pedidos Automáticos
                </a>
            <% } %>
            <a href="home.jsp" class="btn">Voltar</a>
        </div>

        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Razão Social</th>
                    <th>CNPJ</th>
                    <th>Categoria</th>
                    <th>Telefone</th>
                    <th>E-mail</th>
                    <th>Status</th>
                    <th>Ações</th>
                </tr>
            </thead>
            <tbody>
                <%
                List<Supplier> suppliers = (List<Supplier>) request.getAttribute("suppliers");
                if (suppliers != null && !suppliers.isEmpty()) {
                    for (Supplier s : suppliers) {
                        String statusTexto = s.getActive() ? "Ativo" : "Inativo";
                        String classStatus = s.getActive() ? "status-ativo" : "status-inativo";
                %>
                <tr>
                    <td><%=s.getId()%></td>
                    <td><%=s.getCompanyName()%></td>
                    <td><%=s.getCnpj()%></td>
                    <td><%=s.getSupplyCategory() != null ? s.getSupplyCategory() : "-"%></td>
                    <td><%=s.getPhone() != null ? s.getPhone() : "-"%></td>
                    <td><%=s.getEmail() != null ? s.getEmail() : "-"%></td>
                    <td class="<%=classStatus%>"><%=statusTexto%></td>
                    <td>
                        <div style="display: flex; gap: 5px;">
                            <% if (temAcesso) { %>
                                <a href="SupplierServlet?action=editar&id=<%=s.getId()%>" class="btn btn-editar">Editar</a>
                                <% if (s.getActive()) { %>
                                    <a href="SupplierServlet?action=desativar&id=<%=s.getId()%>"
                                       class="btn btn-desativar"
                                       onclick="return confirm('Desativar <%=s.getCompanyName()%>?');">
                                       Desativar
                                    </a>
                                <% } else { %>
                                    <a href="SupplierServlet?action=reativar&id=<%=s.getId()%>"
                                       class="btn btn-ativar">
                                       Reativar
                                    </a>
                                <% } %>
                            <% } %>
                            <a href="PurchaseServlet?action=historico&supplierId=<%=s.getId()%>" class="btn">Compras</a>
                        </div>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="8" style="text-align: center;">Nenhum fornecedor cadastrado.</td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </main>
</body>
</html>