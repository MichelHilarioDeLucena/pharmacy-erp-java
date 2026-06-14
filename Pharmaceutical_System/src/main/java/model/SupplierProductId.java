package model;

import java.io.Serializable;
import java.util.Objects;

public class SupplierProductId implements Serializable {
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Integer productId;
    private Integer supplierId;

    public SupplierProductId() {}
    public SupplierProductId(Integer productId, Integer supplierId) {
        this.productId = productId;
        this.supplierId = supplierId;
    }

    public Integer getProductId() { 
    	return productId; 
    }
    public void setProductId(Integer productId) { 
    	this.productId = productId; 
    }

    public Integer getSupplierId() { 
    	return supplierId; 
    }
    public void setSupplierId(Integer supplierId) { 
    	this.supplierId = supplierId; 
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SupplierProductId that = (SupplierProductId) o;
        return Objects.equals(productId, that.productId) &&
               Objects.equals(supplierId, that.supplierId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productId, supplierId);
    }
}