function confirmarAcao(acao, nomeUsuario) {
    var mensagem = "Tem certeza que deseja " + acao.toLowerCase() + " o usuário '" + nomeUsuario + "'?";
    return confirm(mensagem);
}

function exibirAlerta(msg) {
    alert("Ops! " + msg);
}

document.addEventListener('DOMContentLoaded', function() {
    const botoesBloqueados = document.querySelectorAll('.em-breve');
    
    botoesBloqueados.forEach(botao => {
        botao.addEventListener('click', function(event) {
            event.preventDefault(); 
            alert("Este módulo está em desenvolvimento e estará disponível em breve!");
        });
    });
});

function confirmarAcao(acao, nomeUsuario) {
    var mensagem = "Tem certeza que deseja " + acao.toLowerCase() + " o usuário '" + nomeUsuario + "'?";
    return confirm(mensagem);
}

let pixInterval;

function iniciarPagamento(event) {
    event.preventDefault(); 
    
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
            confirmarPagamento();
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
    
    const avisoProcessando = document.createElement('div');
    avisoProcessando.className = 'modal-overlay';
    avisoProcessando.style.display = 'flex';
    avisoProcessando.innerHTML = '<h2 style="color: white; font-family: sans-serif;">Processando Venda... </h2>';
    document.body.appendChild(avisoProcessando);
    
    const form = document.getElementById('checkoutForm');
    if (form) {
        form.submit();
    }
}