<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Recuperar Senha</title>
<link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
	<div class="container">
		<h2>Esqueci minha senha</h2>

		<form action="${pageContext.request.contextPath}/EsqueciSenhaServlet"
			method="POST">
			<input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />
			<label for="email">Digite o e-mail cadastrado:</label> <input
				type="email" id="email" name="email" required>

			<div style="margin-top: 20px;">
				<button type="submit" class="btn btn-ativar">Enviar Link de
					Recuperação</button>
				<a href="${pageContext.request.contextPath}/login.jsp" class="btn">Voltar</a>
			</div>
		</form>
	</div>
</body>
</html>