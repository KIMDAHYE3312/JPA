package com.ohgiraffers.section06.compositekey.subsection02.idclass;

import jakarta.persistence.*;
import org.junit.jupiter.api.*;

public class IdClassTests {

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

    @DisplayName("아이디 클래스 사용한 복합키 테이블 매핑 테스트")
    @Test
    public void idClassMappingTest(){

        // given
        Member member = new Member();
        member.setMemberNo(1);
        member.setMemberId("user01");
        member.setPhone("010-1234-5678");
        member.setAddress("서울시 종로구");
        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();
        entityManager.persist(member);
        entityTransaction.commit();

        // then
        Member foundMember = entityManager.find(Member.class, new MemberPK(1, "user01"));
        Assertions.assertEquals(member, foundMember);
    }
}
