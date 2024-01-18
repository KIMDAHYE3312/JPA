package com.ohgiraffers.section08.namedquery;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;

public class NamedQueryTests {

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
    * 동적쿼리 : 현재 우리가 사용하는 방식처럼 EntityManager가 제공하는 메소드를 이용하여 JPQL을 문자열로 런타임 시점에 동적으로 쿼리를 만드는 방식
    *           (동적으로 만들어질 쿼리를 위한 조건식이나 반복문은 자바 코드를 이용할 수 있다.)
    * 정적쿼리 : 미리 쿼리를 정의하고 변경하지 않고 사용하는 쿼리를 말하며 미리 정의한 코드는 이름을 부여해서 사용하게 된다. 이를 NamedQuery라고 한다.
    * */

    @DisplayName("동적쿼리를 이용한 조회 테스트")
    @Test
    public void dynamicQueryTest(){
        // given
        String searchName = "한우";
        int searchCategoryCode = 4;
        // when
        StringBuilder jpql = new StringBuilder("SELECT m FROM menu_section08 m ");
        if(searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) {
            jpql.append("WHERE ");
            jpql.append("m.menuName LIKE '%' || :menuName || '%' ");
            jpql.append("AND ");
            jpql.append("m.categoryCode = :categoryCode ");
        } else {

            if(searchName != null && !searchName.isEmpty()) {
                jpql.append("WHERE ");
                jpql.append("m.menuName LIKE '%' || :menuName || '%' ");
            } else if(searchCategoryCode > 0){
                jpql.append("WHERE ");
                jpql.append("m.categoryCode = :categoryCode ");
            }
        }

        TypedQuery<Menu> query = entityManager.createQuery(jpql.toString(), Menu.class);

        if(searchName != null && !searchName.isEmpty() && searchCategoryCode > 0) {
            query.setParameter("menuName", searchName);
            query.setParameter("categoryCode", searchCategoryCode);
        } else {

            if(searchName != null && !searchName.isEmpty()) {
                query.setParameter("menuName", searchName);

            } else if(searchCategoryCode > 0){
                query.setParameter("categoryCode", searchCategoryCode);

            }
        }

        List<Menu> menuList = query.getResultList();
        // then

        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);
    }

    @DisplayName("네임드쿼리를 이용한 조회 테스트")
    @Test
    public void namedQueryTest() {
        // given

        //when
        List<Menu> menuList = entityManager.createNamedQuery("menu_section08.selectMenuList", Menu.class).getResultList();
        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(System.out::println);

    }
}
