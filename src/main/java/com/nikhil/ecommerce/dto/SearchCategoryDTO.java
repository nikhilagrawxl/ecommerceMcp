package com.nikhil.ecommerce.dto;

public class SearchCategoryDTO {
    private String name;
    private String url;
    private int noOfResults;

    public SearchCategoryDTO() {
    }

    public SearchCategoryDTO(String name, String url, int noOfResults) {
        this.name = name;
        this.url = url;
        this.noOfResults = noOfResults;
    }

    public String getName() { return name; }
    public String getUrl() { return url; }
    public int getNoOfResults() { return noOfResults; }

    public void setName(String name) { this.name = name; }
    public void setUrl(String url) { this.url = url; }
    public void setNoOfResults(int noOfResults) { this.noOfResults = noOfResults; }
}
