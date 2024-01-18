package com.ohgiraffers.section03.persistencecontext;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class A_EntityLifeCycleTests {

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

    /*
    * 영속성 컨텍스트는 엔티티 매니저가 엔티티 객체를 저장하는 공간으로 엔티티 객체를 보관하고 관리한다.
    * 엔티티 매니저가 생성될 때 하나의 영속성 컨텍스트가 만들어진다.
    *
    * 엔티티의 생명주기
    * 비영속, 영속, 준영속, 삭제상태
    * */

    @DisplayName("비영속 테스트")
    @Test
    public void nonpersistenceTest(){

        // given
        Menu foundMenu = entityManager.find(Menu.class, 11);  // 영속상태

        Menu newMenu = new Menu(); // 비영속상태 : 객체만 생성하면 영속성 컨텍스트나 데이터베이스와 관련없다.
        newMenu.setMenuCode(foundMenu.getMenuCode());
        newMenu.setMenuName(foundMenu.getMenuName());
        newMenu.setMenuPrice(foundMenu.getMenuPrice());
        newMenu.setCategoryCode(foundMenu.getCategoryCode());
        newMenu.setOrderableStatus(foundMenu.getOrderableStatus());
        // when
        boolean isTrue = (foundMenu == newMenu);
        // then
        Assertions.assertFalse(isTrue);

    }

    @DisplayName("영속성 연속 조회 테스트")
    @Test
    public void persistenceTest(){
        /*
        * 엔티티 매니저가 영속성 컨텍스트에 엔티티 객체를 저장(persist)하면 영속성 컨텍스트가 엔티티 객체를 관리하게 되고 이를 영속 상태라고 한다.
        * find(), JPQL을 사용한 조회도 영속 상태가 된다.
        * */
        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);
        Menu foundMenu2 = entityManager.find(Menu.class, 11);

        // when
        boolean isTrue = (foundMenu1 == foundMenu2);
        // then
        Assertions.assertTrue(isTrue);

    }

    @DisplayName("영속성 객체 추가 테스트")
    @Test
    public void insertPersisteTest() {

        // given
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(menuToRegist);

        Menu foundMenu = entityManager.find(Menu.class, 500);
        boolean isTrue = (menuToRegist == foundMenu);
        // then
        Assertions.assertTrue(isTrue);

    }

    @DisplayName("영속성 객체 추가 값 변경 테스트")
    @Test
    public void persisteRegistAndModifyTest(){

        // given
        Menu menuToRegist = new Menu();
        menuToRegist.setMenuCode(500);
        menuToRegist.setMenuName("수박죽");
        menuToRegist.setMenuPrice(10000);
        menuToRegist.setCategoryCode(1);
        menuToRegist.setOrderableStatus("Y");
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        Menu foundMenu = null;
        try {
            entityManager.persist(menuToRegist);
            menuToRegist.setMenuName("메론죽");
            entityManager.flush();
            foundMenu = entityManager.find(Menu.class, 500);
            System.out.println("foundMenu = " + foundMenu);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        // then
        Assertions.assertEquals("메론죽", foundMenu.getMenuName());
    }

    @DisplayName("준영속성 detach 테스트")
    @Test
    public void detachPersistenceTest(){

        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);   // 영속상태
        Menu foundMenu2 = entityManager.find(Menu.class, 12);  // 영속상태
        // when
        /*
        * 영속성 컨텍스트가 관리하던 엔티티 객체를 관리하지 않는 상태가 된다면 준영속상태라고 한다.
        * 그 중 detach는 특정 엔티티만 준영속 상태로 만든다.
        * */
        entityManager.detach(foundMenu2); // 준영속상태

        foundMenu1.setMenuPrice(50000);
        foundMenu2.setMenuPrice(50000);
        // then
        Assertions.assertEquals(50000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertEquals(50000, entityManager.find(Menu.class, 12).getMenuPrice());
    }

    @DisplayName("준영속성 clear 테스트")
    @Test
    public void detachPersistenceClearTest(){

        // given
        Menu foundMenu1 = entityManager.find(Menu.class, 11);   // 영속상태
        Menu foundMenu2 = entityManager.find(Menu.class, 12);  // 영속상태
        // when
        /* clear()는 영속성 컨텍스트를 초기화한다. */
        entityManager.clear();

        foundMenu1.setMenuPrice(5000);
        foundMenu2.setMenuPrice(5000);
        // then
        Assertions.assertEquals(9000, entityManager.find(Menu.class, 11).getMenuPrice());
        Assertions.assertEquals(10000, entityManager.find(Menu.class, 12).getMenuPrice());

    }

    @DisplayName("삭제 remove 테스트")
    @Test
    public void removeTest(){

        /*
        * remove : 엔티티를 영속성 컨텍스트 및 데이터베이스에서 삭제한다.
        * 단, 트랜잭션을 제어하지 않으면 영구 반영되지는 않는다.
        * 트랜잭션을 커밋하는 순간 영속성 컨텍스트에서 관리하는 엔티티 객체가 데이터베이스에 반영되게 된다.(이를 flush라 한다.)
        * flush : 영속성 컨텍스트의 변경내용을 데이터베이스에 동기화하는 작업(등록, 수정, 삭제한 엔티티를 데이터베이스에 반영)
        * */
        // given
        Menu foundMenu = entityManager.find(Menu.class, 2);

        // when
        entityManager.remove(foundMenu);
        Menu refoundMenu = entityManager.find(Menu.class, 2);
        // then
        Assertions.assertEquals(2, foundMenu.getMenuCode());
        Assertions.assertEquals(null, refoundMenu);
    }

    /*
    * 병합(merge) : 파라미터로 넘어온 준영속 엔티티 객체의 식별자 값으로 1차 캐시에서 엔티티 객체를 조회한다.
    * 만약 1차 캐시에 엔티티가 없으면 데이터베이스에서 엔티티를 조회하고 1차 캐시에 저장한다.
    * 조회한 영속 엔티티 객체에 준영속 상태의 엔티티 객체의 값을 병합한 뒤 영속 엔티티 객체를 반환한다.
    * 혹은 조회할 수 없는 데이터의 경우 새로 생성해서 병합한다.(save or update)
    * */
    @DisplayName("병합 merge 수정 테스트")
    @Test
    public void mergeTest(){

        // given
        Menu menuToDetach = entityManager.find(Menu.class, 2);   // 영속상태

        entityManager.detach(menuToDetach);                                   // 준영속상태
        // when

        menuToDetach.setMenuName("수박죽");
        System.out.println("menuToDetach = " + menuToDetach);
        Menu refoundMenu = entityManager.find(Menu.class, 2); // 영속상태
        System.out.println("refoundMenu = " + refoundMenu);

        System.out.println("menuToDetach = " + menuToDetach.hashCode());
        System.out.println("refoundMenu = " + refoundMenu.hashCode());

        entityManager.merge(menuToDetach);

        // then
        Menu mergedMenu = entityManager.find(Menu.class, 2);
        Assertions.assertEquals("수박죽", mergedMenu.getMenuName());
    }

    @DisplayName("병합 merge 삽입 테스트")
    @Test
    public void mergeInsertTest(){

        // given
        Menu menuToDetach = entityManager.find(Menu.class, 2);
        entityManager.detach(menuToDetach);
        // when
        menuToDetach.setMenuCode(999);
        menuToDetach.setMenuName("호박죽");

        entityManager.merge(menuToDetach);
        // then
        Menu mergedMenu = entityManager.find(Menu.class, 999);
        Assertions.assertEquals("호박죽", mergedMenu.getMenuName());
    }
}
