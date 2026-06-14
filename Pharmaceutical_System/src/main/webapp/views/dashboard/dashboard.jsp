<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ page import="java.util.List"%>
<%@ page import="model.Usuario, model.Perfil"%>
<%
Usuario logado = (Usuario) session.getAttribute("usuarioLogado");
if (logado == null) {
	response.sendRedirect(request.getContextPath() + "/login.jsp");
	return;
}

List<Object[]> revenueByPayment = (List<Object[]>) request.getAttribute("revenueByPayment");
List<Object[]> topProducts = (List<Object[]>) request.getAttribute("topProducts");
List<Object[]> revenueByDay = (List<Object[]>) request.getAttribute("revenueByDay");
List<Object[]> revenueByWeek = (List<Object[]>) request.getAttribute("revenueByWeek");
List<Object[]> revenueByMonth = (List<Object[]>) request.getAttribute("revenueByMonth");
List<Object[]> revenueByYear = (List<Object[]>) request.getAttribute("revenueByYear");

Integer totalEstoqueBaixo = (Integer) request.getAttribute("totalEstoqueBaixo");
Integer totalCompras = (Integer) request.getAttribute("totalCompras");
Integer totalFornecedores = (Integer) request.getAttribute("totalFornecedores");

Number totalRevenueObj = (Number) request.getAttribute("totalRevenue");
Number totalSalesObj = (Number) request.getAttribute("totalSales");
double totalRevenue = totalRevenueObj != null ? totalRevenueObj.doubleValue() : 0d;
long totalSales = totalSalesObj != null ? totalSalesObj.longValue() : 0L;

StringBuilder labelsPayment = new StringBuilder();
StringBuilder dataPayment = new StringBuilder();
if (revenueByPayment != null) {
	for (Object[] row : revenueByPayment) {
		labelsPayment.append("'").append(row[0]).append("',");
		dataPayment.append(row[1]).append(",");
	}
}

StringBuilder labelsProducts = new StringBuilder();
StringBuilder dataProducts = new StringBuilder();
if (topProducts != null) {
	for (Object[] row : topProducts) {
		labelsProducts.append("'").append(row[0]).append("',");
		dataProducts.append(row[1]).append(",");
	}
}

StringBuilder labelsDay = new StringBuilder();
StringBuilder dataDay = new StringBuilder();
if (revenueByDay != null) {
	for (int i = revenueByDay.size() - 1; i >= 0; i--) {
		Object[] row = revenueByDay.get(i);
		labelsDay.append("'").append(row[2]).append("/").append(row[1]).append("',");
		dataDay.append(row[3]).append(",");
	}
}

StringBuilder labelsWeek = new StringBuilder();
StringBuilder dataWeek = new StringBuilder();

if (revenueByWeek != null) {
	for (int i = revenueByWeek.size() - 1; i >= 0; i--) {
		Object[] row = revenueByWeek.get(i);

		String yearWeek = row[0].toString();
		String year = yearWeek.substring(0, 4);
		String week = yearWeek.substring(4);

		labelsWeek.append("'").append(year).append("/").append(week).append("',");

		dataWeek.append(row[1]).append(",");
	}
}

StringBuilder labelsMonth = new StringBuilder();
StringBuilder dataMonth = new StringBuilder();
if (revenueByMonth != null) {
	for (int i = revenueByMonth.size() - 1; i >= 0; i--) {
		Object[] row = revenueByMonth.get(i);
		labelsMonth.append("'").append(row[1]).append("/").append(row[0]).append("',");
		dataMonth.append(row[2]).append(",");
	}
}

StringBuilder labelsYear = new StringBuilder();
StringBuilder dataYear = new StringBuilder();
if (revenueByYear != null) {
	for (int i = revenueByYear.size() - 1; i >= 0; i--) {
		Object[] row = revenueByYear.get(i);
		labelsYear.append("'").append(row[0]).append("',");
		dataYear.append(row[1]).append(",");
	}
}

String totalRevenueFormatted = String.format(java.util.Locale.US, "%,.2f", totalRevenue).replace(",", "X")
		.replace(".", ",").replace("X", ".");
%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
<meta charset="UTF-8">
<title>Painel Unificado - ERP Farmácia</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link rel="stylesheet" type="text/css"
	href="${pageContext.request.contextPath}/css/dashboard-unificado.css">
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script defer
	src="${pageContext.request.contextPath}/js/dashboard-unificado.js"></script>
</head>
<body>
	<div class="dashboard-page">
		<header class="dashboard-topbar">
			<div>
				<h1>Painel Unificado</h1>
				<p>Dashboard e relatórios em uma única tela</p>
			</div>
			<div class="dashboard-userbox">
				<span>Logado como <strong><%=logado.getNome()%></strong></span> <span><%=logado.getPerfil()%></span>
				<a href="${pageContext.request.contextPath}/LogoutServlet"
					class="btn btn-danger">Sair</a>
			</div>
		</header>

		<main class="dashboard-layout">
			<section class="dashboard-main">
				<div class="chart-grid">
					<section class="chart-card">
						<h2>Forma de pagamento</h2>
						<div class="chart-wrap">
							<canvas id="paymentChart"></canvas>
						</div>
					</section>
					<section class="chart-card">
						<h2>Top produtos</h2>
						<div class="chart-wrap">
							<canvas id="productsChart"></canvas>
						</div>
					</section>
					<section class="chart-card">
						<h2>Venda por dia</h2>
						<div class="chart-wrap">
							<canvas id="dayChart"></canvas>
						</div>
					</section>
					<section class="chart-card">
						<h2>Venda por semana</h2>
						<div class="chart-wrap">
							<canvas id="weekChart"></canvas>
						</div>
					</section>
					<section class="chart-card">
						<h2>Venda por mês</h2>
						<div class="chart-wrap">
							<canvas id="monthChart"></canvas>
						</div>
					</section>
					<section class="chart-card chart-wide">
						<h2>Venda por ano</h2>
						<div class="chart-wrap">
							<canvas id="yearChart"></canvas>
						</div>
					</section>
				</div>
			</section>

			<aside class="dashboard-aside">
				<section class="panel-card">
					<h2>Relatórios</h2>
					<div class="action-grid">
						<a href="${pageContext.request.contextPath}/home.jsp"
							class="btn btn-ghost">Home</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=estoque-completo-pdf"
							class="btn btn-primary">Estoque PDF</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=estoque-completo-csv"
							class="btn btn-success">Estoque CSV</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=estoque-baixo-pdf"
							class="btn btn-primary">Estoque baixo PDF</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=estoque-baixo-csv"
							class="btn btn-success">Estoque baixo CSV</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=compras-pdf"
							class="btn btn-primary">Compras PDF</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=compras-csv"
							class="btn btn-success">Compras CSV</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=fornecedores-pdf"
							class="btn btn-primary">Fornecedores PDF</a> <a
							href="${pageContext.request.contextPath}/ReportServlet?action=fornecedores-csv"
							class="btn btn-success">Fornecedores CSV</a>
							<a href="${pageContext.request.contextPath}/ReportServlet?action=vendas-pdf" class="btn btn-primary">Vendas PDF</a>
							<a href="${pageContext.request.contextPath}/ReportServlet?action=vendas-csv" class="btn btn-success">Vendas CSV</a>
					</div>
				</section>

			</aside>
		</main>
	</div>

	<script>
		window.dashboardData = {
			payment : {
				labels : [
	<%=labelsPayment.toString()%>
		],
				values : [
	<%=dataPayment.toString()%>
		]
			},
			products : {
				labels : [
	<%=labelsProducts.toString()%>
		],
				values : [
	<%=dataProducts.toString()%>
		]
			},
			day : {
				labels : [
	<%=labelsDay.toString()%>
		],
				values : [
	<%=dataDay.toString()%>
		]
			},
			week : {
				labels : [
	<%=labelsWeek.toString()%>
		],
				values : [
	<%=dataWeek.toString()%>
		]
			},
			month : {
				labels : [
	<%=labelsMonth.toString()%>
		],
				values : [
	<%=dataMonth.toString()%>
		]
			},
			year : {
				labels : [
	<%=labelsYear.toString()%>
		],
				values : [
	<%=dataYear.toString()%>
		]
			}
		};
	</script>
</body>
</html>
