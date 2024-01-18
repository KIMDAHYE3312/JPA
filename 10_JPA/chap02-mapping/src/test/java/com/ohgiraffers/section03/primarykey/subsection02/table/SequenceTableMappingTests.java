package com.ohgiraffers.section03.primarykey.subsection02.table;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Persistence;
import org.junit.jupiter.api.*;

import java.util.Date;
import java.util.List;

public class SequenceTableMappingTests {

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

    @DisplayName("식별자 매핑 테스트")
    @Test
    public void identityMappingTest(){


        // given
        Member member = new Member();
        member.setMemberId("user01");
        member.setMemberPwd("pass01");
        member.setNickname("홍길동");
        member.setPhone("010-1234-5678");
        member.setAddress("서울시 종로구");
        member.setEnrollDate(new Date());
        member.setMemberRole("ROLE_MEMBER");
        member.setStatus("Y");

        Member member2 = new Member();
        member2.setMemberId("user02");
        member2.setMemberPwd("pass02");
        member2.setNickname("유관순");
        member2.setPhone("010-1111-5678");
        member2.setAddress("서울시 강남구");
        member2.setEnrollDate(new Date());
        member2.setMemberRole("ROLE_MEMBER");
        member2.setStatus("Y");

        // when
        EntityTransaction entityTransaction = entityManager.getTransaction();
        entityTransaction.begin();

        entityManager.persist(member);
        entityManager.persist(member2);

        entityTransaction.commit();

        // then
        String jpql = "SELECT A.memberNo FROM member_section03_subsection02 A";
        List<Integer> memberNoList = entityManager.createQuery(jpql, Integer.class).getResultList();

        memberNoList.forEach(System.out::println);

    }
}
