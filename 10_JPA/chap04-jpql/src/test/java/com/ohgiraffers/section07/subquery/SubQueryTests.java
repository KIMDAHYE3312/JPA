package com.ohgiraffers.section07.subquery;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class SubQueryTests {

    private static EntityManagerFactory entityManagerFactory;
    private EntityManager entityManager;

    @BeforeAll
    public static void initFactory(){
        entityManagerFactory = Persistence.createEntityManagerFactory("jpatest");
    }

    @BeforeEach
    public void initManager() {
        entityManager = entityManagerFactory.createEntityManager();
    }

    @AfterAll
    public static void closeFactory() {
        entityManagerFactory.close();
    }

    @AfterEach
    public void closeManager() {
        entityManager.close();
    }

    /*
    * JPQL도 SQL처럼 서브쿼리를 지원한다.
    * 하지만 select, from 절에서는 사용할 수 없고 where, having절에서만 사용이 가능하다.
    * */

    @DisplayName("서브쿼리를 이용한 메뉴 조회 테스트")
    @Test
    public void subqueryMenuTest(){

        // given
        String categoryNameParameter = "한식";

        String jpql = "SELECT m FROM menu_section07 m WHERE m.categoryCode "
                + "= (SELECT c.categoryCode FROM category_section07 c WHERE c.categoryName = :categoryName)";
        // when
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                                            .setParameter("categoryName", categoryNameParameter)
                                            .getResultList();
        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
