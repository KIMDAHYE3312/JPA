package com.ohgiraffers.restapi.product.controller;

import com.ohgiraffers.restapi.common.Criteria;
import com.ohgiraffers.restapi.common.PageDTO;
import com.ohgiraffers.restapi.common.PagingResponseDTO;
import com.ohgiraffers.restapi.common.ResponseDTO;
import com.ohgiraffers.restapi.product.dto.ProductAndCategoryDTO;
import com.ohgiraffers.restapi.product.dto.ProductDTO;
import com.ohgiraffers.restapi.product.service.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1")
@Slf4j
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<ResponseDTO> selectProductListWithPaging(
            @RequestParam(name = "offset", defaultValue = "1") String offset){

        log.info("[ProductController] selectProductListWithPaging Start ============ ");
        log.info("[ProductController] selectProductListWithPaging offset : {} ", offset);

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);

        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();
        /* 1. offset의 번호에 맞는 페이지에 뿌려줄 Product들 */
        Page<ProductDTO> productList = productService.selectProductListWithPaging(cri);
        pagingResponseDTO.setData(productList);
        /* 2. PageDTO : 화면에서 페이징 처리에 필요한 정보들 */
        pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) productList.getTotalElements()));

        log.info("[ProductController] selectProductListWithPaging End ============ ");
        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));
    }


    @GetMapping("/products/{productCode}")
    public ResponseEntity<ResponseDTO> selectProductDetail(@PathVariable int productCode){

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상품 상세정보 조회 성공" , productService.selectProduct(productCode)));
    }

    @GetMapping("/products/search")
    public ResponseEntity<ResponseDTO> selectSearchProductList(
            @RequestParam(name = "s", defaultValue = "all") String search){

        return ResponseEntity
                .ok()
                .body(new ResponseDTO(HttpStatus.OK, "조회 성공", productService.selectProductList(search)));
    }

    @GetMapping("/products/meals")
    public ResponseEntity<ResponseDTO> selectProductListAboutMeal() {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공",  productService.selectProductListAboutMeal()));
    }

    @GetMapping("/products/dessert")
    public ResponseEntity<ResponseDTO> selectProductListAboutDessert() {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공",  productService.selectProductListAboutDessert()));
    }

    @GetMapping("/products/beverage")
    public ResponseEntity<ResponseDTO> selectProductListAboutBeverage() {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공",  productService.selectProductListAboutBeverage()));
    }

    @GetMapping("/products-management")
    public ResponseEntity<ResponseDTO> selectProductListWithPagingForAdmin(@RequestParam(name="offset", defaultValue="1") String offset) {
        log.info("[ProductController] selectProductListWithPagingForAdmin : " + offset);

        Criteria cri = new Criteria(Integer.valueOf(offset), 10);
        PagingResponseDTO pagingResponseDTO = new PagingResponseDTO();

        Page<ProductAndCategoryDTO> productAndCategory = productService.selectProductListWithPagingForAdmin(cri);
        pagingResponseDTO.setData(productAndCategory);
        pagingResponseDTO.setPageInfo(new PageDTO(cri, (int) productAndCategory.getTotalElements()));

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "조회 성공", pagingResponseDTO));
    }

    @GetMapping("/products-management/{productCode}")
    public ResponseEntity<ResponseDTO> selectProductDetailForAdmin(@PathVariable int productCode) {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상품 상세정보 조회 성공",  productService.selectProductForAdmin(productCode)));
    }

    @PostMapping("/products")
    public ResponseEntity<ResponseDTO> insertProduct(@ModelAttribute ProductDTO productDTO, MultipartFile productImage){

        return ResponseEntity.ok()
                .body(new ResponseDTO(HttpStatus.OK, "상품 입력 성공"
                                , productService.insertProduct(productDTO, productImage)));
    }

    @PutMapping(value = "/products")
    public ResponseEntity<ResponseDTO> updateProduct(@ModelAttribute ProductDTO productDTO, MultipartFile productImage) {

        return ResponseEntity.ok().body(new ResponseDTO(HttpStatus.OK, "상품 수정 성공",  productService.updateProduct(productDTO, productImage)));
    }
}
