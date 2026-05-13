# 💊 ERP Farmácia

Sistema ERP Web voltado para o controle operacional e administrativo de farmácias.
Desenvolvido em Java com foco em **segurança, organização modular e gestão completa de processos empresariais**.

> ⚠️ **Importante:** Este README descreve apenas a aplicação.
> A **documentação técnica (diagramas, arquitetura, etc.) é uma entrega separada** e não está incluída aqui.

---

# 🛠️ Tecnologias Utilizadas

| Tecnologia | Versão | Uso |
|---|---|---|
| Java | 17 (no container) / 13+ (local) | Linguagem principal |
| Java EE | 8 | Servlets / JSP |
| Hibernate Core | 5.6.15 | ORM / JPA |
| MySQL Connector/J | 8.3.0 | Driver JDBC |
| jBCrypt | 0.4 | Hash de senhas |
| JavaMail | 1.6.2 | Envio de e-mails |
| Dotenv-Java | 3.0.0 | Variáveis de ambiente (execução local) |
| Maven | — | Build |
| Apache Tomcat | 9.0 | Servidor de aplicação |
| MySQL | 8.0 | Banco de dados |
| Docker / Docker Compose | — | Containerização |

---

# 🔐 Requisitos do Sistema

## I. Segurança e Controle de Acesso

- [PARCIAL] Registro de acessos (`log_acessos`: usuário, data/hora, ação, IP, resultado)
- [ ] Proteção contra CSRF
- [ ] Proteção contra XSS
- [X] Proteção contra SQL Injection (via Hibernate/JPA)
- [X] Timeout de sessão
- [NÃO TESTADO] Bloqueio após tentativas de login falhas

## II. Gestão de Usuários e Perfis

- [X] Cadastro de usuários
- [X] Edição de usuários
- [X] Ativação/desativação
- [X] Recuperação de senha via e-mail (token com expiração)
- [PARCIAL] Sistema de papéis (roles)
- [PARCIAL] Logs detalhados de operações (CRUD, login, permissões)

## III. Gestão de Produtos e Estoque

- [X] Cadastro de produtos
- [X] Edição de produtos
- [ ] Exclusão lógica
- [X] Listagem
- [URGENTE] Associação com categorias
- [URGENTE] Associação com fornecedores
- [X] Controle de estoque (quantidade atual/mínima)
- [PARCIAL] Alertas automáticos de estoque baixo

## IV. Gestão de Fornecedores e Compras

- [ ] Cadastro de fornecedores (CNPJ, contato, produtos)
- [ ] Histórico de compras
- [ ] Simulação de pedidos automáticos (baseado na demanda)

## V. Gestão de Vendas e Pagamentos

- [ ] Carrinho de compras (checkout)
- [ ] Pagamento via boleto (simulado)
- [ ] Pagamento via cartão (simulado)
- [ ] Pagamento via Pix (simulado)
- [ ] Emissão de recibo (PDF)
- [ ] Atualização automática de estoque
- [ ] Dashboard com gráficos (vendas, produtos mais vendidos, faturamento)

## VI. Relatórios

- [ ] Relatório de vendas (PDF/CSV)
- [ ] Relatório de estoque baixo
- [ ] Relatório de fornecedores/compras

---

# 🏗️ Arquitetura

Padrão **MVC** com as seguintes camadas:

```
Controller  →  Servlets
Service     →  Regras de negócio
Repository  →  JPA / Hibernate
View        →  JSP
```

---

# 🐳 Executando com Docker (Recomendado)

Esta é a forma mais simples de rodar o projeto, sem precisar instalar Java, Maven ou MySQL localmente.

## Pré-requisitos

- [Docker](https://www.docker.com/) instalado
- [Docker Compose](https://docs.docker.com/compose/) instalado

## 1. Variáveis de Ambiente

Crie um arquivo `.env` na raiz do projeto:

```env
# O que o CONTAINER vai criar ao nascer:
MYSQL_ROOT_PASSWORD=admin_docker
MYSQL_DATABASE=pharmaceutical_system
MYSQL_USER=user_app
MYSQL_PASSWORD=senha_app

# O que o seu JAVA vai usar para se conectar:
DB_USER=${MYSQL_USER}
DB_PASSWORD=${MYSQL_PASSWORD}
DB_URL=jdbc:mysql://db:3306/${MYSQL_DATABASE}

# E-mail
EMAIL_USER=email@gmail.com
EMAIL_PASS=senh ade1 6dig itos
```

> ⚠️ **Nunca comite o arquivo `.env`.** Ele já deve estar no `.gitignore`.

> ℹ️ O host `db` no `DB_URL` refere-se ao nome do serviço definido no `docker-compose.yml`. Dentro da rede Docker, os containers se comunicam pelo nome do serviço — **não use `localhost`**.

## 2. Build e Execução

Antes de subir os containers, gere o `.war` com o Maven:

```bash
mvn clean package -DskipTests
```

Em seguida, suba o ambiente:

```bash
docker-compose up --build
```

A aplicação estará disponível em: **http://localhost:8080**

## 3. Parar o Ambiente

```bash
# Para os containers (dados do banco são preservados)
docker-compose down

# Para os containers E apaga os dados do banco
docker-compose down -v
```

## Como o ambiente Docker está estruturado

O `docker-compose.yml` define dois serviços:

| Serviço | Container | Imagem | Porta |
|---|---|---|---|
| `db` | `erp-db` | `mysql:8.0` | `3307` (host) → `3306` (container) |
| `app` | `erp-app` | Build local (Tomcat 9 + JDK 17) | `8080` |

**Detalhes importantes:**

- O serviço `app` só inicia após o `db` estar saudável (via `healthcheck`).
- As variáveis de ambiente são injetadas via `env_file: .env`, sem hardcode no compose.
- O banco de dados **persiste os dados** entre reinicializações através de um volume nomeado (`mysql_data`).
- O script `script_banco_farmacia.sql` é executado automaticamente na **primeira inicialização** do container do banco, criando as tabelas e dados iniciais.

**Dockerfile da aplicação (`Dockerfile`):**

```dockerfile
FROM tomcat:9.0-jdk17-temurin

# Remove aplicações padrão do Tomcat
RUN rm -rf /usr/local/tomcat/webapps/*

# Copia o WAR gerado pelo Maven
COPY target/erp.war /usr/local/tomcat/webapps/ROOT.war

EXPOSE 8080
CMD ["catalina.sh", "run"]
```

> ℹ️ O WAR é gerado localmente com `mvn clean package` e copiado para dentro da imagem no momento do `docker-compose up --build`.

---

# ⚙️ Executando Localmente (Sem Docker)

## Pré-requisitos

- JDK 13+
- Apache Maven
- Apache Tomcat 9+
- MySQL

## 1. Banco de Dados

Crie o banco e execute o script:

```sql
CREATE DATABASE erp;
```

O projeto possui um script SQL completo (`script_banco_farmacia.sql`) com DDL (estrutura das tabelas) e DML (dados iniciais), porém **ele não está versionado no repositório**. Solicite o arquivo ao responsável pelo projeto e execute-o após criar o banco:

```bash
mysql -u root -p erp < script_banco_farmacia.sql
```

> ℹ️ No ambiente Docker, esse script é executado automaticamente na primeira inicialização do container do banco — não é necessário rodá-lo manualmente nesse caso.

## 2. Variáveis de Ambiente

Crie um arquivo `.env` na raiz:

```env
# O que o CONTAINER vai criar ao nascer:
MYSQL_ROOT_PASSWORD=admin_docker
MYSQL_DATABASE=pharmaceutical_system
MYSQL_USER=user_app
MYSQL_PASSWORD=senha_app

# O que o seu JAVA vai usar para se conectar (localhost para execução local):
DB_USER=${MYSQL_USER}
DB_PASSWORD=${MYSQL_PASSWORD}
DB_URL=jdbc:mysql://localhost:3306/${MYSQL_DATABASE}

# E-mail
EMAIL_USER=email@gmail.com
EMAIL_PASS=senh ade1 6dig itos
```

> ⚠️ Para execução local, o host do banco é `localhost`. Para Docker, use o nome do serviço (`db`).

## 3. Build e Deploy

```bash
mvn clean install
```

Faça o deploy do `.war` gerado no Tomcat (pasta `webapps/`) ou use o plugin Maven configurado no projeto.

---

# 📁 Estrutura do Projeto

```
erp-farmacia/
├── docker-compose.yml              # Orquestração dos containers (app + db)
├── Dockerfile                      # Imagem da aplicação (Tomcat 9 + JDK 17)
├── pom.xml                         # Dependências e build Maven
├── script_banco_farmacia.sql       # Script SQL completo (DDL + DML) — não versionado
└── src/main/
    ├── java/
    │   ├── config/                 # Configurações globais (JPA, ENV, rotas)
    │   │   ├── AppPaths.java
    │   │   ├── EnvConfig.java
    │   │   └── JPAUtil.java
    │   ├── controller/             # Servlets (camada de entrada HTTP)
    │   ├── dao/                    # Acesso ao banco de dados (JPA/Hibernate)
    │   ├── model/                  # Entidades JPA
    │   ├── service/                # Regras de negócio
    │   ├── filter/                 # Filtros HTTP (ex: segurança)
    │   │   └── SecurityFilter.java
    │   ├── util/                   # Utilitários (PDF, hash, XSS, relatórios)
    │   └── db/
    │       └── ConnectionFactory.java
    └── webapp/
        ├── views/                  # JSPs organizados por módulo
        │   ├── auth/               # Login, esqueci senha, nova senha
        │   ├── cashier/            # Checkout e recibo
        │   ├── dashboard/
        │   ├── product/
        │   ├── purchase/
        │   ├── report/
        │   ├── supplier/
        │   └── user/
        ├── css/
        ├── js/
        └── WEB-INF/
            └── web.xml
```

---

# 📌 Observações

- Projeto acadêmico — algumas funcionalidades podem estar **simuladas** (pagamentos, notificações, etc.)
- Este README **não substitui a documentação técnica exigida na entrega**
