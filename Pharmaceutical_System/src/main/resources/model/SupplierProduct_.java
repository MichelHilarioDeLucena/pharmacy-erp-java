package model;

import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-27T18:57:11.041-0300")
@StaticMetamodel(SupplierProduct.class)
public class SupplierProduct_ {
	public static volatile SingularAttribute<SupplierProduct, Integer> productId;
	public static volatile SingularAttribute<SupplierProduct, Integer> supplierId;
	public static volatile SingularAttribute<SupplierProduct, LocalDate> purchaseDate;
}
