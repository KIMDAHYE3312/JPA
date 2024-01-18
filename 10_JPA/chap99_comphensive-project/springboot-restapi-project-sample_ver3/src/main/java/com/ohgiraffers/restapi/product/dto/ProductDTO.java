package com.ohgiraffers.restapi.product.dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class ProductDTO {

    private int productCode;
    private String productName;
    private String productPrice;
    private String productDescription;
    private String productOrderable;
    private int categoryCode;
    private String productImageUrl;
    private Long productStock;

}
