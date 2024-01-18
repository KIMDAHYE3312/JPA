package com.ohgiraffers.section03.bidirection;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

public class BiDirectionTests {

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
    * 데이터베이스의 테이블은 외래키 하나로 양방향 조회가 가능하지만 객체는 서로 다른 다 단방향 참조를 합쳐서 양방향이라고 한다.
    * 따라서 두 개의 연관 관계 중 연관 관계의 주인을 정하고, 주인이 아닌 연관관계를 하나 더 추가하는 방식으로 작성하게 된다.
    * 양방향 연관 관계는 항상 설정하는 것이 아니라 반대 방향으로도 접근하여 객체 그래프 탐색을 할 일이 많은 경우에만 사용한다.
    *
    * 양방향 연관 관계 시 연관 관계의 주인(Owner)라는 이름으로 인해 오해가 있을 수 있다.
    * 비지니스 로직 상 더 중요하다고 연관 관계의 주인이 되는 것이 아니다.
    * 비즈니스 중요도를 배제하고 단순히 외래키 관리자의 의미를 부여해야한다.
    * 연관 관계의 주인은 외래키를 가지고 있는 엔티티이다.
    * */



    @DisplayName("양방향 연관관계 매핑 조회 테스트")
    @Test
    public void bidirectionAssociationTest(){

        // given
        int menuCode = 10;
        int categoryCode = 3;
        // when
        /* 진짜 연관 관계 : 처음 조회 시부터 조인한 결과를 인출해온다.ㅣ*/
        Menu foundMenu = entityManager.find(Menu.class, menuCode);
        /* 가짜 연관 관계 : 해당 엔티티를 조회하고 필요 시 연관 관계 엔티티를 조회하는 쿼리를 다시 실행하게 된다.*/
        Category foundCategory = entityManager.find(Category.class, categoryCode);
        System.out.println("foundCategory = " + foundCategory);
        // then
        Assertions.assertEquals(menuCode, foundMenu.getMenuCode());
        Assertions.assertEquals(categoryCode, foundCategory.getCategoryCode());

        /*
        * 주의사항!
        * toString() 오버라이딩 시 양방향 연관 관계는 재귀호출이 일어나기 때문에 stackOverFlowError가 발생하게된다.
        * 따라서 재귀가 일어나지 않게 하기 위해서는 엔티티의 주인이 아닌 쪽의 toString을 연관객체부분이 출력되지 않도록 해야한다.
        * 특히 자동 완성 및 롬복 라이브러리를 이용하는 경우 해당 문제 발생 가능성이 매우 높아진다.
        * */
        System.out.println("foundMenu = " + foundMenu);
        System.out.println("foundCategory = " + foundCategory);

        System.out.println("foundMenu = " + foundMenu);
        System.out.println("foundCategory = " + foundCategory);

        foundCategory.getMenuList().forEach(System.out::println);
    }

    /*
    * 연관관계의 주인에는 값을 입력하고, 주인이 아닌 곳에는 값을 입력하지 않을 때,
    * 외래키 컬럼이 not null 제약조건이 설정되어 있다면 에러가 발생한다.
    * 양방향 연관관계를 설정하고 흔히 하는 실수이므로 반드시 Menu엔티티에 카테고리 정보를 추가한다.
    * */
    @DisplayName("양방향 연관관계 주인 객체를 이용한 삽입 테스트")
    @Test
    public void bidirectionAssociationInsertTest(){

        // given
        Menu menu = new Menu();
        menu.setMenuCode(124);
        menu.setMenuName("연관관계주인메뉴");
        menu.setMenuPrice(10000);
        menu.setOrderableStatus("Y");

        menu.setCategory(entityManager.find(Category.class, 4));
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(menu);
        entityTransaction.commit();

        // then
        Menu foundMenu = entityManager.find(Menu.class, menu.getMenuCode());
        Assertions.assertEquals(menu.getMenuCode(), foundMenu.getMenuCode());

    }

    @DisplayName("양방향 연관관계 주인이 아닌 객체를 이용한 삽입 테스트")
    @Test
    public void bidirectionAssociationInsertNotOwerTest(){

        // given
        Category category = new Category();
        category.setCategoryCode(1004);
        category.setCategoryName("양방향카테고리");
        category.setRefCategoryCode(null);

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(category);
        entityTransaction.commit();
        // then
        Category foundCategory = entityManager.find(Category.class, category.getCategoryCode());
        Assertions.assertEquals(category.getCategoryCode(), foundCategory.getCategoryCode());
        System.out.println("foundCategory = " + foundCategory);

    }
}
