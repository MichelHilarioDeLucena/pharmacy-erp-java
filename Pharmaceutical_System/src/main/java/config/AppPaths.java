package config;

public class AppPaths {

	public static final String VIEW_BASE      = "/views";
	public static final String USER_BASE      = VIEW_BASE + "/user";
	public static final String AUTH_BASE      = VIEW_BASE + "/auth";
	public static final String COMMON_BASE    = VIEW_BASE + "/common";
	public static final String DASHBOARD_BASE = VIEW_BASE + "/dashboard";
	public static final String CASHIER_BASE   = VIEW_BASE + "/cashier";

	public static final String LOGIN_PAGE    = "/login.jsp";
	public static final String HOME_PAGE     = "/home.jsp";
	public static final String MENSAGEM_PAGE = COMMON_BASE + "/mensagem.jsp";

	public static final String USUARIO_FORM  = USER_BASE + "/form.jsp";
	public static final String USUARIO_LISTA = USER_BASE + "/listaUsuarios.jsp";

	public static final String PRODUTO_FORM    = VIEW_BASE + "/product/form.jsp";
	public static final String PRODUTO_LISTA   = VIEW_BASE + "/product/lista.jsp";
	public static final String PRODUTO_SERVLET = "/ProductServlet";

	public static final String SUPPLIER_FORM    = VIEW_BASE + "/supplier/form.jsp";
	public static final String SUPPLIER_LISTA   = VIEW_BASE + "/supplier/lista.jsp";
	public static final String SUPPLIER_SERVLET = "/SupplierServlet";

	public static final String PURCHASE_FORM    = VIEW_BASE + "/purchase/form.jsp";
	public static final String PURCHASE_LISTA   = VIEW_BASE + "/purchase/lista.jsp";
	public static final String PURCHASE_SERVLET = "/PurchaseServlet";

	public static final String DASHBOARD_JSP     = DASHBOARD_BASE + "/dashboard.jsp";
	public static final String DASHBOARD_SERVLET = "/DashboardServlet";

	public static final String CHECKOUT_JSP     = CASHIER_BASE + "/checkout.jsp";
	public static final String CHECKOUT_SERVLET = "/CheckoutServlet";

	public static final String REPORT_LISTA   = VIEW_BASE + "/report/lista.jsp";
	public static final String REPORT_SERVLET = "/ReportServlet";

	public static final String USUARIO_LISTAR_ACAO = "/UsuarioServlet?acao=listar";
	public static final String ENTRADA_ESTOQUE     = "/entrada_estoque.jsp";
}