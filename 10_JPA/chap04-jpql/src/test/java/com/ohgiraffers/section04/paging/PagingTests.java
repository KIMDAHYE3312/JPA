package com.ohgiraffers.section04.paging;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class PagingTests {

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
    * 페이징 처리용 SQL은 DBMS에 따라 각각 문법이 다른 문제점을 안고있다.
    * JPA는 이러한 페이징을 API를 통해 추상화해서 간단하게 처리할 수 있도록 제공해준다.
    * */

    @DisplayName("페이징 API를 이용한 조회 테스트")
    @Test
    public void pagingAPITest(){
        // given
        int offset = 0;                // 조회를 건너 뛸 행 수
        int limit = 5;                  // 조회할 행 수

        String jpql = "SELECT m FROM menu_section04 m ORDER BY m.menuCode DESC";
        // when
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                .setFirstResult(offset)       // 조회를 시작할 위치(0부터 시작)
                .setMaxResults(limit)         // 조회할 데이터의 수
                .getResultList();

        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
}
