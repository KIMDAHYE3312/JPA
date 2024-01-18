package com.ohgiraffers.section02.named;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class NamedNativeQueryTests {

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
    * NamedNativeQuery
    * 네이티브 SQL도 JPQL처럼 @NamedNativeQuery를 사용해서 정적 SQL을 만들어두고 쓸 수 있다.
    * */

    @DisplayName("NamedNativeQuery를 이용한 조회 테스트")
    @Test
    public void namedNativeQueryTest(){

        // given

        // when
        Query nativeQuery = entityManager.createNamedQuery("Category.menuCountOfCategory");
        List<Object[]> categoryList = nativeQuery.getResultList();

        // then
        Assertions.assertNotNull(categoryList);
        Assertions.assertTrue(entityManager.contains(categoryList.get(0)[0]));
    }
}
