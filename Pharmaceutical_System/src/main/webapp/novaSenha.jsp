<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="pt-br">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Definir Nova Senha - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
    <style>
        .card-senha {
            max-width: 400px;
            margin: 80px auto;
            padding: 25px;
            background: #fff;
            border-radius: 8px;
            box-shadow: 0 4px 15px rgba(0,0,0,0.1);
            text-align: center;
        }
        .form-group {
            text-align: left;
            margin-bottom: 15px;
        }
        input[type="password"] {
            width: 100%;
            padding: 12px;
            border: 1px solid #ddd;
            border-radius: 4px;
            box-sizing: border-box; /* Garante que o padding não estoure a largura */
        }
    </style>
</head>
<body>

    <div class="card-senha">
        <h2>🔒 Nova Senha</h2>
        <p>Crie uma senha forte para sua segurança.</p>
        <hr>
        
        <form action="RedefinirSenhaServlet" method="POST">
            <input type="hidden" name="token" value="${token}">

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