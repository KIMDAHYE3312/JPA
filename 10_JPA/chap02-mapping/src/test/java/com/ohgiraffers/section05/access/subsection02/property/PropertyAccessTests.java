package com.ohgiraffers.section05.access.subsection02.property;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

public class PropertyAccessTests {

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

    @DisplayName("프로퍼티 접근 테스트")
    @Test
    public void propertyAccessTest(){
        // given
        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setMemeberPwd("pass01");
        member.setNickname("홍길동");
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);
        entityTransaction.commit();
        // then
        String jpql = "SELECT a.nickname FROM member_section05_subsection02 a WHERE a.memberNo = 1";
        String registedNickname = entityManager.createQuery(jpql, String.class).getSingleResult();

        Assertions.assertEquals("홍길동님", registedNickname);
    }
}
