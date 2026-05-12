package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-05T22:45:37.482-0300")
@StaticMetamodel(Purchase.class)
public class Purchase_ {
	public static volatile SingularAttribute<Purchase, Integer> id;
	public static volatile SingularAttribute<Purchase, Supplier> supplier;
	public static volatile SingularAttribute<Purchase, Usuario> operator;
	public static volatile SingularAttribute<Purchase, LocalDateTime> purchaseDate;
	public static volatile SingularAttribute<Purchase, BigDecimal> totalAmount;
	public static volatile SingularAttribute<Purchase, String> orderStatus;
	public static volatile ListAttribute<Purchase, PurchaseItem> items;
}
