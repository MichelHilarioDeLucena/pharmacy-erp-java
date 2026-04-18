💊 ERP Farmácia - Guia Rápido de Execução
Este guia contém o passo a passo exato para rodar o projeto localmente na sua máquina. O sistema utiliza Java (Servlets), Apache Tomcat e MySQL.

⚙️ Passo 1: Pré-requisitos
Certifique-se de ter instalado:

Eclipse IDE (versão para Enterprise Java/Web).

Apache Tomcat (versão 9.0 ou superior) configurado no Eclipse.

MySQL Server e MySQL Workbench.

🗄️ Passo 2: Preparar o Banco de Dados
Abra o MySQL Workbench.

Copie o script SQL localizado em [caminho_se_houver, ex: /sql/init.sql] ou peça o "Script Apocalíptico" para a equipe.

Execute o script. Ele criará o banco erp, as tabelas necessárias e um usuário de teste automático:

Login: admin

Senha: 123

🔐 Passo 3: Variáveis de Ambiente (.env)
Para não vazar senhas, o projeto usa o dotenv. Você precisa criar o seu próprio arquivo de configuração:

Crie um arquivo chamado exatamente .env na pasta raiz do projeto (Pharmaceutical_System).

Cole o código abaixo e ajuste com a sua senha do MySQL local:

Snippet de código
DB_URL=jdbc:mysql://localhost:3306/erp
DB_USER=root
DB_PASSWORD=sua_senha_do_mysql_aqui
🚨 Passo 4: Configuração Crítica no Eclipse
O Eclipse costuma "esquecer" de enviar o driver do MySQL para o Tomcat. Faça isso para evitar o erro No suitable driver found:

No Eclipse, clique com o botão direito no projeto > Properties.

No menu lateral, clique em Deployment Assembly.

Clique no botão Add... (à direita).

Escolha Java Build Path Entries > Next.

Selecione Maven Dependencies > Finish.

Clique em Apply and Close.

🚀 Passo 5: Rodar a Aplicação
Na aba Servers do Eclipse, clique com o botão direito no Tomcat e selecione Clean....

Dê Start no servidor Tomcat.

Abra o navegador e acesse: http://localhost:8080/Pharmaceutical_System/index.html
