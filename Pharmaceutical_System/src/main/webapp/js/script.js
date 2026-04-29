// src/main/webapp/js/script.js

function confirmarAcao(acao, nomeUsuario) {
    // Exibe um pop-up de confirmação no navegador
    var mensagem = "Tem certeza que deseja " + acao.toLowerCase() + " o usuário '" + nomeUsuario + "'?";
    return confirm(mensagem);
}

function exibirAlerta(msg) {
    // Por enquanto usamos o alert nativo, que é um pop-up que trava a tela até o OK
    // Mas agora ele só aparece se o Servlet enviar uma mensagem
    alert("⚠️ Ops! " + msg);
}

// --- Funções do Dashboard ---

// Seleciona todos os botões com a classe 'em-breve'
document.addEventListener('DOMContentLoaded', function() {
    const botoesBloqueados = document.querySelectorAll('.em-breve');
    
    botoesBloqueados.forEach(botao => {
        botao.addEventListener('click', function(event) {
            event.preventDefault(); // Impede o link de tentar navegar
            alert("Este módulo está em desenvolvimento e estará disponível em breve!");
        });
    });
});

// Mantém a função que já tínhamos antes
function confirmarAcao(acao, nomeUsuario) {
    var mensagem = "Tem certeza que deseja " + acao.toLowerCase() + " o usuário '" + nomeUsuario + "'?";
    return confirm(mensagem);
}