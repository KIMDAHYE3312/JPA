package com.ohgiraffers.section05.access.subsection01.field;

import jakarta.persistence.*;


/*
* 필드 접근이 기본값이므로 해당 설정은 제거해도 동일하게 동작한다.
* 또한 필드 레벨과 프로퍼티 레벨에 모두 선언하면 프로퍼티 레벨을 우선으로 사용한다.
* */
@Entity(name="member_section05_subsection01")
@Table(name= "tbl_member_section05_subsection01")
/* 1. 클래스 레벨 : 모든 필드에 대해서 필드 접근 방식을 적용한다. */
@Access(AccessType.FIELD)
public class Member {

    /* 2. 필드 레벨 : 해당 필드에 대해서 필드 접근 방식을 적용한다.*/
    @Id
    @Column(name="member_no")
    @Access(AccessType.FIELD)
    private int memberNo;

    @Column(name="member_id")
    @Access(AccessType.FIELD)
    private String memberId;

    @Column(name="member_pwd")
    @Access(AccessType.FIELD)
    private String memeberPwd;

    @Column(name="nickname")
    @Access(AccessType.FIELD)
    private String nickname;

    public Member() {
    }

    public Member(int memberNo, String memberId, String memeberPwd, String nickname) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.memeberPwd = memeberPwd;
        this.nickname = nickname;
    }

    public int getMemberNo() {

        System.out.println("getMemberNo()를 이용한 access 확인...");
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {

        System.out.println("getMemberId()를 이용한 access 확인...");
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemeberPwd() {

        System.out.println("getMemberPwd()를 이용한 access 확인...");
        return memeberPwd;
    }

    public void setMemeberPwd(String memeberPwd) {
        this.memeberPwd = memeberPwd;
    }

    public String getNickname() {

        System.out.println("getNickname()를 이용한 access 확인...");
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberNo=" + memberNo +
                ", memberId='" + memberId + '\'' +
                ", memeberPwd='" + memeberPwd + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}
