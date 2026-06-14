package service;

import java.util.List;
import javax.persistence.EntityManager;
import dao.ProductDAO;
import dao.PurchaseDAO;
import dao.SaleDAO;
import dao.SupplierDAO;
import model.Product;
import model.Purchase;
import model.Sale;
import model.Supplier;

public class ReportService {

    private ProductDAO  productDAO;
    private PurchaseDAO purchaseDAO;
    private SupplierDAO supplierDAO;
    private SaleDAO saleDAO;
    
    public ReportService(EntityManager em) {
        this.productDAO  = new ProductDAO(em);
        this.purchaseDAO = new PurchaseDAO(em);
        this.supplierDAO = new SupplierDAO(em);
        this.saleDAO     = new SaleDAO(em);
    }

<<<<<<< HEAD
=======
    public List<Sale> relatorioVendas() {
        return saleDAO.listarTodos();
    }
    
    // Relatório de estoque baixo
>>>>>>> main
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