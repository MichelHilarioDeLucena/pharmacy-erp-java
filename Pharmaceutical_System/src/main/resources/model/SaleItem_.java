package model;

import java.math.BigDecimal;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-05T15:55:00.770-0300")
@StaticMetamodel(SaleItem.class)
public class SaleItem_ {
	public static volatile SingularAttribute<SaleItem, Integer> id;
	public static volatile SingularAttribute<SaleItem, Sale> sale;
	public static volatile SingularAttribute<SaleItem, Integer> productId;
	public static volatile SingularAttribute<SaleItem, Integer> quantity;
	public static volatile SingularAttribute<SaleItem, BigDecimal> unitPrice;
	public static volatile SingularAttribute<SaleItem, BigDecimal> subtotal;
}
