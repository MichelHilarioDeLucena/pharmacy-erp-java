package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;

//Importações oficiais das suas classes do projeto
import config.JPAUtil;
import dao.UsuarioDAO;
import model.Usuario;

@WebServlet("/oauth2callback")
public class OAuthCallbackServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String code = request.getParameter("code");

        // Se o Google não devolver o código, volta para o login
        if (code == null || code.isEmpty()) {
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp?error=OAuth_Failed");
            return;
        }

        try {
            // 1. Faz a requisição para o Google trocando o código pelo Token
            GoogleTokenResponse tokenResponse = new GoogleAuthorizationCodeTokenRequest(
                    new NetHttpTransport(),
                    GsonFactory.getDefaultInstance(),
                    "https://oauth2.googleapis.com/token",
                    System.getenv("GOOGLE_CLIENT_ID"),
                    System.getenv("GOOGLE_CLIENT_SECRET"),
                    code,
                    System.getenv("GOOGLE_REDIRECT_URI")
            ).execute();

            // 2. Desempacota o Token obtido para extrair o perfil do usuário
            GoogleIdToken idToken = tokenResponse.parseIdToken();
            GoogleIdToken.Payload payload = idToken.getPayload();
            
            // Pega o e-mail que o usuário logou no Google
            String emailGoogle = payload.getEmail(); 

            // 3. LOGICA DO BANCO DE DADOS:
            // Aqui você vai usar a estrutura que vocês já têm para fazer o SELECT.
            // Exemplo conceitual:
            // UsuarioDAO dao = new UsuarioDAO();
            // Usuario usuario = dao.buscarPorEmail(emailGoogle);
            // boolean emailExisteNoBanco = (usuario != null);

            // Temporário para teste: assumindo que o e-mail existe se bater com os de teste do banco
            boolean emailExisteNoBanco = emailGoogle.endsWith("@farmacia.com") || emailGoogle.equals("seu_email_pessoal@gmail.com");

            if (emailExisteNoBanco) {
                // Inicia a sessão da mesma forma que o LoginServlet de vocês faz
                HttpSession session = request.getSession();
                session.setAttribute("usuarioLogado", emailGoogle);
                
                // Redireciona o usuário para o painel principal do sistema
                response.sendRedirect(request.getContextPath() + "/views/dashboard/index.jsp");
            } else {
                // Se o e-mail autenticado pelo Google não constar no sistema, barra o acesso
                response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp?error=Usuario_Nao_Cadastrado");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect(request.getContextPath() + "/views/auth/login.jsp?error=Erro_Tecnico_OAuth");
        }
    }
}