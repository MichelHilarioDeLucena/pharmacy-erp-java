<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List, model.SaleItem, model.Product, java.math.BigDecimal"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>POS Checkout - ERP</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style2.css">
</head>
<body>

    <div class="checkout-container">
        <h2>Point of Sale 🛒</h2>

        <% if ("true".equals(request.getParameter("success"))) {
            String saleId = request.getParameter("saleId");
        %>
        <div class="alert-success">
            <b>✅ Venda finalizada com sucesso e estoque atualizado!</b><br>
            <a href="${pageContext.request.contextPath}/ReceiptServlet?saleId=<%=saleId%>" class="btn-receipt">
               📄 Baixar Cupom Fiscal (PDF)
            </a>
        </div>
        <% } %>

        <% if (request.getAttribute("erro") != null) { %>
        <div style="background:#f8d7da; border:1px solid #f5c6cb; padding:10px; border-radius:6px; margin-bottom:12px; color:#721c24;">
            ⚠️ <%=request.getAttribute("erro")%>
        </div>
        <% } %>

        <!-- BUSCA DE PRODUTO -->
        <h3>Buscar Produto</h3>
        <form action="${pageContext.request.contextPath}/CheckoutServlet" method="POST" style="display:flex; gap:10px; align-items:flex-end; margin-bottom:16px;">
            <input type="hidden" name="action" value="buscar">
            <div>
                <label style="display:block; font-weight:bold; margin-bottom:4px;">ID ou Nome do Produto</label>
                <input type="text" name="termo" placeholder="Ex: 1 ou Tylenol"
                       value="<%=request.getAttribute("termoBusca") != null ? request.getAttribute("termoBusca") : ""%>"
                       style="padding:8px; border:1px solid #ccc; border-radius:4px; width:250px;">
            </div>
            <button type="submit" class="btn-finalizar" style="height:36px; padding:0 16px;">🔍 Buscar</button>
        </form>

        <!-- RESULTADOS DA BUSCA -->
        <%
        List<Product> resultados = (List<Product>) request.getAttribute("resultados");
        if (resultados != null && !resultados.isEmpty()) {
        %>
        <table class="cart-table" style="margin-bottom:20px;">
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Nome</th>
                    <th>Preço</th>
                    <th>Estoque</th>
                    <th>Qtd</th>
                    <th>Ação</th>
                </tr>
            </thead>
            <tbody>
            <% for (Product p : resultados) { %>
                <tr>
                    <td><%=p.getId()%></td>
                    <td><%=p.getName()%></td>
                    <td class="right">R$ <%=p.getSalePrice()%></td>
                    <td class="center"><%=p.getCurrentStock()%></td>
                    <td>
                        <form action="${pageContext.request.contextPath}/CheckoutServlet" method="POST" style="display:flex; gap:6px;">
                            <input type="hidden" name="action" value="add">
                            <input type="hidden" name="productId" value="<%=p.getId()%>">
                            <input type="number" name="quantidade" value="1" min="1" max="<%=p.getCurrentStock()%>"
                                   style="width:60px; padding:4px; border:1px solid #ccc; border-radius:4px;">
                            <button type="submit" class="btn-finalizar" style="height:30px; padding:0 12px;">➕ Adicionar</button>
                        </form>
                    </td>
                </tr>
            <% } %>
            </tbody>
        </table>
        <% } else if (resultados != null && resultados.isEmpty()) { %>
            <p style="color:#888;">Nenhum produto encontrado para "<strong><%=request.getAttribute("termoBusca")%></strong>".</p>
        <% } %>

        <!-- CARRINHO -->
        <h3>Carrinho</h3>
        <table class="cart-table">
            <thead>
                <tr>
                    <th>Produto</th>
                    <th>Qtd</th>
                    <th>Preço Unit.</th>
                    <th>Subtotal</th>
                    <th>Remover</th>
                </tr>
            </thead>
            <tbody>
                <%
                List<SaleItem> cart = (List<SaleItem>) session.getAttribute("cart");
                BigDecimal grandTotal = BigDecimal.ZERO;
                if (cart != null && !cart.isEmpty()) {
                    for (int i = 0; i < cart.size(); i++) {
                        SaleItem item = cart.get(i);
                        grandTotal = grandTotal.add(item.getSubtotal());
                %>
                <tr>
                    <td><%=item.getProductName()%></td>
                    <td class="center"><%=item.getQuantity()%></td>
                    <td class="right">R$ <%=item.getUnitPrice()%></td>
                    <td class="right"><strong>R$ <%=item.getSubtotal()%></strong></td>
                    <td class="center">
                        <form action="${pageContext.request.contextPath}/CheckoutServlet" method="POST">
                            <input type="hidden" name="action" value="remover">
                            <input type="hidden" name="index" value="<%=i%>">
                            <button type="submit" style="background:none; border:none; color:red; cursor:pointer; font-size:1.1em;">✕</button>
                        </form>
                    </td>
                </tr>
                <%
                    }
                } else {
                %>
                <tr>
                    <td colspan="5" class="center empty-cart">Carrinho vazio</td>
                </tr>
                <% } %>
            </tbody>
        </table>

        <% if (cart != null && !cart.isEmpty()) { %>
        <div style="text-align:right; margin-bottom:8px;">
            <form action="${pageContext.request.contextPath}/CheckoutServlet" method="POST" style="display:inline;">
                <input type="hidden" name="action" value="limpar">
                <button type="submit" style="background:none; border:none; color:#dc3545; cursor:pointer; text-decoration:underline;">🗑 Limpar carrinho</button>
            </form>
        </div>
        <% } %>

        <h2 class="total-label">Total: <span class="total-value">R$ <%=grandTotal%></span></h2>

        <fieldset class="payment-section">
            <legend>Pagamento</legend>
            <form id="checkoutForm" action="${pageContext.request.contextPath}/CheckoutServlet" method="POST" onsubmit="iniciarPagamento(event)">
                <input type="hidden" name="action" value="finish">

                <div class="form-group">
                    <label>Cliente (Opcional):</label>
                    <select name="clientId">
                        <option value="">-- Consumidor Final (Não identificado) --</option>
                        <%
                        List<Object[]> clients = (List<Object[]>) request.getAttribute("clientsList");
                        if (clients != null) {
                            for (Object[] c : clients) {
                        %>
                        <option value="<%=c[0]%>"><%=c[1]%> (CPF: <%=c[2]%>)</option>
                        <%
                            }
                        }
                        %>
                    </select>
                </div>

                <div class="form-group">
                    <label>Forma de Pagamento:</label>
                    <select name="paymentMethod" id="paymentMethod" required>
                        <option value="PIX">Pix</option>
                        <option value="CREDIT_CARD">Cartão de Crédito</option>
                        <option value="DEBIT_CARD">Cartão de Débito</option>
                        <option value="CASH">Dinheiro</option>
                    </select>
                </div>

                <button type="submit" class="btn-finalizar" <%=(cart == null || cart.isEmpty()) ? "disabled" : ""%>>
                    Finalizar Venda
                </button>
            </form>
        </fieldset>
    </div>

    <div id="modalPix" class="modal-overlay">
        <div class="modal-content">
            <h3>Pagamento via Pix</h3>
            <p>Escaneie o QR Code abaixo:</p>
            <div class="qr-code-fake">QR CODE FAKE</div>
            <p>Aguardando pagamento... (<span id="pixTimer">5</span>s)</p>
            <button type="button" class="btn-pagar btn-pular" onclick="confirmarPagamento()">Pular (Simular Pago)</button>
        </div>
    </div>

    <div id="modalCartao" class="modal-overlay">
        <div class="modal-content">
            <h3 id="tituloCartao">Pagamento via Cartão</h3>
            <p>Insira a senha do cliente:</p>
            <input type="password" id="senhaCartao" class="input-senha" placeholder="****" maxlength="4">
            <button type="button" class="btn-pagar" onclick="validarSenha()">Confirmar Senha</button>
            <button type="button" class="btn-pagar btn-pular" onclick="fecharModais()">Cancelar</button>
        </div>
    </div>

    <script src="${pageContext.request.contextPath}/js/script.js"></script>
</body>
</html>
