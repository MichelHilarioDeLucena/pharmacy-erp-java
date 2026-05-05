package service;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.EntityManager;
import dao.ProductDAO;
import model.Product;

public class ProductService {

    private ProductDAO productDAO;
    private EntityManager em;

    public ProductService(EntityManager em) {
        this.em = em;
        this.productDAO = new ProductDAO(em);
    }

    public void cadastrar(Product product) {
        validar(product);
        productDAO.salvar(product);
    }

    public void atualizar(Product product) {
        validar(product);
        productDAO.salvar(product);
    }

    public void desativar(Long id) {
        Product product = productDAO.buscarPorId(id);
        if (product == null)
            throw new IllegalArgumentException("Produto não encontrado.");
        product.setActive(false);
        productDAO.salvar(product);
    }

    public void entradaEstoque(Long id, Integer quantidade) {
        if (quantidade <= 0)
            throw new IllegalArgumentException("Quantidade deve ser maior que zero.");
        Product product = productDAO.buscarPorId(id);
        if (product == null)
            throw new IllegalArgumentException("Produto não encontrado.");
        product.setCurrentStock(product.getCurrentStock() + quantidade);
        productDAO.salvar(product);
    }

    public List<Product> listarTodos() {
        return productDAO.listarTodos();
    }

    public List<Product> listarAtivos() {
        return productDAO.listarAtivos();
    }

    public List<Product> listarEstoqueBaixo() {
        return productDAO.listarEstoqueBaixo();
    }

    public Product buscarPorId(Long id) {
        return productDAO.buscarPorId(id);
    }

    public boolean estoqueAbaixoDoMinimo(Product product) {
        return product.getCurrentStock() <= product.getMinStock();
    }

    private void validar(Product product) {
        if (product.getName() == null || product.getName().trim().isEmpty())
            throw new IllegalArgumentException("Nome do produto é obrigatório.");
        if (product.getCostPrice() == null || product.getCostPrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Preço de custo inválido.");
        if (product.getSalePrice() == null || product.getSalePrice().compareTo(BigDecimal.ZERO) <= 0)
            throw new IllegalArgumentException("Preço de venda inválido.");
        if (product.getMinStock() == null || product.getMinStock() < 0)
            throw new IllegalArgumentException("Estoque mínimo inválido.");
    }
}