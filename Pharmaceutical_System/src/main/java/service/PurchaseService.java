package service;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;

import config.JPAUtil;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.SupplierDAO;
import dao.SupplierProductDAO;
import model.Product;
import model.Purchase;
import model.PurchaseItem;
import model.Supplier;
import model.Usuario;
import model.SupplierProduct;


public class PurchaseService {

    private PurchaseDAO purchaseDAO;
    private ProductDAO productDAO;
    private SupplierDAO supplierDAO;

    public PurchaseService(EntityManager em) {
        this.purchaseDAO = new PurchaseDAO(em);
        this.productDAO  = new ProductDAO(em);
        this.supplierDAO = new SupplierDAO(em);
    }

    public void registrar(Purchase purchase) {
        if (purchase.getSupplier() == null)
            throw new IllegalArgumentException("Fornecedor é obrigatório.");
        if (purchase.getItems() == null || purchase.getItems().isEmpty())
            throw new IllegalArgumentException("A compra deve ter ao menos um item.");
        purchaseDAO.salvar(purchase);
    }

    public void confirmar(Integer purchaseId) {
        Purchase purchase = purchaseDAO.buscarPorId(purchaseId);
        if (purchase == null)
            throw new IllegalArgumentException("Compra não encontrada.");
        if (!"PENDENTE".equals(purchase.getOrderStatus()))
            throw new IllegalStateException("Apenas compras pendentes podem ser confirmadas.");

        for (PurchaseItem item : purchase.getItems()) {
            Product product = item.getProduct();
            product.setCurrentStock(product.getCurrentStock() + item.getQuantity());
            productDAO.salvar(product);
        }

        purchase.setOrderStatus("CONFIRMADO");
        purchaseDAO.salvar(purchase);
    }

    public void cancelar(Integer purchaseId) {
        Purchase purchase = purchaseDAO.buscarPorId(purchaseId);
        if (purchase == null)
            throw new IllegalArgumentException("Compra não encontrada.");
        if (!"PENDENTE".equals(purchase.getOrderStatus()))
            throw new IllegalStateException("Apenas compras pendentes podem ser canceladas.");
        purchase.setOrderStatus("CANCELADO");
        purchaseDAO.salvar(purchase);
    }

    public int gerarPedidosAutomaticos(Usuario operador) {
        List<Product> produtosBaixos = productDAO.listarEstoqueBaixo();
        int pedidosGerados = 0;
        EntityManager em = JPAUtil.getEntityManager();
        SupplierProductDAO spDAO = new SupplierProductDAO(em); 
        for (Product product : produtosBaixos) {
            List<SupplierProduct> vinculos = spDAO.listarPorProduto(product.getId().intValue());
            if (vinculos.isEmpty()) continue;

            vinculos.sort((a, b) -> b.getPurchaseDate().compareTo(a.getPurchaseDate()));
            SupplierProduct ultimoVinculo = vinculos.get(0);
            Supplier supplier = supplierDAO.buscarPorId(ultimoVinculo.getSupplierId());
            if (supplier == null || !supplier.getActive()) continue;

            int quantidadeAPedir = (product.getMinStock() * 2) - product.getCurrentStock();
            if (quantidadeAPedir <= 0) continue;

            PurchaseItem item = new PurchaseItem();
            item.setProduct(product);
            item.setQuantity(quantidadeAPedir);
            item.setUnitPrice(product.getCostPrice());
            item.setSubtotal(product.getCostPrice().multiply(new BigDecimal(quantidadeAPedir)));

            Purchase purchase = new Purchase();
            purchase.setSupplier(supplier);
            purchase.setOperator(operador);
            purchase.setOrderStatus("PENDENTE");
            purchase.addItem(item);

            purchaseDAO.salvar(purchase);
            pedidosGerados++;
        }
        return pedidosGerados;
    }

    public List<Purchase> listarTodos() {
        return purchaseDAO.listarTodos();
    }

    public List<Purchase> listarPorStatus(String status) {
        return purchaseDAO.listarPorStatus(status);
    }

    public List<Purchase> listarPorFornecedor(Integer supplierId) {
        Supplier supplier = supplierDAO.buscarPorId(supplierId);
        if (supplier == null)
            throw new IllegalArgumentException("Fornecedor não encontrado.");
        return purchaseDAO.listarPorFornecedor(supplier);
    }

    public Purchase buscarPorId(Integer id) {
        return purchaseDAO.buscarPorId(id);
    }
}