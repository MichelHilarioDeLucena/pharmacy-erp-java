# poo3_project

Para você encarregado de configurar o mysql-workbench, você deve focar em criar a tabela no banco de dados e conectar com o código Java utilizando a tecnologia descrita. Acabei não o fazendo por desconhecimento da IDE e quis passar alguém mais familiarizado com o ambiente

Para você não precisar entender todo o código sozinho, envie para uma IA generativa um zip do branch atual do poo3_project e siga o passo-a-passo de como conectar o MySQL com o Eclipse. Uma vez feita a conexão mínima, poderemos refiná-la.


Aqui embaixo é o READ.ME criado por IA
# 📦 Módulo de Conexão (JPA/MySQL) e Segurança (BCrypt)

Este commit estabelece a base de infraestrutura para a persistência de dados e segurança do sistema. 

## 🛠️ O que foi implementado

* **Mapeamento Objeto-Relacional (JPA):** * Criação da entidade `Usuario` mapeada para a tabela `usuarios` no MySQL.
  * O projeto agora utiliza Container-Managed Persistence (padrão Java EE), delegando o controle do `EntityManager` ao servidor de aplicação via `@PersistenceContext`.
* **Criptografia de Senhas:** * Inclusão da biblioteca `jBCrypt`.
  * Criação do utilitário `HashBCrypt` para gerar e validar o hash das senhas dos usuários, garantindo que não sejam salvas em texto puro no banco.
* **Segurança de Credenciais:** * Inclusão do `dotenv-java`. As credenciais de banco de dados e URL são lidas via variáveis de ambiente, impedindo o vazamento de senhas no repositório.
* **EJB de Inicialização:** * Adicionado o Singleton `TesteConexao` que roda no startup do servidor para injetar um usuário admin de testes no banco de dados e validar a estabilidade da conexão.

## 🧹 Limpeza de Código
Foram removidas classes de conexão JDBC manuais (`ConnectionFactory`) e fábricas de Entity Manager em modo Java SE (`JPAUtil`), alinhando o projeto estritamente à arquitetura Jakarta EE.

## ⚙️ Como testar localmente
1. Certifique-se de ter um banco MySQL rodando.
2. Crie um arquivo `.env` na raiz do projeto (não commitar!) com as variáveis: `DB_URL`, `DB_USER` e `DB_PASSWORD`.
3. Suba a aplicação no servidor. O console deverá exibir as mensagens do `TesteConexao`.
