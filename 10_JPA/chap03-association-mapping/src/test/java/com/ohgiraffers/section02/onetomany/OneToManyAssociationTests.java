package com.ohgiraffers.section02.onetomany;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.List;

public class OneToManyAssociationTests {

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

    @DisplayName("일대다 연관관계 객체 그래프 탐색을 이용한 조회 테스트")
    @Test
    public void oneToManyAssociationSearchTest(){

        // given
        int categoryCode = 6;
        // when
        // 일대다 연관관계의 경우 해당 테이블만 조회하고 연관된 메뉴 테이블을 아직 조회하지 않는다.
        CategoryAndMenu categoryAndMenu = entityManager.find(CategoryAndMenu.class, categoryCode);

        // then
        Assertions.assertNotNull(categoryAndMenu);

        // 출력문을 작성하면 사용하는 경우 연관 테이블을 조회해오는 동작이 일어난다.
        System.out.println("categoryAndMenu = " + categoryAndMenu);
    }

    @DisplayName("일대다 연관관계 객체 삽입 테스트")
    @Test
    public void oneToManyAssociationInsertTest(){

        // given
        CategoryAndMenu categoryAndMenu = new CategoryAndMenu();
        categoryAndMenu.setCategoryCode(8888);
        categoryAndMenu.setCategoryName("일대다추가카테고리");
        categoryAndMenu.setRefCategoryCode(null);

        List<Menu> menuList = new ArrayList<>();
        Menu menu = new Menu();
        menu.setMenuCode(7777);
        menu.setMenuName("일대다아이스크림");
        menu.setMenuPrice(50000);
        menu.setOrderableStatus("Y");

        menu.setCategoryCode(categoryAndMenu.getCategoryCode());
        menuList.add(menu);
        Menu menu2 = new Menu();
        menu2.setMenuCode(7778);
        menu2.setMenuName("일대다아이스크림2");
        menu2.setMenuPrice(50000);
        menu2.setOrderableStatus("Y");

        menu2.setCategoryCode(categoryAndMenu.getCategoryCode());

        menuList.add(menu2);

        categoryAndMenu.setMenuList(menuList);
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(categoryAndMenu);
        entityTransaction.commit();

        // then
        CategoryAndMenu foundCategoryAndMenu = entityManager.find(CategoryAndMenu.class, 8888);
        System.out.println("foundCategoryAndMenu = " + foundCategoryAndMenu);
    }
}
