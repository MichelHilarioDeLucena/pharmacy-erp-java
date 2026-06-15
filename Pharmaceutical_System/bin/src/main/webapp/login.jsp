<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body style="background-color: #f4f7f6;">

    <div class="login-container">
        <h2>ERP Farmácia</h2>
        <p>Acesse sua conta</p>
        <hr>
        <br>

        <form action="LoginServlet" method="POST">
            <div class="form-group">
                <label for="login">Usuário:</label> 
                <input type="text" id="login" name="login" required placeholder="ex: carlos.admin">
            </div>

            <div class="form-group">
                <label for="senha">Senha:</label> 
                <input type="password" id="senha" name="senha" required>
            </div>

            <button type="submit" class="btn btn-ativar" style="width: 100%; padding: 10px;">Entrar</button>
        </form>

        <div class="links-uteis">
            <a href="${pageContext.request.contextPath}/views/auth/esqueciSenha.jsp">Esqueci minha senha</a>
        </div>
    </div>

    <script src="js/script.js"></script>

    <script>
        // O JSP troca isso pelo valor que você colocou no request.setAttribute("mensagem", "...")
        var mensagemServidor = "${mensagem}"; 
        
        if (mensagemServidor && mensagemServidor.trim() !== "") {
            exibirAlerta(mensagemServidor);
        }
    </script>

</body>
</html>