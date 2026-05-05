# 💊 ERP Farmácia (Pharmaceutical System)

Sistema ERP Web voltado para o controle operacional e administrativo de farmácias.
O projeto foi desenvolvido em Java com foco em **segurança, organização modular e gestão completa de processos empresariais**.

> ⚠️ **Importante:** Este README descreve apenas a aplicação.
> A **documentação técnica (diagramas, arquitetura, etc.) é uma entrega separada** e **não está incluída aqui**.

---

# 🛠️ Tecnologias Utilizadas

* **Java 13**
* **Java EE 8 (Servlets/JSP)**
* **Hibernate Core (5.6.15)** (JPA)
* **MySQL Connector/J (8.3.0)**
* **jBCrypt (0.4)** (hash de senhas)
* **JavaMail (1.6.2)** (envio de e-mails)
* **Dotenv-Java (3.0.0)** (variáveis de ambiente)
* **Maven** (build)

---

# 🔐 Requisitos do Sistema

## I. Segurança e Controle de Acesso (1-6)

* [PARCIAL] Registro de acessos (**log_acessos**: usuário, data/hora, ação, IP, resultado)
* [ ] Proteção contra CSRF
* [ ] Proteção contra XSS
* [X] Proteção contra SQL Injection (via Hibernate/JPA)
* [X] Timeout de sessão
* [ NÃO TESTADO] Bloqueio após tentativas de login falhas

---

## II. Gestão de Usuários e Perfis (7-12)

* [X] Cadastro de usuários
* [X] Edição de usuários
* [X] Ativação/desativação
* [X] Recuperação de senha via e-mail (token JWT ou UUID com expiração)
* [PARCIAL] Sistema de papéis (roles)
* [PARCIAL] Logs detalhados de operações (CRUD, login, permissões)

---

## III. Gestão de Produtos e Estoque (13-20)

* [ ] Cadastro de produtos
* [ ] Edição de produtos
* [ ] Exclusão lógica
* [ ] Listagem
* [ ] Associação com categorias
* [ ] Associação com fornecedores
* [ ] Controle de estoque (quantidade atual/mínima)
* [ ] Alertas automáticos de estoque baixo

---

## IV. Gestão de Fornecedores e Compras (21-24)

* [ ] Cadastro de fornecedores (CNPJ, contato, produtos)
* [ ] Histórico de compras
* [ ] Simulação de pedidos automáticos (baseado na demanda)

---

## V. Gestão de Vendas e Pagamentos (25-34)

* [ ] Carrinho de compras (checkout)
* [ ] Pagamento via boleto (simulado)
* [ ] Pagamento via cartão (simulado)
* [ ] Pagamento via Pix (simulado)
* [ ] Emissão de recibo (PDF)
* [ ] Atualização automática de estoque
* [ ] Dashboard com gráficos:

  * [ ] Vendas (dia/semana/mês)
  * [ ] Produtos mais vendidos
  * [ ] Faturamento por pagamento

---

## VI. Relatórios (35-37)

* [ ] Relatório de vendas (PDF/CSV)
* [ ] Relatório de estoque baixo
* [ ] Relatório de fornecedores/compras


# ⚙️ Pré-requisitos

* JDK 13+
* Apache Maven
* Apache Tomcat 9+
* MySQL

---

# 🚀 Instalação e Execução

## 1. Banco de Dados

Crie um banco MySQL:

```sql
CREATE DATABASE erp;
```

As tabelas podem ser:

* Geradas automaticamente via Hibernate
* Ou criadas via scripts SQL do projeto

---

## 2. Variáveis de Ambiente

Crie um arquivo `.env` na raiz:

```env
DB_URL=jdbc:mysql://localhost:3306/erp
DB_USER=root
DB_PASSWORD=SUA_SENHA
EMAIL_USER=seu_email@gmail.com
EMAIL_PASS=sua_senha_de_app
```

> ⚠️ Nunca comite esse arquivo.

---

## 3. Execução

```bash
mvn clean install
```

Deploy no Tomcat (ou via plugin configurado).

---

# 🐳 Docker (Opcional)

```bash
docker-compose up --build
```

---

# ☸️ Kubernetes (Opcional)

```bash
kubectl apply -f k8s/
```

---

# 🗄️ Banco de Dados

Scripts disponíveis em:

```
/database
```

Inclui:

* DDL (estrutura)
* DML (dados iniciais)

---

# 🧱 Arquitetura

* Padrão **MVC**
* Camadas:

  * Controller (Servlets)
  * Service
  * Repository (JPA/Hibernate)
  * View (JSP)

---

# 📌 Observações

* Projeto acadêmico
* Algumas funcionalidades podem estar **simuladas** (pagamentos, notificações, etc.)
* Este README **não substitui a documentação técnica exigida na entrega**

---

