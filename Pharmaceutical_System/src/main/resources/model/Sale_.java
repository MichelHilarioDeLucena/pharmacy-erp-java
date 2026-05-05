package model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import javax.annotation.Generated;
import javax.persistence.metamodel.ListAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-05T15:55:00.769-0300")
@StaticMetamodel(Sale.class)
public class Sale_ {
	public static volatile SingularAttribute<Sale, Integer> id;
	public static volatile SingularAttribute<Sale, Usuario> operator;
	public static volatile SingularAttribute<Sale, Integer> clientId;
	public static volatile SingularAttribute<Sale, LocalDateTime> saleDate;
	public static volatile SingularAttribute<Sale, BigDecimal> totalAmount;
	public static volatile SingularAttribute<Sale, String> paymentMethod;
	public static volatile SingularAttribute<Sale, String> paymentStatus;
	public static volatile ListAttribute<Sale, SaleItem> items;
}
