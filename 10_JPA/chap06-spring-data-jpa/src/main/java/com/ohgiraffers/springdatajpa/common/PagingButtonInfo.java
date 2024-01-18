package com.ohgiraffers.springdatajpa.common;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class PagingButtonInfo {

    private int currentPage;
    private int startPage;
    private int endPage;
}
