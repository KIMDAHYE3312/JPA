package com.ohgiraffers.section03.projection;

/* 여러 컬럼 정보를 의미 있는 단위로 묶기 위한 용도의 클래스 */
public class CategoryInfo {

    private int categoryCode;

    private String categoryName;

    public CategoryInfo() {
    }

    public CategoryInfo(int categoryCode, String categoryName) {
        this.categoryCode = categoryCode;
        this.categoryName = categoryName;
    }

    public int getCategoryCode() {
        return categoryCode;
    }

    public void setCategoryCode(int categoryCode) {
        this.categoryCode = categoryCode;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    @Override
    public String toString() {
        return "CategoryInfo{" +
                "categoryCode=" + categoryCode +
                ", categoryName='" + categoryName + '\'' +
                '}';
    }
}
