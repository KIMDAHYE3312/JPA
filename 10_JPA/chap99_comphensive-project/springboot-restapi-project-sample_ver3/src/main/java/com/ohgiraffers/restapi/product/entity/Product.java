package com.ohgiraffers.restapi.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;


@Entity
@Table(name = "TBL_PRODUCT")
@AllArgsConstructor
@Getter
@ToString
public class Product {

    @Id
    @Column(name = "PRODUCT_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int productCode;

    @Column(name = "PRODUCT_NAME", length = 100, nullable = false)
    private String productName;

    @Column(name = "PRODUCT_PRICE", length = 100, nullable = false)
    private String productPrice;

    @Column(name = "PRODUCT_DESCRIPTION", length = 1000, nullable = false)
    private String productDescription;

    @Column(name = "PRODUCT_ORDERABLE", length = 5, nullable = false)
    private String productOrderable;

    @Column(name = "CATEGORY_CODE")
    private int categoryCode;

    @Column(name = "PRODUCT_IMAGE_URL", length = 100, nullable = false)
    private String productImageUrl;

    @Column(name = "PRODUCT_STOCK", nullable = false)
    private Long productStock;

    public Product() {}

    public Product productCode(int productCode) {
        this.productCode = productCode;
        return this;
    }

    public Product productName(String productName) {
        this.productName = productName;
        return this;
    }

    public Product productPrice(String productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public Product productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public Product productOrderable(String productOrderable) {
        this.productOrderable = productOrderable;
        return this;
    }

    public Product categoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    public Product productImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
        return this;
    }

    public Product productStock(Long productStock) {
        this.productStock = productStock;
        return this;
    }

    public Product build() {
        return new Product(productCode, productName, productPrice, productDescription, productOrderable, categoryCode, productImageUrl, productStock);
    }

}
