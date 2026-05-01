package config;

/**
 * DEUS DOS DIRETÓRIOS: Centraliza todos os caminhos do ERP. [NOTA] Mantendo em
 * português conforme solicitado.
 */
public class AppPaths {

	// --- PASTAS BASE ---
	public static final String VIEW_BASE = "/views";
	public static final String USER_BASE = VIEW_BASE + "/user";
	public static final String AUTH_BASE = VIEW_BASE + "/auth";
	public static final String COMMON_BASE = VIEW_BASE + "/common";

	// --- PÁGINAS GERAIS ---
	public static final String LOGIN_PAGE = "/login.jsp";
	public static final String HOME_PAGE = "/home.jsp";
	public static final String MENSAGEM_PAGE = COMMON_BASE + "/mensagem.jsp";

	// --- USUÁRIO ---
	public static final String USUARIO_FORM = USER_BASE + "/form.jsp";
	public static final String USUARIO_LISTA = USER_BASE + "/listaUsuarios.jsp";

	// --- MÓDULOS FUTUROS ---
	public static final String PRODUTO_FORM = VIEW_BASE + "/produto/form.jsp";
	public static final String PRODUTO_LISTA = VIEW_BASE + "/produto/lista.jsp";
}