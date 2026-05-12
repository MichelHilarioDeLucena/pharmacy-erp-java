package service;

import java.util.List;
import javax.persistence.EntityManager;
import dao.SupplierDAO;
import model.Supplier;

public class SupplierService {

    private SupplierDAO supplierDAO;

    public SupplierService(EntityManager em) {
        this.supplierDAO = new SupplierDAO(em);
    }

    public void cadastrar(Supplier supplier) {
        validar(supplier);
        if (supplierDAO.buscarPorCnpj(supplier.getCnpj()) != null)
            throw new IllegalArgumentException("CNPJ já cadastrado.");
        supplierDAO.salvar(supplier);
    }

    public void atualizar(Supplier supplier) {
        validar(supplier);
        supplierDAO.salvar(supplier);
    }

    public void desativar(Integer id) {
        Supplier supplier = supplierDAO.buscarPorId(id);
        if (supplier == null)
            throw new IllegalArgumentException("Fornecedor não encontrado.");
        supplier.setActive(false);
        supplierDAO.salvar(supplier);
    }

    public void reativar(Integer id) {
        Supplier supplier = supplierDAO.buscarPorId(id);
        if (supplier == null)
            throw new IllegalArgumentException("Fornecedor não encontrado.");
        supplier.setActive(true);
        supplierDAO.salvar(supplier);
    }

    public Supplier buscarPorId(Integer id) {
        return supplierDAO.buscarPorId(id);
    }

    public List<Supplier> listarTodos() {
        return supplierDAO.listarTodos();
    }

    public List<Supplier> listarAtivos() {
        return supplierDAO.listarAtivos();
    }

    private void validar(Supplier supplier) {
        if (supplier.getCompanyName() == null || supplier.getCompanyName().trim().isEmpty())
            throw new IllegalArgumentException("Razão social é obrigatória.");
        if (supplier.getCnpj() == null || supplier.getCnpj().trim().isEmpty())
            throw new IllegalArgumentException("CNPJ é obrigatório.");
    }
}