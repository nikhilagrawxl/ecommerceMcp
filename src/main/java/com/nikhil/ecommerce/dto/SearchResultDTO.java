package com.nikhil.ecommerce.dto;

import java.util.List;

public class SearchResultDTO {
    private String keyword;
    private int numResults;
    private List<SearchCategoryDTO> matchingCategories;
    private List<SearchProductDTO> topProducts;

    public SearchResultDTO() {
    }

    public SearchResultDTO(String keyword, int numResults, List<SearchCategoryDTO> matchingCategories, List<SearchProductDTO> topProducts) {
        this.keyword = keyword;
        this.numResults = numResults;
        this.matchingCategories = matchingCategories;
        this.topProducts = topProducts;
    }

    public String getKeyword() { return keyword; }
    public int getNumResults() { return numResults; }
    public List<SearchCategoryDTO> getMatchingCategories() { return matchingCategories; }
    public List<SearchProductDTO> getTopProducts() { return topProducts; }

    public void setKeyword(String keyword) { this.keyword = keyword; }
    public void setNumResults(int numResults) { this.numResults = numResults; }
    public void setMatchingCategories(List<SearchCategoryDTO> matchingCategories) { this.matchingCategories = matchingCategories; }
    public void setTopProducts(List<SearchProductDTO> topProducts) { this.topProducts = topProducts; }
}
