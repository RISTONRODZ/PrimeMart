package org.riston.ecommerce.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Ecommerce Backend API")
                        .version("1.0")
                        .description("API documentation E-commerce Public, Customer, Seller, and Admin endpoints.")
                        .contact(new Contact()
                                .name("Riston Rodrigues")
                                .email("ristonrodz1@gmail.com")
                                .url("https://github.com/RISTONRODZ")))
                .addSecurityItem(new SecurityRequirement().addList("bearer-jwt"))
                .components(new Components()
                        .addSecuritySchemes("bearer-jwt",
                                new SecurityScheme()
                                        .name("bearer-jwt")
                                        .type(SecurityScheme.Type.HTTP)
                                        .scheme("bearer")
                                        .bearerFormat("JWT")));
    }

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("1. Public")
                .pathsToMatch(
                        "/api/v1/home",
                        "/api/v1/auth/**",
                        "/api/v1/products/**",
                        "/api/v1/reviews/products/**",
                        "/api/v1/reviews",
                        "/api/v1/home-categories"
                )

                .addOpenApiCustomizer(openApi -> {
                    if (openApi.getPaths().containsKey("/api/v1/home-categories")) {
                        var pathItem = openApi.getPaths().get("/api/v1/home-categories");
                        pathItem.setPost(null);
                        pathItem.setPatch(null);
                        pathItem.setDelete(null);
                        pathItem.setPut(null);
                    }
                })
                .build();
    }

    @Bean
    public GroupedOpenApi customerApi() {
        return GroupedOpenApi.builder()
                .group("2. Customer")
                .pathsToMatch(
                        "/api/v1/users/profile",
                        "/api/v1/cart/**",
                        "/api/v1/wishlist/**",
                        "/api/v1/orders/**",
                        "/api/v1/payment/**",
                        "/api/v1/reviews/products/**",
                        "/api/v1/reviews/**",
                        "/api/v1/coupons/apply"
                ).build();
    }

    @Bean
    public GroupedOpenApi sellerApi() {
        return GroupedOpenApi.builder()
                .group("3. Seller")
                .pathsToMatch(
                        "/api/v1/seller/**",
                        "/api/v1/transactions/seller/**"
                ).build();
    }

    @Bean
    public GroupedOpenApi adminApi() {
        return GroupedOpenApi.builder()
                .group("4. Admin")
                .pathsToMatch(
                        "/api/v1/admin/**",
                        "/api/v1/coupons/**",
                        "/api/v1/home-categories/**"
                ).pathsToExclude(
                        "/api/v1/home-categories"
                )
                .build();
    }

}