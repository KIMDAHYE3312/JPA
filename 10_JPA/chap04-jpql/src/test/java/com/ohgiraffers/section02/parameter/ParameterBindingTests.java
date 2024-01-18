package com.ohgiraffers.section02.parameter;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class ParameterBindingTests {

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
    * 파라미터 바인딩하는 방법
    * 1. 이름 기준 파라미터(named parameter)
    *    ':' 다음에 이름 기준 파라미터를 지정한다.
    * 2. 위치 기준 파라미터(positional parameters)
    *    '?' 다음에 값을 주고 위치 값은 1부터 시작한다.
    * */
    @DisplayName("이름 기준 파라미터 바인딩 메뉴 목록 조회 테스트")
    @Test
    public void namedParameterBindTest(){

        // given
        String menuNameParameter = "한우딸기국밥";
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = :menuName";
        // when
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class)
                                                .setParameter("menuName", menuNameParameter)
                                                .getResultList();
        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }

    @DisplayName("위치 기준 파라미터 바인딩 메뉴 목록 조회 테스트")
    @Test
    public void positionParameterBindTest(){

        // given
        String menuNameParameter = "한우딸기국밥";
        String jpql = "SELECT m FROM menu_section02 m WHERE m.menuName = ?1";
        // when
        List<Menu> menuList = entityManager.createQuery(jpql, Menu.class).setParameter(1, menuNameParameter).getResultList();
        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }














}
