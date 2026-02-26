package com.nikhil.ecommerce.mcp.tools;

import com.nikhil.ecommerce.mcp.ToolRegistry;
import com.nikhil.ecommerce.dto.SearchCategoryDTO;
import com.nikhil.ecommerce.dto.SearchProductDTO;
import com.nikhil.ecommerce.dto.SearchResultDTO;
import com.nikhil.ecommerce.service.SearchService;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class SearchTools {
    private final SearchService searchService;
    private final ToolRegistry registry;

    public SearchTools(SearchService searchService, ToolRegistry registry) {
        this.searchService = searchService;
        this.registry = registry;
    }

    @PostConstruct
    public void register() {
        Map<String, Object> schema = new HashMap<>();
        schema.put("type", "object");
        Map<String, Object> props = new HashMap<>();
        props.put("keyword", new HashMap<String, Object>() {{ put("type", "string"); }});
        schema.put("properties", props);
        schema.put("required", new String[]{"keyword"});

        registry.register("searchProducts", args -> {
            String keyword = (String) args.get("keyword");
            SearchResultDTO result = searchService.getSearchResults(keyword);
            return formatMarkdown(result);
        }, new ToolRegistry.ToolMetadata("Search products on Snapdeal and return compact results", schema));
    }

    private String formatMarkdown(SearchResultDTO result) {
        StringBuilder sb = new StringBuilder();

        sb.append("**Search Results**\n");
        if (result.getKeyword() != null && !result.getKeyword().trim().isEmpty()) {
            sb.append("Query: ").append(result.getKeyword()).append("\n");
        }
        sb.append("Total Results: ").append(result.getNumResults()).append("\n\n");

        List<SearchCategoryDTO> categories = result.getMatchingCategories();
        if (categories != null && !categories.isEmpty()) {
            sb.append("**Top Categories**\n");
            int max = Math.min(5, categories.size());
            for (int i = 0; i < max; i++) {
                SearchCategoryDTO c = categories.get(i);
                sb.append("- ").append(c.getName());
                if (c.getNoOfResults() > 0) {
                    sb.append(" (").append(c.getNoOfResults()).append(")");
                }
                if (c.getUrl() != null && !c.getUrl().trim().isEmpty()) {
                    sb.append(" — ").append("[link](https://www.snapdeal.com/").append(c.getUrl()).append(")");
                }
                sb.append("\n");
            }
            sb.append("\n");
        }

        List<SearchProductDTO> products = result.getTopProducts();
        if (products == null || products.isEmpty()) {
            sb.append("No products found.");
            return sb.toString();
        }

        sb.append("**Top Products**\n");
        for (SearchProductDTO p : products) {
            sb.append("\n");
            sb.append("**").append(p.getTitle()).append("**\n");
            if (p.getImageUrl() != null && !p.getImageUrl().trim().isEmpty()) {
                sb.append("Image: ").append("[link](").append(p.getImageUrl()).append(")\n");
            }
            sb.append("Selling Price: ₹").append(formatMoney(p.getSellingPrice()));
            if (p.getMrp() > 0) {
                sb.append(" | MRP: ₹").append(formatMoney(p.getMrp()));
            }
            if (p.getDiscount() > 0) {
                sb.append(" | Discount: ").append(formatMoney(p.getDiscount())).append("%");
            }
            sb.append("\n");
            if (p.getKeyFeatures() != null && !p.getKeyFeatures().isEmpty()) {
                sb.append("Key Features: ");
                int max = Math.min(5, p.getKeyFeatures().size());
                for (int i = 0; i < max; i++) {
                    if (i > 0) sb.append(", ");
                    sb.append(p.getKeyFeatures().get(i));
                }
                sb.append("\n");
            }
            if (p.getPageUrl() != null && !p.getPageUrl().trim().isEmpty()) {
                sb.append("Page: ").append("[link](https://www.snapdeal.com/").append(p.getPageUrl()).append(")\n");
            }
        }
        return sb.toString();
    }

    private String formatMoney(double value) {
        if (value == (long) value) {
            return String.valueOf((long) value);
        }
        return String.format("%.2f", value);
    }
}
