<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.Supplier, model.Product, model.Usuario"%>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }
    List<Supplier> suppliers = (List<Supplier>) request.getAttribute("suppliers");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Nova Compra - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
    <style>
        .form-group { margin-bottom: 14px; }
        .form-group label { display: block; font-weight: bold; margin-bottom: 4px; }
        .form-group input, .form-group select {
            width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
        }
        .form-row { display: flex; gap: 16px; }
        .form-row .form-group { flex: 1; }
        .item-row { display: flex; gap: 10px; align-items: flex-end; margin-bottom: 10px; padding: 10px; background: #f8f9fa; border-radius: 6px; }
        .item-row .form-group { margin-bottom: 0; }
    </style>
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2>➕ Nova Compra</h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong></span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px; max-width: 800px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div style="background: #fff3cd; border: 1px solid #ffc107; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #856404;">
                ⚠️ <%=request.getAttribute("mensagem")%>
            </div>
        <% } %>

        <form method="post" action="PurchaseServlet">

            <div class="form-group">
                <label>Fornecedor *</label>
                <select name="supplierId" required>
                    <option value="">-- Selecione --</option>
                    <%
                    if (suppliers != null) {
                        for (Supplier s : suppliers) {
                    %>
                        <option value="<%=s.getId()%>"><%=s.getCompanyName()%> — <%=s.getCnpj()%></option>
                    <%
                        }
                    }
                    %>
                </select>
            </div>

            <h3 style="margin-top: 24px;">Itens da Compra</h3>
            <div id="itens">
                <div class="item-row">
                    <div class="form-group" style="flex: 1;">
                        <label>ID do Produto</label>
                        <input type="number" name="productId" min="1" required placeholder="ID do produto">
                    </div>
                    <div class="form-group" style="width: 100px;">
                        <label>Quantidade</label>
                        <input type="number" name="quantity" min="1" value="1" required>
                    </div>
                    <div class="form-group" style="width: 130px;">
                        <label>Preço Unit. (R$)</label>
                        <input type="number" name="unitPrice" step="0.01" min="0" required>
                    </div>
                </div>
            </div>

            <button type="button" class="btn" onclick="adicionarItem()" style="margin-bottom: 20px;">
                ➕ Adicionar Item
            </button>

            <div style="display: flex; gap: 10px; margin-top: 10px;">
                <button type="submit" class="btn btn-ativar">Registrar Compra</button>
                <a href="PurchaseServlet" class="btn">Cancelar</a>
            </div>
        </form>
    </main>

    <script>
        function adicionarItem() {
            const div = document.createElement('div');
            div.className = 'item-row';
            div.innerHTML = `
                <div class="form-group" style="flex: 1;">
                    <label>ID do Produto</label>
                    <input type="number" name="productId" min="1" required placeholder="ID do produto">
                </div>
                <div class="form-group" style="width: 100px;">
                    <label>Quantidade</label>
                    <input type="number" name="quantity" min="1" value="1" required>
                </div>
                <div class="form-group" style="width: 130px;">
                    <label>Preço Unit. (R$)</label>
                    <input type="number" name="unitPrice" step="0.01" min="0" required>
                </div>
                <button type="button" class="btn btn-desativar" onclick="this.parentElement.remove()">✕</button>
            `;
            document.getElementById('itens').appendChild(div);
        }
    </script>
</body>
</html>
