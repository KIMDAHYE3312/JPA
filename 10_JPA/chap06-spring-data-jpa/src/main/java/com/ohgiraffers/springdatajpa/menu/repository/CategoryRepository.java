package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CategoryRepository extends JpaRepository<Category, Integer> {

    /* findAll 메소드를 사용할 수 있지만 여기서는 직접 jpql or native query를 작성*/

    /* jpql 작성 시에는 value만 작성해도 되지만 native query 작성 시에는 반드시 nativeQuery=true 속성을 정의해야한다.*/
    @Query(value="SELECT category_code, category_name, ref_category_code FROM tbl_category ORDER BY category_code ASC", nativeQuery = true)
    List<Category> findAllCategory();
}
