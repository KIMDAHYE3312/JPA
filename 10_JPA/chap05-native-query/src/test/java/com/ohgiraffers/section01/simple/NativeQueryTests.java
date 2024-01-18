package com.ohgiraffers.section01.simple;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Stream;

public class NativeQueryTests {

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
    * Native Query란 SQL쿼리를 그대로 사용하는 것을 말한다.
    * 이를 사용하면 ORM의 기능을 이용하면서 SQL 쿼리도 활용할 수 있어서 더욱 강력한 데이터베이스 접근이 가능하다.
    * 따라서 복잡한 쿼리를 작성할 떄나, 특정 데이터베이스에만 사용 가능한 기능을 사용해야 할 때 등에 Native query를 사용한다.
    *
    * 네이티브 쿼리 API는 다음의 3가지가 있다.
    * 1. 결과 타입 정의
    * public Query createNativeQuery(String sqlString, Class resultClass);
    *
    * 2. 결과 타입을 정의할 수 없을 때
    * public Query createNativeQuery(String sqlString);
    *
    * 3. 결과 매핑 사용
    * public Query createNativeQuery(String sqlString, String resultSetMapping);
    * */

    /*
    * 1. 결과 타입 정의
    *
    * 유의할 점은 모든 컬럼값을 매핑하는 경우에만 타입을 특정할 수 있다.
    * 만약 일부 컬럼만 조회하고 싶은 경우 Object[] 또는 스칼라 값을 별도로 담을 클래스를 정의해서 사용해야 한다.
    * */
    @DisplayName("결과 타입을 정의한 네이티브 쿼리 사용 테스트")
    @Test
    public void resultTypeNativeQueryTest(){

        // given
        int menuCodeParameter = 15;
        // when
        /* DBMS의 고유한 SQL문법을 작성한다. 위치 기반 파라미터로만 사용이 가능하다. */
        String query = "SELECT menu_code, menu_name, menu_price, category_code, orderable_status FROM tbl_menu WHERE menu_code = ?";

        /* 일부 컬럼만 조회하는 것은 불가능하다. */
        //String query = "SELECT menu_code, menu_name, menu_price FROM tbl_menu WHERE menu_code = ?";

        Query nativeQuery = entityManager.createNativeQuery(query, Menu.class).setParameter(1, menuCodeParameter);
        Menu foundMenu = (Menu) nativeQuery.getSingleResult();

        // then
        Assertions.assertNotNull(foundMenu);
        /* 영속성 컨텍스트에서 관리하는 객체임을 알 수 있다.*/
        Assertions.assertTrue(entityManager.contains(foundMenu));
        System.out.println("foundMenu = " + foundMenu);
    }

    /* 2. 결과 타입을 정의할 수 없는 경우 */
    @DisplayName("결과 타입을 정의할 수 없는 경우 조회 테스트")
    @Test
    public void resultTypeNotDeclareTest(){

        // given
        String query = "SELECT menu_name, menu_price FROM tbl_menu";
        // when
        List<Object[]> menuList = entityManager.createNativeQuery(query).getResultList();

        //List<Object[]> menuList = entityManager.createNativeQuery(query, Object[].class).getResultList();
        // then
        Assertions.assertNotNull(menuList);
        menuList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });

    }

    /* 자동 결과 매핑 */
    @DisplayName("자동 결과 매핑을 사용한 조회 테스트")
    @Test
    public void autoResultMappingTest(){

        // given
        String query = "SELECT " +
                "             a.category_code, a.category_name, a.ref_category_code, COALESCE(v.menu_count, 0) menu_count " +
                "         FROM tbl_category a " +
                "         LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code " +
                "                      FROM tbl_menu b " +
                "                     GROUP BY b.category_code) v ON (a.category_code = v.category_code) " +
                "       ORDER BY 1";
        // when
        Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountAutoMapping");
        List<Object[]> categoryList = nativeQuery.getResultList();
        // then
        Assertions.assertTrue(entityManager.contains(categoryList.get(0)[0]));
        Assertions.assertNotNull(categoryList);

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }

    @DisplayName("수동 결과 매핑을 사용한 조회 테스트")
    @Test
    public void manualResultMappingTest(){

        // given
        String query = "SELECT " +
                "             a.category_code, a.category_name, a.ref_category_code, COALESCE(v.menu_count, 0) menu_count " +
                "         FROM tbl_category a " +
                "         LEFT JOIN (SELECT COUNT(*) AS menu_count, b.category_code " +
                "                      FROM tbl_menu b " +
                "                     GROUP BY b.category_code) v ON (a.category_code = v.category_code) " +
                "       ORDER BY 1";
        // when
        Query nativeQuery = entityManager.createNativeQuery(query, "categoryCountManualMapping");
        List<Object[]> categoryList = nativeQuery.getResultList();
        // then
        Assertions.assertTrue(entityManager.contains(categoryList.get(0)[0]));
        Assertions.assertNotNull(categoryList);

        categoryList.forEach(row -> {
            Stream.of(row).forEach(col -> System.out.print(col + " "));
            System.out.println();
        });
    }
}
