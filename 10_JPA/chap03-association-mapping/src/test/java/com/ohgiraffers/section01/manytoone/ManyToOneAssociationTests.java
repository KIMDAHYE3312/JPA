package com.ohgiraffers.section01.manytoone;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

public class ManyToOneAssociationTests {

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
    * Association Mapping은 Entity클래스간의 관계를 매핑하는 것을 의미한다.
    * 이를 통해 객체를 이용해 데이터베이스의 테이블 간의 관계를 매핑할 수 있다.
    *
    * 다중성에 의한 분류
    * 연관관계가 있는 객체 관계에서 실제로 연관을 가지는 객체의 수에 따라 분류된다.
    * - N:1(ManyToOne) 연관 관계
    * - 1:N(OneToMany) 연관 관계
    * - 1:1(OneToOne) 연관 관계
    * - N:N(ManyToMany) 연관관계
    *
    * 방향에 따른 분류
    * 테이블의 연관 관계는 외래키를 이용하여 양방향 연관 관계의 특징을 가진다.
    * 참조에 의한 객체의 연관 관계는 단방향이다.
    * 객체간의 연관 관계를 양방향으로 만들고 싶을 경우 반대 쪽에서도 필드를 추가해서 참조를 보관하면된다.
    * 하지만 엄밀하게 이는 양방향 관계가 아니라 단방향 관계 2개로 볼 수 있다.
    * - 단방향 연관 관계
    * - 양방향 연관 관계
    * */


    /*
    * ManyToOne은 다수의 엔티티가 하나의 엔티티를 참조하는 상황에서 사용된다.
    * 예를 들어 하나의 카테고리가 여러 개의 메뉴를 가질 수 있는 상황에서 메뉴 엔티티가 카테고리 엔티티를 참조하는 것이다.
    * 이 때 메뉴 엔티티가 Many, 카테고리 엔티티가 One이 된다.
    *
    * 연관관계를 가지는 엔티티를 조회하는 방법은 객체 그래프 탐색(객체 연관 관계를 사용한 조회), 객체지향 쿼리(JPQL) 사용이 있다.
    * */

    @DisplayName("다대일 연관관계 객체 그래프 탐색을 이용한 조회 테스트")
    @Test
    public void manyToOneAssociationTest(){
        // given
        int menuCode = 15;
        // when
        MenuAndCategory foundMenu = entityManager.find(MenuAndCategory.class, menuCode);

        // then
        System.out.println("foundMenu = " + foundMenu);
    }

    /*
    * JPQL은 Java Persistence Query Language의 약자로, 객체 지향 쿼리 언어 중 하나이다.
    * 객체지향 모델에 맞게 작성된 쿼리를 통해, 엔티티 객체를 대상으로 검색, 검색 결과를 토대로 객체를 조작할 수 있다.
    * join문법이 sql과는 다소 차이가 있지만 직접 쿼리를 작성할 수 있는 문법을 제공한다.
    *
    * 주의할 점은 FROM절에 기술할 테이블명에는 반드시 엔티티명이 작성되어야 한다.
    * */
    @DisplayName("다대일 연관관계 객체지향쿼리 사용한 카테고리 이름 조회 테스트")
    @Test
    public void manyToOneAssociationTest2(){

        // given      SELECT categoryName FROM tbl_menu a JOIN tbl_category b on a.categoryCode = b.categoryCode
        String jpql = "SELECT c.categoryName FROM menu_and_category m JOIN m.category c WHERE m.menuCode = 15";
        // when
        String category = entityManager.createQuery(jpql, String.class).getSingleResult();

        // then
        Assertions.assertNotNull(category);
        System.out.println("category = " + category);
    }
    /*
    * commit()을 할 경우 컨텍스트 내에 저장된 영속성 객체를 insert하는 쿼리가 동작 된다.
    *
    * 단, 카테고리가 존재하는 값이 아니므로 부모 테이블(TBL_CATEGORY)에 값이 먼저 들어있어야 그 카테고리를 참조하는 자식 테이블(TBL_MENU)에 데이터를 넣을 수 있다.
    * 이 때 필요한 것은 @ManyToOne 어노테이션에 영속성 전이 설정을 해주는 것이다.
    *
    * 영속성 전이란 특정 엔티티를 영속화할 때 연관된 엔티티도 함께 영속화한다는의미이다.
    * cascade=CascadeType.PERSIST를 설정하면 MenuAndCategory엔티티를 영속화 할 때 Category 엔티티도 함께 영속화 하게 된다.
    * */
    @DisplayName("다대일 연관관계 객체 삽입 테스트")
    @Test
    public void manyToOneAssociationInsertTest(){

        // given
        MenuAndCategory menuAndCategory = new MenuAndCategory();
        menuAndCategory.setMenuCode(99999);
        menuAndCategory.setMenuName("죽방멸치빙수");
        menuAndCategory.setMenuPrice(30000);

        Category category = new Category();
        category.setCategoryCode(33333);
        category.setCategoryName("신규카테고리");
        category.setRefCategoryCode(null);

        menuAndCategory.setCategory(category);
        menuAndCategory.setOrderableStatus("Y");
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(menuAndCategory);
        entityTransaction.commit();
        // then
        MenuAndCategory foundMenuAndCategory = entityManager.find(MenuAndCategory.class, 99999);
        Assertions.assertEquals(99999, foundMenuAndCategory.getMenuCode());
        Assertions.assertEquals(33333, foundMenuAndCategory.getCategory().getCategoryCode());

    }



















}
