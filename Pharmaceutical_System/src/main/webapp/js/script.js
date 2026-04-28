// src/main/webapp/js/script.js

function confirmarAcao(acao, nomeUsuario) {
    // Exibe um pop-up de confirmação no navegador
    var mensagem = "Tem certeza que deseja " + acao.toLowerCase() + " o usuário '" + nomeUsuario + "'?";
    return confirm(mensagem);
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