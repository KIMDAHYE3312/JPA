package com.ohgiraffers.section05.access.subsection02.property;

import jakarta.persistence.*;



@Entity(name="member_section05_subsection02")
@Table(name= "tbl_member_section05_subsection02")
/* 1. 클래스 레벨 : 모든 필드에 대해서 Getter 접근 방식을 적용한다.
* 주의할 점은 @Id 어노테이션이 필드에 엔티티를 생성하지 못하기 때문에 @Id 어노테이션을 getter메소드 위로 옮겨야한다.
* */
//@Access(AccessType.PROPERTY)
public class Member {


    @Id
    @Column(name="member_no")
    private int memberNo;

    @Column(name="member_id")
    private String memberId;

    @Column(name="member_pwd")
    private String memeberPwd;

    @Column(name="nickname")
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

    @Access(AccessType.PROPERTY)
    public String getNickname() {

        System.out.println("getNickname()를 이용한 access 확인...");
        return nickname + "님";
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
