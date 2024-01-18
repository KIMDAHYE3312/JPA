package com.ohgiraffers.section05.access.subsection01.field;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

public class FieldAccessTests {

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
    * @Access
    * JPA는 엔티티 객체의 필드에 직접 접근하는 방식과 getter메소드를 이용하는 방식 두 가지로 엔티티 객체에 접근한다.
    * */

    @DisplayName("필드 접근 테스트")
    @Test
    public void fieldAccessTest(){

        // given
        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemeberPwd("pass01");
        member.setNickname("홍길동");
        // when
        entityManager.persist(member);

        // then
        Member foundMember = entityManager.find(Member.class, 1);
        Assertions.assertEquals(member, foundMember);
        System.out.println("foundMember = " + foundMember);

    }

    /*
    * 작성한 메소드가 호출되지 않았기 때문에 필드 접근을 사용한다는 것을 확인
    * 하지만 다른 로직을 처리하거나 값을 검증하는 추가 로직을 수행하는 경우에는 프로퍼티 접근방식을 혼용해서 사용한다.
    * */
}
