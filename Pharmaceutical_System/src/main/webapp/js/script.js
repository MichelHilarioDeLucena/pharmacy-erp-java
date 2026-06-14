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
<<<<<<< HEAD
    event.preventDefault(); 
    
=======
    event.preventDefault();

    // Remove qualquer campo hidden que tenha sido criado anteriormente
    const oldHidden = document.getElementById('cardPasswordHidden');
    if (oldHidden) oldHidden.remove();

>>>>>>> main
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
<<<<<<< HEAD
    } else if (metodo === 'BOLETO') {
        document.getElementById('modalBoleto').style.display = 'flex';
    } else {
=======
    } else if (metodo === 'CASH') {
	    // Atualiza o valor total no modal
	    const totalSpan = document.querySelector('.total-value');
	    if (totalSpan) {
	        document.getElementById('totalCompra').innerText = totalSpan.innerText;
	    }
	    document.getElementById('modalDinheiro').style.display = 'flex';
	} else if (metodo === 'BOLETO') {
    	document.getElementById('modalBoleto').style.display = 'flex';
	}else{
>>>>>>> main
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
	console.log('Formulário encontrado?', document.getElementById('checkoutForm'));
    const senhaInput = document.getElementById('senhaCartao');
    if (senhaInput && senhaInput.value.length >= 4) {
        // Garante que o campo hidden exista (cria se não existir)
        let hiddenPassword = document.getElementById('cardPasswordHidden');
        if (!hiddenPassword) {
            hiddenPassword = document.createElement('input');
            hiddenPassword.type = 'hidden';
            hiddenPassword.name = 'cardPassword';
            hiddenPassword.id = 'cardPasswordHidden';
            const form = document.getElementById('checkoutForm');
            if (form) {
                form.appendChild(hiddenPassword);
                console.log('Campo hidden criado dinamicamente');
            } else {
                console.error('Formulário checkoutForm não encontrado');
                alert('Erro interno: formulário não encontrado.');
                return;
            }
        }
        hiddenPassword.value = senhaInput.value;
        console.log('Senha copiada para hidden:', hiddenPassword.value);
        
        // Agora pode fechar o modal e submeter
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
    avisoProcessando.innerHTML = '<h2 style="color: white; font-family: sans-serif;">Processando Venda... 🚀</h2>';
    document.body.appendChild(avisoProcessando);
    
<<<<<<< HEAD
=======
    const hidden = document.getElementById('cardPasswordHidden');
    console.log('Valor do hidden antes de enviar:', hidden ? hidden.value : 'hidden não encontrado');
    
    // Envia o formulário definitivamente para o CheckoutServlet
>>>>>>> main
    const form = document.getElementById('checkoutForm');
    if (form) {
        form.submit();
    }
}
function confirmarDinheiro() {
    const valorPagoStr = document.getElementById('valorPago').value;
    if (!valorPagoStr) {
        alert('Digite o valor recebido.');
        return;
    }
    // Converte vírgula para ponto e valida
    let valorPago = parseFloat(valorPagoStr.replace(',', '.'));
    if (isNaN(valorPago)) {
        alert('Valor inválido.');
        return;
    }
    // Pega o total do carrinho (valor exibido)
    const totalSpan = document.querySelector('.total-value');
    let total = 0;
    if (totalSpan) {
        total = parseFloat(totalSpan.innerText.replace('R$', '').replace(',', '.').trim());
    }
    if (valorPago < total) {
        document.getElementById('trocoMsg').innerHTML = `<span style="color:red;">Valor insuficiente. Faltam R$ ${(total - valorPago).toFixed(2)}</span>`;
        return;
    }
    const troco = valorPago - total;
    if (troco > 0) {
        alert(`Pagamento confirmado. Troco: R$ ${troco.toFixed(2)}`);
    }
    // Armazena o valor pago e troco em campos hidden do formulário (opcional)
    let hiddenPago = document.getElementById('valorPagoHidden');
    if (!hiddenPago) {
        hiddenPago = document.createElement('input');
        hiddenPago.type = 'hidden';
        hiddenPago.name = 'valorPago';
        hiddenPago.id = 'valorPagoHidden';
        document.getElementById('checkoutForm').appendChild(hiddenPago);
    }
    hiddenPago.value = valorPago.toFixed(2);
    
    let hiddenTroco = document.getElementById('trocoHidden');
    if (!hiddenTroco) {
        hiddenTroco = document.createElement('input');
        hiddenTroco.type = 'hidden';
        hiddenTroco.name = 'troco';
        hiddenTroco.id = 'trocoHidden';
        document.getElementById('checkoutForm').appendChild(hiddenTroco);
    }
    hiddenTroco.value = troco.toFixed(2);
    
    fecharModais();
    confirmarPagamento(); // finaliza a venda
}

function confirmarBoleto() {
    fecharModais();
    confirmarPagamento(); // finaliza a venda
}