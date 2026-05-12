package service;

import java.util.List;
import javax.persistence.EntityManager;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.SupplierDAO;
import model.Product;
import model.Purchase;
import model.Supplier;

public class ReportService {

    private ProductDAO  productDAO;
    private PurchaseDAO purchaseDAO;
    private SupplierDAO supplierDAO;

    public ReportService(EntityManager em) {
        this.productDAO  = new ProductDAO(em);
        this.purchaseDAO = new PurchaseDAO(em);
        this.supplierDAO = new SupplierDAO(em);
    }

    // Relatório de estoque baixo
    public List<Product> relatorioEstoqueBaixo() {
        return productDAO.listarEstoqueBaixo();
    }

    // Relatório de todos os produtos
    public List<Product> relatorioEstoqueCompleto() {
        return productDAO.listarAtivos();
    }

    // Relatório de compras
    public List<Purchase> relatorioCompras() {
        return purchaseDAO.listarTodos();
    }

    // Relatório de compras por status
    public List<Purchase> relatorioComprasPorStatus(String status) {
        return purchaseDAO.listarPorStatus(status);
    }

    // Relatório de fornecedores
    public List<Supplier> relatorioFornecedores() {
        return supplierDAO.listarTodos();
    }

    // Relatório de compras por fornecedor
    public List<Purchase> relatorioComprasPorFornecedor(Integer supplierId) {
        Supplier supplier = supplierDAO.buscarPorId(supplierId);
        if (supplier == null)
            throw new IllegalArgumentException("Fornecedor não encontrado.");
        return purchaseDAO.listarPorFornecedor(supplier);
    }
}