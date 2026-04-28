# 💊 ERP Farmácia (Pharmaceutical System)

Um sistema de gestão (ERP) voltado para o controle operacional e administrativo de farmácias. Desenvolvido em Java para a Web, o projeto foca em segurança, persistência de dados eficiente e envio de comunicações automatizadas.

## 🛠️ Tecnologias Utilizadas

Este projeto foi construído utilizando o ecossistema Java EE e gerenciado via Maven. As principais dependências incluem:

* **Java 13** - Linguagem base do projeto.
* **Java EE 8 (Servlets/JSP)** - Estrutura principal da aplicação Web.
* **Hibernate Core (5.6.15)** - Mapeamento Objeto-Relacional (JPA) para interação com o banco de dados.
* **MySQL Connector/J (8.3.0)** - Driver de conexão com o banco de dados MySQL.
* **jBCrypt (0.4)** - Criptografia de senhas para garantir a segurança dos usuários.
* **JavaMail (1.6.2)** - Serviço de mensageria para envio de e-mails (ex: recuperação de senhas).
* **Dotenv-Java (3.0.0)** - Gerenciamento de variáveis de ambiente para proteção de credenciais sensíveis.

## ⚙️ Pré-requisitos

Antes de começar, você precisará ter instalado em sua máquina:
* [JDK 13](https://jdk.java.net/13/) ou superior.
* [Apache Maven](https://maven.apache.org/) para o build e gerenciamento de dependências.
* Servidor de Aplicação Web (recomendado: [Apache Tomcat 9+](https://tomcat.apache.org/)).
* Banco de Dados [MySQL](https://www.mysql.com/).

## 🚀 Instalação e Execução

### 1. Banco de Dados
Crie um banco de dados no MySQL para a aplicação. As tabelas serão geradas automaticamente pelo Hibernate (dependendo da sua configuração no `persistence.xml`), ou você pode rodar os scripts SQL fornecidos no projeto.

### 2. Variáveis de Ambiente
Na raiz do projeto, crie um arquivo chamado `.env` para armazenar as senhas e credenciais (como a senha de aplicativo do e-mail). **Atenção: Não comite este arquivo!**
Exemplo de conteúdo do `.env`:
```env
DB_URL=jdbc:mysql://localhost:3306/erp
DB_USER=root
DB_PASSWORD=SUA_SENHA
EMAIL_USER=seu_email@gmail.com
EMAIL_PASS=sua_senha_de_app_16_digitos
