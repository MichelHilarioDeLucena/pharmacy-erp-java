<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page
	import="java.util.List, model.SaleItem, model.Product, java.math.BigDecimal"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>POS Checkout - ERP</title>
<style>
.product-grid {
	display: flex;
	gap: 10px;
	margin-bottom: 20px;
}

.product-card {
	border: 1px solid #ccc;
	padding: 15px;
	text-align: center;
	border-radius: 8px;
	width: 150px;
	background: #f9f9f9;
}

.product-card button {
	width: 100%;
	padding: 10px;
	background: #28a745;
	color: white;
	border: none;
	cursor: pointer;
	border-radius: 4px;
}

.product-card button:hover {
	background: #218838;
}

/* Estilos para os Modais de Pagamento */
.modal-overlay {
    display: none; position: fixed; top: 0; left: 0; width: 100%; height: 100%;
    background: rgba(0,0,0,0.6); z-index: 1000; justify-content: center; align-items: center;
}
.modal-content {
    background: white; padding: 30px; border-radius: 8px; text-align: center;
    width: 350px; box-shadow: 0 4px 8px rgba(0,0,0,0.2);
}
.modal-content h3 { margin-top: 0; }
.qr-code-fake { width: 150px; height: 150px; background: #eee; margin: 15px auto; display: flex; align-items: center; justify-content: center; border: 2px dashed #ccc; font-weight: bold; color: #555;}
.input-senha { width: 80%; padding: 10px; margin: 15px 0; text-align: center; letter-spacing: 5px; font-size: 18px; }
.btn-pagar { background: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 4px; cursor: pointer; width: 100%;}
.btn-pagar:hover { background: #0056b3; }
.btn-pular { background: #6c757d; margin-top: 10px; }
</style>
</head>
<body>


	<h2>Point of Sale 🛒</h2>

<%
	if ("true".equals(request.getParameter("success"))) {
	    // Pega o ID da venda que o Servlet mandou na URL
	    String saleId = request.getParameter("saleId"); 
	%>
	<div style="background: #d4edda; color: #155724; padding: 15px; border-radius: 5px; margin-bottom: 20px; border: 1px solid #c3e6cb;">
	    <b>✅ Venda finalizada com sucesso e estoque atualizado!</b><br>
	    
	    <a href="${pageContext.request.contextPath}/ReceiptServlet?saleId=<%=saleId%>" 
	       style="display: inline-block; margin-top: 10px; padding: 8px 15px; background: #28a745; color: white; text-decoration: none; border-radius: 4px; font-weight: bold;">
	       📄 Baixar Cupom Fiscal (PDF)
	    </a>
	</div>
	<%
	}
	%>
	<h3>Produtos Disponíveis (Clique para "Bipar")</h3>
	<div class="product-grid">
		<%
		List<Product> products = (List<Product>) request.getAttribute("productsList");
		if (products != null) {
			for (Product p : products) {
		%>
		<div class="product-card">
			<h4><%=p.getName()%></h4>
			<p>
				R$
				<%=p.getSalePrice()%></p>
			<p>
				<small>Estoque: <%=p.getCurrentStock()%></small>
			</p>
			<form action="${pageContext.request.contextPath}/CheckoutServlet"
				method="POST">
				<input type="hidden" name="action" value="add"> <input
					type="hidden" name="productId" value="<%=p.getId()%>">
				<button type="submit">Adicionar</button>
			</form>
		</div>
		<%
		}
		}
		%>
	</div>

	<h3>Carrinho</h3>
	<table border="1" width="100%">
		<tr>
			<th>Produto</th>
			<th>Qtd</th>
			<th>Preço Unit.</th>
			<th>Subtotal</th>
		</tr>
		<%
		List<SaleItem> cart = (List<SaleItem>) session.getAttribute("cart");
		BigDecimal grandTotal = BigDecimal.ZERO;
		if (cart != null && !cart.isEmpty()) {
			for (SaleItem item : cart) {
				grandTotal = grandTotal.add(item.getSubtotal());
		%>
		<tr>
			<td><%=item.getProductName()%></td>
			<td><%=item.getQuantity()%></td>
			<td>R$ <%=item.getUnitPrice()%></td>
			<td>R$ <%=item.getSubtotal()%></td>
		</tr>
		<%
		}
		} else {
		%>
		<tr>
			<td colspan="4">Carrinho vazio</td>
		</tr>
		<%
		}
		%>
	</table>
	<h2>
		Total: R$
		<%=grandTotal%></h2>

	<fieldset style="margin-top: 20px;">
		<legend>Pagamento</legend>
		<form id="checkoutForm" action="${pageContext.request.contextPath}/CheckoutServlet" method="POST" onsubmit="iniciarPagamento(event)">
			<input type="hidden" name="action" value="finish"> <label>Cliente
				(Opcional):</label> <select name="clientId">
				<option value="">-- Consumidor Final (Não identificado) --</option>
				<%
				List<Object[]> clients = (List<Object[]>) request.getAttribute("clientsList");
				if (clients != null) {
					for (Object[] c : clients) {
				%>
				<option value="<%=c[0]%>"><%=c[1]%> (CPF:
					<%=c[2]%>)
				</option>
				<%
				}
				}
				%>
			</select> <br>
			<br> <label>Forma de Pagamento:</label> <select
				name="paymentMethod" required>
				<option value="PIX">Pix</option>
				<option value="CREDIT_CARD">Cartão de Crédito</option>
				<option value="DEBIT_CARD">Cartão de Débito</option>
				<option value="CASH">Dinheiro</option>
			</select> <br>
			<br>

			<button type="submit" style="padding: 10px 20px; font-size: 16px;"
				<%=(cart == null || cart.isEmpty()) ? "disabled" : ""%>>Finalizar
				Venda</button>
		</form>
	</fieldset>
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

    <div id="modalBoleto" class="modal-overlay">
        <div class="modal-content">
            <h3>Boleto Bancário</h3>
            <p>Código: 34191.09008 63571.277308 71444.640008 1 900000000000</p>
            <button type="button" class="btn-pagar" onclick="confirmarPagamento()">Confirmar</button>
        </div>
    </div>
  <script src="js/script.js"></script>
</body>
</html>