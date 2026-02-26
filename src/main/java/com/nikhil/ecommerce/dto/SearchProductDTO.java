package com.nikhil.ecommerce.dto;

public class SearchProductDTO {
    private String title;
    private double sellingPrice;
    private String pageUrl;
    private String brandName;
    private double avgRating;
    private String imageUrl;
    private double mrp;
    private double discount;
    private java.util.List<String> keyFeatures;

    public SearchProductDTO() {
    }

    public SearchProductDTO(String title, double sellingPrice, String pageUrl, String brandName, double avgRating, String imageUrl, double mrp, double discount, java.util.List<String> keyFeatures) {
        this.title = title;
        this.sellingPrice = sellingPrice;
        this.pageUrl = pageUrl;
        this.brandName = brandName;
        this.avgRating = avgRating;
        this.imageUrl = imageUrl;
        this.mrp = mrp;
        this.discount = discount;
        this.keyFeatures = keyFeatures;
    }

    public String getTitle() { return title; }
    public double getSellingPrice() { return sellingPrice; }
    public String getPageUrl() { return pageUrl; }
    public String getBrandName() { return brandName; }
    public double getAvgRating() { return avgRating; }
    public String getImageUrl() { return imageUrl; }
    public double getMrp() { return mrp; }
    public double getDiscount() { return discount; }
    public java.util.List<String> getKeyFeatures() { return keyFeatures; }

    public void setTitle(String title) { this.title = title; }
    public void setSellingPrice(double sellingPrice) { this.sellingPrice = sellingPrice; }
    public void setPageUrl(String pageUrl) { this.pageUrl = pageUrl; }
    public void setBrandName(String brandName) { this.brandName = brandName; }
    public void setAvgRating(double avgRating) { this.avgRating = avgRating; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public void setMrp(double mrp) { this.mrp = mrp; }
    public void setDiscount(double discount) { this.discount = discount; }
    public void setKeyFeatures(java.util.List<String> keyFeatures) { this.keyFeatures = keyFeatures; }
}
