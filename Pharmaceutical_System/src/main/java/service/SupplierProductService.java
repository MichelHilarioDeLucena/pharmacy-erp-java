package service;

import javax.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;
import dao.SupplierProductDAO;
import model.SupplierProduct;
import model.SupplierProductId;

public class SupplierProductService {

    private SupplierProductDAO dao;
    private EntityManager em;

    public SupplierProductService(EntityManager em) {
        this.em = em;
        this.dao = new SupplierProductDAO(em);
    }

    public void vincular(Integer productId, Integer supplierId, LocalDate purchaseDate) {
        if (dao.exists(productId, supplierId)) {
            throw new IllegalArgumentException("Produto já vinculado a este fornecedor.");
        }
        SupplierProduct rel = new SupplierProduct(productId, supplierId, purchaseDate);
        dao.salvar(rel);
    }

    public void desvincular(Integer productId, Integer supplierId) {
        SupplierProductId id = new SupplierProductId(productId, supplierId);
        dao.remover(id);
    }

    public List<SupplierProduct> listarPorProduto(Integer productId) {
        return dao.listarPorProduto(productId);
    }

    public List<SupplierProduct> listarPorFornecedor(Integer supplierId) {
        return dao.listarPorFornecedor(supplierId);
    }

    public boolean isVinculado(Integer productId, Integer supplierId) {
        return dao.exists(productId, supplierId);
    }
}