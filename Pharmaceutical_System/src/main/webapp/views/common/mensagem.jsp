<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Aviso do Sistema</title>
    <link rel="stylesheet" type="text/css" href="css/style.css">
</head>
<body>
    <div style="border: 1px solid #ccc; padding: 30px; max-width: 500px; margin: 40px auto; text-align: center; background-color: white; border-radius: 8px; box-shadow: 0 0 10px rgba(0,0,0,0.1);">
        <h2 style="color: #333;">Aviso do Sistema</h2>
        
        <p style="font-size: 16px; color: #555;"><strong><%= request.getAttribute("mensagem") %></strong></p>
        
        <br><br>
        <a href="${pageContext.request.contextPath}/login.jsp" class="btn">Voltar para o Início</a>
    </div>
</body>
</html>