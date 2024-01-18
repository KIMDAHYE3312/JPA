package com.ohgiraffers.section05.groupfunction;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

import java.util.Arrays;
import java.util.List;

public class GroupFunctionTests {

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
     * JPQL의 그룹함수는 COUNT, MAX, MIN, SUM, AVG로 SQL의 그룹함수와 별반 차이가 없다.
     * 단 몇 가지 주의사항이 있다.
     * 1. 그룹함수의 반환타입은 결과값이 정수이면 Long, 실수이면 Double로 반환된다.
     * 2. 값이 없는 상태에서 count를 제외한 그룹함수는 null이 되고 count만 0이 된다.
     *    따라서 반환 값을 담기 위해 선언하는 변수 타입을 기본자료형으로 하게 되면, 조회 결과를 언박싱 할 때 NPE가 발생한다.
     * 3. 그룹함수의 반환 자료형은 Long or Double 형이기 때문에 Having 절에서 그룹함수 결과값과 비교하기 위한 파라미터 타입은 Long or Double로 해야한다.
     *
     * */

    @DisplayName("특정 카테고리의 등록된 메뉴 수 조회")
    @Test
    public void categoryMenuCountTest() {

        // given
        int categoryCodeParameter = 9;
        String jpql = "SELECT COUNT(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";

        // when
        Long countOfMenu = entityManager.createQuery(jpql, Long.class)
                .setParameter("categoryCode", categoryCodeParameter)
                .getSingleResult();
        // then

        Assertions.assertTrue(countOfMenu >= 0);
        System.out.println("countOfMenu = " + countOfMenu);
    }


    @DisplayName("count를 제외한 다른 그룹함수의 조회결과가 없는 경우 테스트")
    @Test
    public void otherGroupFunctionTest(){

        // given
        int categoryCodeParameter = 8;
        String jpql = "SELECT SUM(m.menuPrice) FROM menu_section05 m WHERE m.categoryCode = :categoryCode";
        // when

        // then
        Assertions.assertThrows(NullPointerException.class, () -> {
            /* 반환 값을 담을 변수의 타입을 기본 자료형으로 하는 경우 Wrapper 타입을 언박싱하는 과정에서 NPE이 발생하게 된다. */
            long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode", categoryCodeParameter)
                    .getSingleResult();
        });

        Assertions.assertDoesNotThrow(() -> {
            /* 반환 값을 담는 변수를 Wrapper 타입으로 선언해야 null 값이 반환 되어도 NPE가 발생하지 않는다.*/
            Long sumOfPrice = entityManager.createQuery(jpql, Long.class)
                    .setParameter("categoryCode", categoryCodeParameter)
                    .getSingleResult();
        });

    }
    @DisplayName("groupby절과 having절을 사용한 조회 테스트")
    @Test
    public void groupbyAndHavingTest(){
        // given
        long minPrice = 50000L;     // 그룹함수의 반환 타입은 Long이므로 비교를 위한 파라미터도 Long타입을 사용해야한다.

        String jpql = "SELECT m.categoryCode, SUM(m.menuPrice)"
                + " FROM menu_section05 m"
                + " GROUP BY m.categoryCode"
                + " HAVING SUM(m.menuPrice) >= :minPrice";
        // when
        List<Object[]> sumPriceOfCategoryList = entityManager.createQuery(jpql, Object[].class)
                                                                .setParameter("minPrice", minPrice)
                                                                .getResultList();
        // then
        Assertions.assertNotNull(sumPriceOfCategoryList);
        sumPriceOfCategoryList.forEach(row -> {
            Arrays.stream(row).forEach(System.out::println);
        });

    }

}
