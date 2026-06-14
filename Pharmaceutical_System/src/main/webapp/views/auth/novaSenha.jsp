<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Definir Nova Senha - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>

    <div class="card-senha">
        <h2>Nova Senha</h2>
        <p>Crie uma senha forte para sua segurança.</p>
        <hr>
        
       <form action="${pageContext.request.contextPath}/RedefinirSenhaServlet" method="POST">
            <input type="hidden" name="token" value="${param.token}">
            <input type="hidden" name="csrfToken" value="${sessionScope.csrfToken}" />

            <div class="form-group">
                <label for="senha">Nova Senha:</label>
                <input type="password" id="senha" name="senha" required 
                       placeholder="Mínimo 6 caracteres" minlength="6">
            </div>

            <button type="submit" class="btn-ativar" style="width: 100%; cursor: pointer;">
                Atualizar Senha
            </button>
        </form>
    </div>

</body>
</html>