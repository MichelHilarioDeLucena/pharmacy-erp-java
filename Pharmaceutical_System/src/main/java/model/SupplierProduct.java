package model;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "produto_fornecedor")
@IdClass(SupplierProductId.class)
public class SupplierProduct {

    @Id
    @Column(name = "produto_id")
    private Integer productId;

    @Id
    @Column(name = "fornecedor_id")
    private Integer supplierId;

    @Column(name = "data_compra")
    private LocalDate purchaseDate;

    // Opcional: se quiser ter os objetos carregados, use @ManyToOne com @JoinColumn
    // mas cuidado com performance

    public SupplierProduct() {}

    public SupplierProduct(Integer productId, Integer supplierId, LocalDate purchaseDate) {
        this.productId = productId;
        this.supplierId = supplierId;
        this.purchaseDate = purchaseDate;
    }

    // Getters e Setters
    public Integer getProductId() { return productId; }
    public void setProductId(Integer productId) { this.productId = productId; }

    public Integer getSupplierId() { return supplierId; }
    public void setSupplierId(Integer supplierId) { this.supplierId = supplierId; }

    public LocalDate getPurchaseDate() { return purchaseDate; }
    public void setPurchaseDate(LocalDate purchaseDate) { this.purchaseDate = purchaseDate; }
}