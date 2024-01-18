package com.ohgiraffers.springdatajpa.menu.repository;

import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/*
* JpaRepository를 상속받아서 사용하는 메소등 외의 메소드는 직접 정의한다.
* JpaRepository<엔티티명, 엔티티의 Pk에 해당하는 타입>
* */
public interface MenuRepository extends JpaRepository<Menu, Integer> {

    /* 전달 받은 가격을 초과하는 메뉴의 목록을 가격 순으로 조회하는 메소드*/
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice);

    /* 전달 받은 가격을 초과하는 메뉴의 목록을 가격 순으로 조회하는 메소드*/
    List<Menu> findByMenuPriceGreaterThanOrderByMenuPrice(Integer menuPrice);

    /* 전달 받은 가격을 초과하는 메뉴의 목록을 전달 받는 정렬 기준으로 조회하는 메소드*/
    List<Menu> findByMenuPriceGreaterThan(Integer menuPrice, Sort sort);
}
