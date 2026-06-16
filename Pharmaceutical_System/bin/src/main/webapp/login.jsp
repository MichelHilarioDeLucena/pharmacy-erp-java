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
            
            <div style="text-align: center; margin: 15px 0; color: #777; font-size: 14px;">ou</div>

            <a href="https://accounts.google.com/o/oauth2/v2/auth?scope=email%20profile&response_type=code&redirect_uri=http://localhost:8081/oauth2callback&client_id=782762727256-dlbj3gi0p6gm7a7ncb8alq4ek59uq4t1.apps.googleusercontent.com" 
               class="btn" 
               style="display: block; width: 100%; padding: 10px 0; background-color: #4285F4; color: white; text-decoration: none; border-radius: 4px; font-weight: bold; text-align: center; box-sizing: border-box;">
               Entrar com o Google
            </a>
        </form>

        <div class="links-uteis" style="margin-top: 20px;">
            <a href="${pageContext.request.contextPath}/views/auth/esqueciSenha.jsp">Esqueci minha senha</a>
        </div>
    </div>

    <script src="js/script.js"></script>

    <script>
        var mensagemServidor = "${mensagem}"; 
        
        if (mensagemServidor && mensagemServidor.trim() !== "") {
            exibirAlerta(mensagemServidor);
        }
    </script>

</body>
</html>
