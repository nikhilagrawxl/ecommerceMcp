package com.nikhil.ecommerce.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nikhil.ecommerce.dto.SearchCategoryDTO;
import com.nikhil.ecommerce.dto.SearchProductDTO;
import com.nikhil.ecommerce.dto.SearchResultDTO;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;

@Service
public class SearchService {

    private static final String BASE_URL = "https://mobileapi.snapdeal.com/service/get/search/v2/getSearchResults";
    private static final int TOP_PRODUCTS_LIMIT = 5;
    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Tool(description = "Search the catalog for an exact or partial match of a keyword to get search results including matching categories and top products.")
    public SearchResultDTO getSearchResults(String keyword) {
        String url = UriComponentsBuilder.fromHttpUrl(BASE_URL)
                .queryParam("keyword", keyword)
                .queryParam("number", 12)
                .queryParam("requestProtocol", "PROTOCOL_JSON")
                .queryParam("responseProtocol", "PROTOCOL_JSON")
                .queryParam("sortBy", "rlvncy")
                .queryParam("spellCheck", true)
                .queryParam("start", 0)
                .build()
                .encode()
                .toUriString();

        String response = restTemplate.getForObject(url, String.class);
        return mapToSearchResult(response);
    }

    private SearchResultDTO mapToSearchResult(String response) {
        if (response == null || response.trim().isEmpty()) {
            return new SearchResultDTO();
        }
        try {
            JsonNode root = objectMapper.readTree(response);
            JsonNode searchResultDTO = root.path("searchResultDTOMobile").path("searchResultDTO");

            SearchResultDTO result = new SearchResultDTO();
            result.setKeyword(searchResultDTO.path("keyword").asText(null));
            result.setNumResults(searchResultDTO.path("numResults").asInt(0));

            List<SearchCategoryDTO> categories = new ArrayList<>();
            for (JsonNode cat : searchResultDTO.path("matchingCategories")) {
                SearchCategoryDTO c = new SearchCategoryDTO();
                c.setName(cat.path("name").asText(null));
                c.setUrl(cat.path("url").asText(null));
                c.setNoOfResults(cat.path("noOfResults").asInt(0));
                categories.add(c);
            }
            result.setMatchingCategories(categories);

            List<SearchProductDTO> products = new ArrayList<>();
            int count = 0;
            for (JsonNode p : root.path("searchResultDTOMobile").path("catalogSearchDTOMobile")) {
                if (count >= TOP_PRODUCTS_LIMIT) {
                    break;
                }
                SearchProductDTO product = new SearchProductDTO();
                product.setTitle(p.path("title").asText(null));
                product.setSellingPrice(p.path("sellingPrice").asDouble(0));
                product.setPageUrl(p.path("pageUrl").asText(null));
                product.setBrandName(p.path("brandName").asText(null));
                product.setAvgRating(p.path("avgRating").asDouble(0));
                product.setImageUrl(extractImageUrl(p));
                product.setMrp(p.path("priceInfo").path("mrp").asDouble(0));
                product.setDiscount(p.path("priceInfo").path("discount").asDouble(0));
                product.setKeyFeatures(extractKeyFeatures(p));
                products.add(product);
                count++;
            }
            result.setTopProducts(products);
            return result;
        } catch (Exception e) {
            return new SearchResultDTO();
        }
    }

    private String extractImageUrl(JsonNode product) {
        JsonNode imgs = product.path("imgs");
        if (imgs.isArray() && imgs.size() > 0) {
            return imgs.get(0).asText(null);
        }
        return product.path("thumbnail").asText(null);
    }

    private List<String> extractKeyFeatures(JsonNode product) {
        List<String> features = new ArrayList<>();
        JsonNode keyFeatures = product.path("keyFeatures");
        if (keyFeatures.isArray()) {
            for (JsonNode f : keyFeatures) {
                String value = f.asText(null);
                if (value != null && !value.trim().isEmpty()) {
                    features.add(value.trim());
                }
            }
        }
        return features;
    }
}
