package com.ohgiraffers.section03.primarykey.subsection02.table;

import jakarta.persistence.*;

import java.util.Date;

@Entity(name="member_section03_subsection02")
@Table(name="tbl_member_section03_subsection02")
@TableGenerator(
        name="member_seq_table_generator",
        table="tbl_my_sequences",
        pkColumnName = "my_seq_member_no"
)
public class Member {

    @Id
    @Column(name="member_no")
    @GeneratedValue(strategy = GenerationType.TABLE, generator = "member_seq_table_generator")
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
//    @Temporal(TemporalType.TIMESTAMP) // DateTime
//    @Temporal(TemporalType.DATE)    // Date
    @Temporal(TemporalType.TIME)      // Time
    private Date enrollDate;


    @Column(name="member_role")
    private String memberRole;

    @Column(name="status", length = 3)
    private String status;

    public Member() {
    }

    public Member(int memberNo, String memberId, String memberPwd, String nickname, String phone, String email, String address, Date enrollDate, String memberRole, String status) {
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

    public String getMemberRole() {
        return memberRole;
    }

    public void setMemberRole(String memberRole) {
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
                ", memberRole='" + memberRole + '\'' +
                ", status='" + status + '\'' +
                '}';
    }
}
