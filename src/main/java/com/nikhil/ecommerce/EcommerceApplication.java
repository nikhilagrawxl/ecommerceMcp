package com.nikhil.ecommerce;

import com.nikhil.ecommerce.model.Order;
import com.nikhil.ecommerce.service.OrderService;
import com.nikhil.ecommerce.service.ProductService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class EcommerceApplication {

	public static void main(String[] args) {
		SpringApplication.run(EcommerceApplication.class, args);
	}

}
