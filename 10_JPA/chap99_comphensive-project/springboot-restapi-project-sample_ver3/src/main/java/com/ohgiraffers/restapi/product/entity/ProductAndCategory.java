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
public class ProductAndCategory {

    @Id
    @Column(name = "PRODUCT_CODE")
    private int productCode;

    @Column(name = "PRODUCT_NAME")
    private String productName;

    @Column(name = "PRODUCT_PRICE")
    private String productPrice;

    @Column(name = "PRODUCT_DESCRIPTION")
    private String productDescription;

    @Column(name = "PRODUCT_ORDERABLE")
    private String productOrderable;

    @ManyToOne
    @JoinColumn(name = "CATEGORY_CODE")
    private Category category;

    @Column(name = "PRODUCT_IMAGE_URL")
    private String productImageUrl;

    @Column(name = "PRODUCT_STOCK")
    private Long productStock;

    public ProductAndCategory() {}

    public ProductAndCategory productCode(int productCode) {
        this.productCode = productCode;
        return this;
    }

    public ProductAndCategory productName(String productName) {
        this.productName = productName;
        return this;
    }

    public ProductAndCategory productPrice(String productPrice) {
        this.productPrice = productPrice;
        return this;
    }

    public ProductAndCategory productDescription(String productDescription) {
        this.productDescription = productDescription;
        return this;
    }

    public ProductAndCategory productOrderable(String productOrderable) {
        this.productOrderable = productOrderable;
        return this;
    }

    public ProductAndCategory category(Category category) {
        this.category = category;
        return this;
    }

    public ProductAndCategory productImageUrl(String productImageUrl) {
        this.productImageUrl = productImageUrl;
        return this;
    }

    public ProductAndCategory productStock(Long productStock) {
        this.productStock = productStock;
        return this;
    }

    public ProductAndCategory build() {
        return new ProductAndCategory(productCode, productName, productPrice, productDescription, productOrderable, category, productImageUrl, productStock);
    }
}
