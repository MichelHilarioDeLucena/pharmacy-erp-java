<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Supplier, model.Usuario"%>
<%@ page import="model.Category , java.util.List" %>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    Supplier supplier = (Supplier) request.getAttribute("supplier");
    boolean ehEdicao  = (supplier != null);
    String tituloForm = ehEdicao ? "Editar Fornecedor" : "➕ Novo Fornecedor";
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%=tituloForm%> - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-group { margin-bottom: 14px; }
        .form-group label { display: block; font-weight: bold; margin-bottom: 4px; }
        .form-group input, .form-group select {
            width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
        }
        .form-row { display: flex; gap: 16px; }
        .form-row .form-group { flex: 1; }
    </style>
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2><%=tituloForm%></h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong></span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px; max-width: 700px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div style="background: #fff3cd; border: 1px solid #ffc107; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #856404;">
                ⚠️ <%=request.getAttribute("mensagem")%>
            </div>
        <% } %>

        <form method="post" action="SupplierServlet">
        	<input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
            <% if (ehEdicao) { %>
                <input type="hidden" name="id" value="<%=supplier.getId()%>">
            <% } %>

            <div class="form-group">
                <label>Razão Social *</label>
                <input type="text" name="companyName" required
                       value="<%=ehEdicao ? supplier.getCompanyName() : ""%>">
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>CNPJ *</label>
                    <input type="text" name="cnpj" required maxlength="18"
                           placeholder="00.000.000/0000-00"
                           value="<%=ehEdicao ? supplier.getCnpj() : ""%>">
                </div>
                
                
                
                <div class="form-group">
                    <label>Categoria de Fornecimento</label>
                    <select name="categoriaId">
                        <option value="">-- Sem categoria --</option>
                        <%
                        List<Category> categorias = (List<Category>) request.getAttribute("categorias");
                        if (categorias != null) {
                            for (Category c : categorias) {
                                boolean selecionado = ehEdicao && supplier.getSupplyCategory() != null && supplier.getSupplyCategory().equals(c.getId());
                        %>
                        <option value="<%=c.getId()%>" <%=selecionado ? "selected" : ""%>><%=c.getName()%></option>
                        <%
                            }
                        }
                        %>
                    </select>
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Telefone</label>
                    <input type="text" name="phone" maxlength="20"
                           value="<%=ehEdicao && supplier.getPhone() != null ? supplier.getPhone() : ""%>">
                </div>
                <div class="form-group">
                    <label>E-mail</label>
                    <input type="email" name="email" maxlength="100"
                           value="<%=ehEdicao && supplier.getEmail() != null ? supplier.getEmail() : ""%>">
                </div>
            </div>

            <div style="display: flex; gap: 10px; margin-top: 20px;">
                <button type="submit" class="btn btn-ativar">
                    <%=ehEdicao ? "Salvar Alterações" : "➕ Cadastrar Fornecedor"%>
                </button>
                <a href="SupplierServlet" class="btn">← Cancelar</a>
            </div>
        </form>
    </main>
</body>
</html>