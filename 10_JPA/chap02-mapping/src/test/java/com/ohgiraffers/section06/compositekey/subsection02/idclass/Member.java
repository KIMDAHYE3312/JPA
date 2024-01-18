package com.ohgiraffers.section06.compositekey.subsection02.idclass;

import jakarta.persistence.*;

@Entity(name="member_section06_subsection02")
@Table(name="tbl_member_section06_subsection02")
@IdClass(MemberPK.class)
public class Member {

    @Id
    @Column(name="member_no")
    private int memberNo;

    @Id
    @Column(name="member_id")
    private String memberId;
    @Column(name="phone")
    private String phone;
    @Column(name="address")
    private String address;

    public Member() {
    }

    public Member(int memberNo, String memberId, String phone, String address) {
        this.memberNo = memberNo;
        this.memberId = memberId;
        this.phone = phone;
        this.address = address;
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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Member{" +
                "memberNo=" + memberNo +
                ", memberId='" + memberId + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                '}';
    }
}
