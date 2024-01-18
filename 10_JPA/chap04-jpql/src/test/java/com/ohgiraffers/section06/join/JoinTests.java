package com.ohgiraffers.section06.join;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

public class JoinTests {

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
    * 조인의 종류
    * 1. 일반 조인 : 일반적인 SQL조인을 의미(내부조인, 외부조인, 컬렉션 조인, 세타조인)
    * 2. 패치 조인 : JPQL에서 성능 최적화를 위해 제공하는 기능으로 연관된 엔티티나 컬렉션을 한번에 조회할 수 있다.
    *               지연로딩이 아닌 즉시로딩을 수행하며 join fetch 명령어를 사용한다.
    * */

    @DisplayName("내부조인을 이용한 조회 테스트")
    @Test
    public void innserJoinTest(){

        // given
        String jpql = "SELECT m FROM menu_section06 m JOIN m.category c";
        // when
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @DisplayName("외부조인을 이용한 조회 테스트")
    @Test
    public void outerJoinTest(){

        // given
        String jpql = "SELECT m.menuName, c.categoryName FROM menu_section06 m RIGHT JOIN m.category c "
                + "ORDER BY m.category.categoryCode";
        // when
        List<Object[]> menuList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @DisplayName("컬렉션 조인을 이용한 조회 테스트")
    @Test
    public void collectionJoinTest() {
        /* 컬렉션 조인은 의미상 분류된 것으로 컬렉션을 지니고 있는 엔티티를 기준으로 조인하는 것을 말한다. */
        // given
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c LEFT JOIN c.menuList m";

        // when
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();

        // then
        Assertions.assertNotNull(categoryList);
        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @DisplayName("세타조인을 이용한 조회 테스트")
    @Test
    public void crossJoinTest(){

        /* 세타 조인은 조인되는 모든 경우의 수를 다 반환하는 크로스 조인과 같다. */
        // given
        String jpql = "SELECT c.categoryName, m.menuName FROM category_section06 c, menu_section06 m";
        // when
        List<Object[]> categoryList = entityManager.createQuery(jpql, Object[].class).getResultList();
        // then
        Assertions.assertNotNull(categoryList);

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @DisplayName("패치조인을 이용한 조회 테스트")
    @Test
    public void fetchJoinTest(){
        /* 패치조인을 하면 처음 SQL 실행 후 로딩할 때 조인 결과를 다 조회한 뒤에 사용하는 방식이기 때문에
        * 쿼리 실행 횟수가 줄어들게 된다.
        * 대부분의 경우 성능이 향상된다.
        * */
        // given
        String jpql = "SELECT m FROM menu_section06 m JOIN FETCH m.category c";
        // when
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).getResultList();
        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }
















}
