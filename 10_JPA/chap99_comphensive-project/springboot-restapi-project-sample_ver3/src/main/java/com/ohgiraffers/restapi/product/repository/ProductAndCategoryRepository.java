package com.ohgiraffers.restapi.product.repository;

import com.ohgiraffers.restapi.product.entity.ProductAndCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductAndCategoryRepository extends JpaRepository<ProductAndCategory, Integer> {
}
