<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Novo Usuário - ERP Farmácia</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div class="container">
        <h2>Cadastrar Novo Usuário</h2>
        <form action="UsuarioServlet?acao=salvar" method="post">
            <label>Nome Completo:</label>
            <input type="text" name="nome" placeholder="Ex: João Silva" required>

            <label>E-mail:</label>
            <input type="email" name="email" placeholder="email@farmacia.com" required>

            <label>Login (Usuário):</label>
            <input type="text" name="login" placeholder="joao.silva" required>

            <label>Senha Inicial:</label>
            <input type="password" name="senha" required>

            <label>Perfil de Acesso:</label>
            <select name="perfil">
                <option value="OPERADOR">Operador</option>
                <option value="GERENTE">Gerente</option>
                <option value="ADMIN">Administrador</option>
                <option value="CAIXA">Caixa</option>
                
            </select>

            <div style="margin-top: 20px;">
                <button type="submit" class="btn btn-salvar">Cadastrar</button>
                <a href="UsuarioServlet?acao=listar" class="btn">Voltar</a>
            </div>
        </form>
    </div>
</body>
</html>