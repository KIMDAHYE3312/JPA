package com.ohgiraffers.restapi.product.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Entity
@Table(name = "TBL_CATEGORY")
@AllArgsConstructor
@Getter
@ToString
public class Category {

    @Id
    @Column(name = "CATEGORY_CODE", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int categoryCode;

    @Column(name = "CATEGORY_NAME", length = 50, nullable = false)
    private String categoryName;

    protected Category() {}

    public Category categoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
        return this;
    }

    public Category categoryName(String categoryName) {
        this.categoryName = categoryName;
        return this;
    }

    public Category build() {
        return new Category(categoryCode, categoryName);
    }

}
