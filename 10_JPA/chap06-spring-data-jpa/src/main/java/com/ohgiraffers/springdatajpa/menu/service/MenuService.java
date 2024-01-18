package com.ohgiraffers.springdatajpa.menu.service;

import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.entity.Category;
import com.ohgiraffers.springdatajpa.menu.entity.Menu;
import com.ohgiraffers.springdatajpa.menu.repository.CategoryRepository;
import com.ohgiraffers.springdatajpa.menu.repository.MenuRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MenuService {

    private final MenuRepository menuRepository;

    private final CategoryRepository categoryRepository;
    private final ModelMapper modelMapper;

    public MenuService(MenuRepository menuRepository, CategoryRepository categoryRepository,ModelMapper modelMapper) {
        this.menuRepository = menuRepository;
        this.categoryRepository = categoryRepository;
        this.modelMapper = modelMapper;
    }

    public MenuDTO findMenuByCode(int menuCode) {

        /*
        * findById 메소드는 이미 구현되어 있으므로 인터페이스에 따로 정의할 필요가 없다.
        * findById의 반환값은 Optional 타입이다. Optional 타입은 NPE을 방지하기 위한 다양한 기능이 존재한다.
        * 아래의 코드를 적용시키면
        * --> 해당 id로 조회되지 못했을 경우 IllegalArgumentException을 발생시킨다.
        * */
        Menu menu = menuRepository.findById(menuCode).orElseThrow(IllegalArgumentException::new);  // 영속성 컨텍스트에 관리 대상으로 (영속화)
        System.out.println("menu = " + menu);

        // modelMapper를 이용하여 entity 객체를 DTO로 변환해서 반환
        return modelMapper.map(menu, MenuDTO.class);
    }

    /* 페이징 처리 전 */
    public List<MenuDTO> findMenuList() {

        /*
        * findAll 메소드는 이미 구현 되어 있으므로 인터페이스에 따로 정의할 필요가 없다. 전체 목록을 조회할 수 있고 따로 Sort(정렬) 기준을 전달하여 조회할 수도 있다.
        * */
        List<Menu> menuList = menuRepository.findAll(Sort.by("menuCode").descending());

        System.out.println("[MenuService] findMenuList >>  menuList =========  " + menuList);

        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    /* Page -> 페이징 처리 후 */
    public Page<MenuDTO> findMenuList(Pageable pageable) {

        /* page 파라미터가 Pageable의 number값으로 넘어오는데 해당 값이 조회 시에는 인덱스 기준이 되어야 해서 -1 처리가 필요하다. */
        pageable = PageRequest.of(pageable.getPageNumber() <= 0? 0 : pageable.getPageNumber() -1,
                                pageable.getPageSize(),
                                Sort.by("menuCode").descending());

        Page<Menu> menuList = menuRepository.findAll(pageable);
        System.out.println("[MenuService] findMenuList >>  menuList ========= " + menuList);

        return menuList.map(menu -> modelMapper.map(menu, MenuDTO.class));
    }

    public List<MenuDTO> findByMenuPrice(Integer menuPrice) {

//        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice);
//        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThanOrderByMenuPrice(menuPrice);
        List<Menu> menuList = menuRepository.findByMenuPriceGreaterThan(menuPrice, Sort.by("menuPrice").descending());

        System.out.println("[MenuService] findByMenuPrice menuList = " + menuList);

        return menuList.stream().map(menu -> modelMapper.map(menu, MenuDTO.class)).collect(Collectors.toList());
    }

    public List<CategoryDTO> findAllCategory() {

        List<Category> categoryList = categoryRepository.findAllCategory();

        System.out.println("[MenuService] findAllCategory >>> categoryList = " + categoryList);

        return categoryList.stream().map(category -> modelMapper.map(category, CategoryDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public void registNewMenu(MenuDTO newMenu) {

        menuRepository.save(modelMapper.map(newMenu, Menu.class));
    }

    @Transactional
    public void modifyMenu(MenuDTO modifyMenu) {

        Menu foundMenu = menuRepository.findById(modifyMenu.getMenuCode()).orElseThrow(IllegalArgumentException::new); // 영속화
        foundMenu.setMenuName(modifyMenu.getMenuName());
    }

    @Transactional
    public void deleteMenu(Integer menuCode) {

        menuRepository.deleteById(menuCode);
    }
}
