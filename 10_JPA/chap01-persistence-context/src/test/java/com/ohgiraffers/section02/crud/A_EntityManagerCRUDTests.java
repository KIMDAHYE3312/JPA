package com.ohgiraffers.section02.crud;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;
import org.junit.jupiter.api.condition.EnabledIf;

public class A_EntityManagerCRUDTests {

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

    @DisplayName("메뉴코드로 메뉴 조회 테스트")
    @Test
    public void selectMenuByCode(){

        // given
        int menuCode = 2;
        // when
        Menu foundMenu = entityManager.find(Menu.class, menuCode);

        // then
        Assertions.assertNotNull(foundMenu);
        Assertions.assertEquals(menuCode, foundMenu.getMenuCode());
        System.out.println("foundMenu = " + foundMenu);
    }

    @DisplayName("새로운 메뉴 추가 테스트")
    @Test
    public void insertNewMenu(){

        // given
        Menu menu = new Menu();
        menu.setMenuName("JPA 테스트용 신규 메뉴");
        menu.setMenuPrice(5000);
        menu.setCategoryCode(4);
        menu.setOrderableStatus("Y");
        // when

        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin(); // 트랜잭션 시작

        try {
            entityManager.persist(menu);
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }

        // then
        Assertions.assertTrue(entityManager.contains(menu));
    }

    @DisplayName("메뉴 이름 수정 테스트")
    @Test
    public void modifyMenuName() {

        // given
        Menu menu = entityManager.find(Menu.class, 28);
        System.out.println("menu = " + menu);

        String menuNameToChange = "갈치스무디";
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            menu.setMenuName(menuNameToChange);
            entityTransaction.commit();
        } catch (Exception e) {

            entityTransaction.rollback();
            e.printStackTrace();
        }

        // then
        Assertions.assertEquals(menuNameToChange, entityManager.find(Menu.class, 28).getMenuName());
    }

    @DisplayName("메뉴 삭제하기 테스트")
    @Test
    public void deleteMenuTest(){

        // given
        Menu menuToRemove = entityManager.find(Menu.class, 28);

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        try {
            entityManager.remove(menuToRemove);  // 삭제요청
            entityTransaction.commit();
        } catch (Exception e) {
            entityTransaction.rollback();
            e.printStackTrace();
        }
        // then
        Menu removedMenu = entityManager.find(Menu.class, 28); // null
        Assertions.assertEquals(null, removedMenu);
    }
}
