package com.ohgiraffers.section04.enumtype;

import jakarta.persistence.*;

import java.util.Date;

/*
* @Enumerated 어노테이션은 Enum 타입 매핑을 위해서 사용
* - EnumType.ORDINAL : Enum 타입을 순서로 매핑한다.(default)
* - EnumType.STRING : Enum 타입을 문자열로 매핑한다.
*
* ORDINAL 사용 시의 장점은 데이터베이스에 저장되는 데이터의 크기가 작다는 것이고,
* 단점은 이미 저장된 enum의 순서를 변경할 수 없다는 것이다.
*
* 반대로 STRING 사용 시의 장점은 저장된 enum의 순서가 바뀌거나 enum이 추가되어도 안전하다는 것이고,
* 단점은 데이터베이스에 저장되는 데이터의 크기가 ordinal에 비해 크다는 것이다.
* */
@Entity(name="member_section04")
@Table(name="tbl_member_section04")
public class Member {

    @Id
    @Column(name="member_no")
    private int memberNo;

    @Column(name="member_id")
    private String memberId;

    @Column(name="member_pwd")
    private String memberPwd;

    @Column(name="nickname")
    @Transient              // 테이블 생성 시 무시된다.
    private String nickname;

    @Column(name="phone", columnDefinition = "varchar(200) default '010-0000-0000'")
    private String phone;

    @Column(name="email", unique = true)
    private String email;

    @Column(name="address", nullable = false)
    private String address;

    @Column(name="enroll_date")
    private Date enrollDate;

    @Column(name="member_role")
    @Enumerated(EnumType.STRING)
    private RoleType memberRole;

    @Column(name="status", length = 3)
    private String status;

    public Member() {
    }

    public Member(int memberNo, String memberId, String memberPwd, String nickname, String phone, String email, String address, Date enrollDate, RoleType memberRole, String status) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.memberPwd = memberPwd;
        this.nickname = nickname;
        this.phone = phone;
        this.email = email;
        this.address = address;
        this.enrollDate = enrollDate;
        this.memberRole = memberRole;
        this.status = status;
    }

    public int getMemberNo() {
        return memberNo;
    }

    public void setMemberNo(int memberNo) {
        this.memberNo = memberNo;
    }

    public String getMemberId() {
        return memberId;
    }

    public void setMemberId(String memberId) {
        this.memberId = memberId;
    }

    public String getMemberPwd() {
        return memberPwd;
    }

    public void setMemberPwd(String memberPwd) {
        this.memberPwd = memberPwd;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getEnrollDate() {
        return enrollDate;
    }

    public void setEnrollDate(Date enrollDate) {
        this.enrollDate = enrollDate;
    }

    public RoleType getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(RoleType memberRole) {
        this.memberRole = memberRole;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberNo=" + memberNo +
                ", memberId='" + memberId + '\'' +
                ", memberPwd='" + memberPwd + '\'' +
                ", nickname='" + nickname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", enrollDate=" + enrollDate +
                ", memberRole=" + memberRole +
                ", status='" + status + '\'' +
                '}';
    }
}
