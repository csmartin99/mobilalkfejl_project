package com.example.csm_lakstextil_webshop;

public class Product {
    private final int productImg;
    private String productName;
    private float productRating;
    private String productDesc;
    private String productPrice;

    public Product(int productImg, String productName, float productRating, String productDesc, String productPrice) {
        this.productImg = productImg;
        this.productName = productName;
        this.productRating = productRating;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
    }

    public int getProductImg() {
        return productImg;
    }

    public String getProductName() {
        return productName;
    }

    public float getProductRating() {
        return productRating;
    }

    public String getProductDesc() {
        return productDesc;
    }

    public String getProductPrice() {
        return productPrice;
    }
}
