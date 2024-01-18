package com.ohgiraffers.springdatajpa.menu.controller;

import com.ohgiraffers.springdatajpa.common.Pagenation;
import com.ohgiraffers.springdatajpa.common.PagingButtonInfo;
import com.ohgiraffers.springdatajpa.menu.dto.CategoryDTO;
import com.ohgiraffers.springdatajpa.menu.dto.MenuDTO;
import com.ohgiraffers.springdatajpa.menu.service.MenuService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/menu")
@Slf4j
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    @GetMapping("/{menuCode}")
    public String findMenuByCode(@PathVariable int menuCode, Model model) {

        MenuDTO menu = menuService.findMenuByCode(menuCode);
        
        model.addAttribute("menu", menu);
        return "menu/detail";
    }

    /* 페이징 처리 전 */
//    @GetMapping("/list")
//    public String findMenuList(Model model) {
//
//        List<MenuDTO> menuList = menuService.findMenuList();
//
//        System.out.println("menuList = " + menuList);
//        model.addAttribute("menuList", menuList);
//
//        return "menu/list";
//    }

    /* 페이징 처리 후 */
    @GetMapping("/list")
    public String findMenuList(@PageableDefault Pageable pageable, Model model){

        /* page -> number, size, sort 파라미터가 Pageable 객체에 담긴다. */
        log.info("[MenuController] findMenuList pageable : {}", pageable);

        Page<MenuDTO> menuList = menuService.findMenuList(pageable);

        log.info("조회한 내용 목록 : {}", menuList.getContent());
        log.info("총 페이지 수 : {}", menuList.getTotalPages());
        log.info("총 메뉴 수 : {}", menuList.getTotalElements());
        log.info("해당 페이지에 표시될 요소 수 : {} ", menuList.getSize());
        log.info("해당 페이지에 실제 요소 수 : {}", menuList.getNumberOfElements());
        log.info("첫 페이지 여부 : {}", menuList.isFirst());
        log.info("정렬 방식 : {}", menuList.getSort());
        log.info("여러 페이지 중 현재 인덱스 : {}", menuList.getNumber());

        PagingButtonInfo paging = Pagenation.getPagingButtonInfo(menuList);

        model.addAttribute("paging", paging);
        model.addAttribute("menuList", menuList);
        return "menu/list";
    }


    @GetMapping("/querymethod")
    public void queryMehtodPage() {}

    @GetMapping("/search")
    public String findByMenuPrice(@RequestParam Integer menuPrice, Model model) {

        List<MenuDTO> menuList = menuService.findByMenuPrice(menuPrice);

        model.addAttribute("menuList", menuList);
        model.addAttribute("menuPrice", menuPrice);

        return "menu/searchResult";
    }

    @GetMapping("/regist")
    public void registPage() {}

    @GetMapping(value="/category", produces = "application/json; charset=utf-8")
    @ResponseBody
    public List<CategoryDTO> findCategoryList() {

        return menuService.findAllCategory();
    }

    @PostMapping("/regist")
    public String registNewMenu(MenuDTO newMenu) {

        System.out.println("newMenu = " + newMenu);

        menuService.registNewMenu(newMenu);

        return "redirect:/menu/list";
    }

    @GetMapping("/modify")
    public void modifyPage(){}

    @PostMapping("/modify")
    public String modifyMenu(MenuDTO modifyMenu) {

        System.out.println("modifyMenu = " + modifyMenu);

        menuService.modifyMenu(modifyMenu);

        return "redirect:/menu/" + modifyMenu.getMenuCode();
    }

    @GetMapping("/delete")
    public void deletePage() {}

    @PostMapping("/delete")
    public String deleteMenu(@RequestParam Integer menuCode) {

        System.out.println("menuCode = " + menuCode);

        menuService.deleteMenu(menuCode);

        return "redirect:/menu/list";
    }
}
