package com.example.csm_lakstextil_webshop;

//Adatmodell definiálása (class vagy interfész formájában)
public class Product {
    private String productId;
    private int productImg;
    private String productName;
    private float productRating;
    private String productDesc;
    private String productPrice;
    private int productCart;

    public Product() {
    }

    public Product(int productImg, String productName, float productRating, String productDesc, String productPrice, int productCart) {
        this.productImg = productImg;
        this.productName = productName;
        this.productRating = productRating;
        this.productDesc = productDesc;
        this.productPrice = productPrice;
        this.productCart = productCart;
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

    public int getProductCart() {
        return productCart;
    }

    public String _getproductId() {
        return productId;
    }

    public void setproductId(String productId) {
        this.productId = productId;
    }
}
