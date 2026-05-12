package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-05T22:46:37.421-0300")
@StaticMetamodel(PurchaseItem.class)
public class PurchaseItem_ {
	public static volatile SingularAttribute<PurchaseItem, Integer> id;
	public static volatile SingularAttribute<PurchaseItem, Purchase> purchase;
	public static volatile SingularAttribute<PurchaseItem, Product> product;
	public static volatile SingularAttribute<PurchaseItem, Integer> quantity;
	public static volatile SingularAttribute<PurchaseItem, BigDecimal> unitPrice;
	public static volatile SingularAttribute<PurchaseItem, BigDecimal> subtotal;
}
