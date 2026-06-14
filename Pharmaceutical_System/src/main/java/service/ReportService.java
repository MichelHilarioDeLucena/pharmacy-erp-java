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

    public List<Product> relatorioEstoqueBaixo() {
        return productDAO.listarEstoqueBaixo();
    }

    public List<Product> relatorioEstoqueCompleto() {
        return productDAO.listarAtivos();
    }

    public List<Purchase> relatorioCompras() {
        return purchaseDAO.listarTodos();
    }

    public List<Purchase> relatorioComprasPorStatus(String status) {
        return purchaseDAO.listarPorStatus(status);
    }

    public List<Supplier> relatorioFornecedores() {
        return supplierDAO.listarTodos();
    }

    public List<Purchase> relatorioComprasPorFornecedor(Integer supplierId) {
        Supplier supplier = supplierDAO.buscarPorId(supplierId);
        if (supplier == null)
            throw new IllegalArgumentException("Fornecedor não encontrado.");
        return purchaseDAO.listarPorFornecedor(supplier);
    }
}