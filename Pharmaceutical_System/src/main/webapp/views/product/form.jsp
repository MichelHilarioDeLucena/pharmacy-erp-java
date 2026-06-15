<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="model.Product"%>
<%@ page import="model.Usuario, model.Perfil"%>
<%@ page import="model.Supplier, model.Category , java.util.List" %>
<%
    Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
    if (logado == null) {
        response.sendRedirect(request.getContextPath() + "/login.jsp");
        return;
    }

    Product produto = (Product) request.getAttribute("produto");
    boolean ehEdicao = (produto != null);
    String tituloForm = ehEdicao ? "✎ Editar Produto" : "➕ Novo Produto";
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
        .form-group input, .form-group textarea, .form-group select {
            width: 100%; padding: 8px; border: 1px solid #ccc; border-radius: 4px; box-sizing: border-box;
        }
        .form-row { display: flex; gap: 16px; }
        .form-row .form-group { flex: 1; }
        .alerta-box { background: #fff3cd; border: 1px solid #ffc107; padding: 10px 16px; border-radius: 6px; margin-bottom: 16px; color: #856404; }
    </style>
</head>
<body>
    <header style="display: flex; justify-content: space-between; align-items: center; padding: 10px 20px; background: #eee; border-bottom: 2px solid #ccc;">
        <h2><%=tituloForm%></h2>
        <div>
            <span>Logado como: <strong><%=logado.getNome()%></strong> (<%=logado.getPerfil()%>)</span>
            <a href="LogoutServlet" style="margin-left: 15px; color: red; text-decoration: none;">Sair</a>
        </div>
    </header>

    <main style="padding: 20px; max-width: 700px;">

        <% if (request.getAttribute("mensagem") != null) { %>
            <div class="alerta-box">⚠️ <%=request.getAttribute("mensagem")%></div>
        <% } %>

        <form method="post" action="ProductServlet">
        <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
            <% if (ehEdicao) { %>
                <input type="hidden" name="id" value="<%=produto.getId()%>">
            <% } %>

            <div class="form-group">
                <label>Nome do Produto *</label>
                <input type="text" name="nome" required
                       value="<%=ehEdicao ? produto.getName() : ""%>">
            </div>

            <div class="form-group">
                <label>Código de Barras</label>
                <input type="text" name="codigoBarras"
                       value="<%=ehEdicao && produto.getBarcode() != null ? produto.getBarcode() : ""%>">
            </div>

            <div class="form-group">
                <label>Descrição</label>
                <textarea name="descricao" rows="3"><%=ehEdicao && produto.getDescription() != null ? produto.getDescription() : ""%></textarea>
            </div>
            
		
			
            <div class="form-row">
                <div class="form-group">
                    <label>Preço de Custo (R$) *</label>
                    <input type="number" name="precoCusto" step="0.01" min="0" required
                           value="<%=ehEdicao ? produto.getCostPrice() : ""%>">
                </div>
                <div class="form-group">
                    <label>Preço de Venda (R$) *</label>
                    <input type="number" name="precoVenda" step="0.01" min="0" required
                           value="<%=ehEdicao ? produto.getSalePrice() : ""%>">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Quantidade Atual *</label>
                    <input type="number" name="qtdAtual" min="0" required
                           value="<%=ehEdicao ? produto.getCurrentStock() : "0"%>">
                </div>
                <div class="form-group">
                    <label>Quantidade Mínima *</label>
                    <input type="number" name="qtdMinima" min="0" required
                           value="<%=ehEdicao ? produto.getMinStock() : "5"%>">
                </div>
            </div>

            <div class="form-row">
                <div class="form-group">
                    <label>Data de Validade</label>
                    <input type="date" name="dataValidade"
                           value="<%=ehEdicao && produto.getExpirationDate() != null ? produto.getExpirationDate().toString() : ""%>">
                </div>
                 <div class="form-group">
                    <label>Categoria</label>
                    <select name="categoriaId">
                        <option value="">-- Sem categoria --</option>
                        <%
                        List<Category> categorias = (List<Category>) request.getAttribute("categorias");
                        if (categorias != null) {
                            for (Category c : categorias) {
                                boolean selecionado = ehEdicao && produto.getCategoryId() != null && produto.getCategoryId().equals(c.getId());
                        %>
                        <option value="<%=c.getId()%>" <%=selecionado ? "selected" : ""%>><%=c.getName()%></option>
                        <%
                            }
                        }
                        %>
                    </select>
                </div>

            </div>

            <div style="display: flex; gap: 10px; margin-top: 20px;">
                <button type="submit" class="btn btn-ativar">
                    <%=ehEdicao ? "Salvar Alterações" : "➕ Cadastrar Produto"%>
                </button>
                <a href="ProductServlet" class="btn">← Cancelar</a>
            </div>
        </form>
    </main>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
