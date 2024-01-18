package com.ohgiraffers.restapi.product.service;

import com.ohgiraffers.restapi.common.Criteria;
import com.ohgiraffers.restapi.product.dto.ProductAndCategoryDTO;
import com.ohgiraffers.restapi.product.dto.ProductDTO;
import com.ohgiraffers.restapi.product.entity.Product;
import com.ohgiraffers.restapi.product.entity.ProductAndCategory;
import com.ohgiraffers.restapi.product.repository.ProductAndCategoryRepository;
import com.ohgiraffers.restapi.product.repository.ProductRepository;
import com.ohgiraffers.restapi.util.FileUploadUtils;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    private final ProductAndCategoryRepository productAndCategoryRepository;

    private final ModelMapper modelMapper;

    /* 이미지 저장 할 위치 및 응답 할 이미지 주소 */
    @Value("${image.image-dir}")
    private String IMAGE_DIR;

    @Value("${image.image-url}")
    private String IMAGE_URL;

    public ProductService(ProductRepository productRepository
            , ProductAndCategoryRepository productAndCategoryRepository
            , ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.productAndCategoryRepository = productAndCategoryRepository;
        this.modelMapper = modelMapper;
    }

    public Page<ProductDTO> selectProductListWithPaging(Criteria cri) {

        log.info("[ProductService] selectProductListWithPaging Start ===================");
        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("productCode").descending());

        Page<Product> result = productRepository.findByProductOrderable("Y", paging);

        /* Entity -> DTO로 변환 */
        Page<ProductDTO> productList = result.map(product -> modelMapper.map(product, ProductDTO.class));

        for(int i = 0; i < productList.toList().size(); i++){
            productList.toList().get(i).setProductImageUrl(IMAGE_URL + productList.toList().get(i).getProductImageUrl());
        }

        log.info("[ProductService] selectProductListWithPaging End ===================");
        return productList;
    }

    public ProductDTO selectProduct(int productCode) {
        log.info("[ProductService] selectProduct Start ===================");

        Product product = productRepository.findById(productCode).get(); // optional 타입은 get()메소드를 이용해서 꺼내온다(까먹지말자)
        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);
        productDTO.setProductImageUrl(IMAGE_URL + productDTO.getProductImageUrl());
        log.info("[ProductService] selectProduct End ===================");

        return productDTO;
    }

    public ProductDTO selectProductForAdmin(int productCode) {
        log.info("[ProductService] selectProductForAdmin Start ===================================");

        Product product = productRepository.findById(productCode).get();
        product = product.productImageUrl(IMAGE_URL + product.getProductImageUrl()).build();

        log.info("[ProductService] selectProductForAdmin End ===================================");

        ProductDTO productDTO = modelMapper.map(product, ProductDTO.class);

        return productDTO;
    }

    public List<ProductDTO> selectProductList(String search) {

        log.info("[ProductService] selectProductList Start ===================");
        log.info("[ProductService] search : {} ", search);

        List<Product> productListWithSearchValue = productRepository.findByProductNameContaining(search);

        List<ProductDTO> productDTOList = productListWithSearchValue.stream()
                                                .map(product -> modelMapper.map(product, ProductDTO.class))
                                                .collect(Collectors.toList());

        for(int i = 0; i < productDTOList.size(); i++){
            productDTOList.get(i).setProductImageUrl(IMAGE_URL + productDTOList.get(i).getProductImageUrl());
        }
        log.info("[ProductService] selectProductList End ===================");

        return productDTOList;
    }

    @Transactional
    public String insertProduct(ProductDTO productDTO, MultipartFile productImage) {
        log.info("[ProductService] insertProduct Start ===================");
        log.info("[ProductService] productDTO : " + productDTO);

        String imageName = UUID.randomUUID().toString().replace("-", "");
        String replaceFileName = null;
        int result = 0; // 결과에 따른 값을 구분하기위한 용도의 변수

        try{
            replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, productImage);

            productDTO.setProductImageUrl(replaceFileName);

            log.info("[ProductService] insert Image Name : ", replaceFileName);

            // 저장을 위해서 일반 DTO객체를 Entity객체로 변경
            Product insertProduct = modelMapper.map(productDTO, Product.class);

            productRepository.save(insertProduct);

            result = 1;
        } catch (Exception e){
            System.out.println("check");
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }


        log.info("[ProductService] insertProduct End ===================");
        return (result > 0)? "상품 입력 성공": "상품 입력 실패";
    }

    public List<ProductDTO> selectProductListAboutMeal() {
        log.info("[ProductService] selectProductListAboutMeal Start ===================================");

        List<Product> productListAboutMeal = productRepository.findByCategoryCode(1);

        List<ProductDTO> productDTOList = productListAboutMeal.stream()
                                            .map(product -> modelMapper.map(product, ProductDTO.class))
                                            .collect(Collectors.toList());

        for(int i = 0; i < productDTOList.size(); i++){
            productDTOList.get(i).setProductImageUrl(IMAGE_URL + productDTOList.get(i).getProductImageUrl());
        }

        log.info("[ProductService] selectProductListAboutMeal End ==============================");

        return productDTOList;
    }

    public List<ProductDTO> selectProductListAboutDessert() {
        log.info("[ProductService] selectProductListAboutDessert Start ===================================");

        List<Product> productListAboutDessert = productRepository.findByCategoryCode(2);

        List<ProductDTO> productDTOList = productListAboutDessert.stream()
                                                .map(product -> modelMapper.map(product, ProductDTO.class))
                                                .collect(Collectors.toList());

        for(int i = 0; i < productDTOList.size(); i++){
            productDTOList.get(i).setProductImageUrl(IMAGE_URL + productDTOList.get(i).getProductImageUrl());
        }

        log.info("[ProductService] selectProductListAboutDessert End ==============================");

        return productDTOList;
    }


    public List<ProductDTO> selectProductListAboutBeverage() {
        log.info("[ProductService] selectProductListAboutBeverage Start ===================================");

        List<Product> productListAboutBeverage = productRepository.findByCategoryCode(3);

        List<ProductDTO> productDTOList = productListAboutBeverage.stream()
                                                .map(product -> modelMapper.map(product, ProductDTO.class))
                                                .collect(Collectors.toList());

        for(int i = 0; i < productDTOList.size(); i++){
            productDTOList.get(i).setProductImageUrl(IMAGE_URL + productDTOList.get(i).getProductImageUrl());
        }

        log.info("[ProductService] selectProductListAboutBeverage End ==============================");

        return productDTOList;
    }

    public Page<ProductAndCategoryDTO> selectProductListWithPagingForAdmin(Criteria cri) {
        log.info("[ProductService] selectProductListWithPagingForAdmin Start ===================================");

        int index = cri.getPageNum() - 1;
        int count = cri.getAmount();
        Pageable paging = PageRequest.of(index, count, Sort.by("productCode").descending());

        Page<ProductAndCategory> result = productAndCategoryRepository.findAll(paging);

        Page<ProductAndCategoryDTO> resultList = result.map(product -> modelMapper.map(product, ProductAndCategoryDTO.class));

        for(int i = 0 ; i < resultList.toList().size() ; i++) {
            resultList.toList().get(i).setProductImageUrl(IMAGE_URL + resultList.toList().get(i).getProductImageUrl());
        }

        log.info("[ProductService] selectProductListWithPagingForAdmin End ===================================");

        return resultList;
    }

    @Transactional
    public String updateProduct(ProductDTO productDTO, MultipartFile productImage) {

        log.info("[ProductService] updateProduct Start ===================================");
        log.info("[ProductService] productDTO : " + productDTO);

        String replaceFileName = null;
        int result = 0;

        try {

            /* update 할 엔티티 조회 */
            Product product = productRepository.findById(productDTO.getProductCode()).get();
            String oriImage = product.getProductImageUrl();
            log.info("[updateProduct] oriImage : " + oriImage);

            /* update를 위한 엔티티 값 수정 */
            product = product.categoryCode(productDTO.getCategoryCode())
                    .productName(productDTO.getProductName())
                    .productPrice(productDTO.getProductPrice())
                    .productOrderable(productDTO.getProductOrderable())
                    .categoryCode(productDTO.getCategoryCode())
                    .productStock(productDTO.getProductStock())
                    .productDescription(productDTO.getProductDescription()).build();

            if(productImage != null){
                String imageName = UUID.randomUUID().toString().replace("-", "");
                replaceFileName = FileUploadUtils.saveFile(IMAGE_DIR, imageName, productImage);
                log.info("[updateProduct] InsertFileName : " + replaceFileName);

                product = product.productImageUrl(replaceFileName).build();	// 새로운 파일 이름으로 update
                log.info("[updateProduct] deleteImage : " + oriImage);

                boolean isDelete = FileUploadUtils.deleteFile(IMAGE_DIR, oriImage);
                log.info("[update] isDelete : " + isDelete);

            } else {

                /* 이미지 변경 없을 시 */
                product = product.productImageUrl(oriImage).build();
            }

            result = 1;

        } catch (IOException e) {
            log.info("[updateProduct] Exception!!");
            FileUploadUtils.deleteFile(IMAGE_DIR, replaceFileName);
            throw new RuntimeException(e);
        }
        log.info("[ProductService] updateProduct End ===================================");
        return (result > 0) ? "상품 업데이트 성공" : "상품 업데이트 실패";
    }
}
