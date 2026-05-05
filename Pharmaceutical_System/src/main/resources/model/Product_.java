package model;

import java.math.BigDecimal;
import java.time.LocalDate;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="Dali", date="2026-05-05T15:55:00.768-0300")
@StaticMetamodel(Product.class)
public class Product_ {
	public static volatile SingularAttribute<Product, Long> id;
	public static volatile SingularAttribute<Product, String> barcode;
	public static volatile SingularAttribute<Product, String> name;
	public static volatile SingularAttribute<Product, String> description;
	public static volatile SingularAttribute<Product, BigDecimal> costPrice;
	public static volatile SingularAttribute<Product, BigDecimal> salePrice;
	public static volatile SingularAttribute<Product, Integer> currentStock;
	public static volatile SingularAttribute<Product, Integer> minStock;
	public static volatile SingularAttribute<Product, Integer> categoryId;
	public static volatile SingularAttribute<Product, Integer> supplierId;
	public static volatile SingularAttribute<Product, Boolean> active;
	public static volatile SingularAttribute<Product, LocalDate> expiryDate;
}
