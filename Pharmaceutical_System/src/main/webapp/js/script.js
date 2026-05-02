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

//Checkout.jsp
// ==========================================
// MÓDULO: Frente de Caixa (POS / Checkout)
// ==========================================

let pixInterval;

function iniciarPagamento(event) {
    // Impede o formulário de ser enviado imediatamente
    event.preventDefault(); 
    
    // Pega o valor selecionado no select 'paymentMethod'
    const metodoSelect = document.querySelector('select[name="paymentMethod"]');
    if (!metodoSelect) return;
    
    const metodo = metodoSelect.value;
    fecharModais();

    if (metodo === 'PIX') {
        document.getElementById('modalPix').style.display = 'flex';
        iniciarTimerPix();
    } else if (metodo === 'CREDIT_CARD' || metodo === 'DEBIT_CARD') {
        document.getElementById('tituloCartao').innerText = metodo === 'CREDIT_CARD' ? 'Cartão de Crédito' : 'Cartão de Débito';
        document.getElementById('senhaCartao').value = '';
        document.getElementById('modalCartao').style.display = 'flex';
        document.getElementById('senhaCartao').focus();
    } else if (metodo === 'BOLETO') {
        document.getElementById('modalBoleto').style.display = 'flex';
    } else {
        // Se for CASH (Dinheiro), pula direto para a confirmação
        confirmarPagamento();
    }
}

function iniciarTimerPix() {
    let timer = 5;
    const timerElement = document.getElementById('pixTimer');
    if (timerElement) timerElement.innerText = timer;
    
    clearInterval(pixInterval);
    
    pixInterval = setInterval(() => {
        timer--;
        if (timerElement) timerElement.innerText = timer;
        
        if (timer <= 0) {
            clearInterval(pixInterval);
            confirmarPagamento(); // Auto-completa após 5 segundos
        }
    }, 1000);
}

function validarSenha() {
    const senhaInput = document.getElementById('senhaCartao');
    if (senhaInput && senhaInput.value.length >= 4) {
        confirmarPagamento();
    } else {
        alert('Digite uma senha de 4 dígitos para simular o pagamento.');
    }
}

function fecharModais() {
    clearInterval(pixInterval);
    document.querySelectorAll('.modal-overlay').forEach(modal => {
        modal.style.display = 'none';
    });
}

function confirmarPagamento() {
    fecharModais();
    
    // Adiciona um aviso visual de "Processando"
    const avisoProcessando = document.createElement('div');
    avisoProcessando.className = 'modal-overlay';
    avisoProcessando.style.display = 'flex';
    avisoProcessando.innerHTML = '<h2 style="color: white; font-family: sans-serif;">Processando Venda... 🚀</h2>';
    document.body.appendChild(avisoProcessando);
    
    // Envia o formulário definitivamente para o CheckoutServlet
    const form = document.getElementById('checkoutForm');
    if (form) {
        form.submit();
    }
}