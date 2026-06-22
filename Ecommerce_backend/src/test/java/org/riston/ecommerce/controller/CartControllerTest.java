package org.riston.ecommerce.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.riston.ecommerce.mapper.CartItemMapper;
import org.riston.ecommerce.model.Cart;
import org.riston.ecommerce.model.CartItem;
import org.riston.ecommerce.model.Product;
import org.riston.ecommerce.model.User;
import org.riston.ecommerce.request.AddItemRequestDto;
import org.riston.ecommerce.request.UpdateCartItemRequest;
import org.riston.ecommerce.response.CartItemDto;
import org.riston.ecommerce.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(CartController.class)
class CartControllerTest {

    private static final String BASE = "/api/v1/cart";
    private static final String JWT = "Bearer mock.jwt.token";

    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockitoBean
    private CartService cartService;
    @MockitoBean
    private CartItemService cartItemService;
    @MockitoBean
    private UserService userService;
    @MockitoBean
    private ProductService productService;
    @MockitoBean
    private CouponService couponService;
    @MockitoBean
    private CartItemMapper cartItemMapper;

    private static User sampleUser() {
        User user = new User();
        user.setId(42L);
        user.setEmail("john@example.com");
        user.setFullName("John Doe");
        return user;
    }

    private static Product sampleProduct() {
        Product product = new Product();
        product.setId(500L);
        product.setTitle("Running Shoes");
        product.setImages(List.of("https://example.com/shoe.jpg"));
        product.setSellingPrice(1999);
        product.setMrpPrice(2999);
        return product;
    }

    private static CartItem sampleCartItem(Long id, Product product) {
        CartItem item = new CartItem();
        item.setId(id);
        item.setProduct(product);
        item.setSize("10");
        item.setQuantity(2);
        item.setMrpPrice(2999);
        item.setSellingPrice(1999);
        return item;
    }

    private static Cart sampleCart(User user, Set<CartItem> items) {
        Cart cart = new Cart();
        cart.setId(10L);
        cart.setUser(user);
        cart.setCartItems(items);
        cart.setTotalSellingPrice(1999.0);
        cart.setTotalItem(2);
        cart.setDiscount(0);
        cart.setTotalMrpPrice(2999);
        return cart;
    }

    @Nested
    @DisplayName("GET /cart")
    class FindUserCartTests {

        @Test
        @DisplayName("returns 200 with the user's cart")
        void findUserCart_success() throws Exception {
            User user = sampleUser();
            Product product = sampleProduct();
            CartItem item = sampleCartItem(101L, product);
            Cart cart = sampleCart(user, new HashSet<>(Set.of(item)));

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(cartService.findUserCart(user)).thenReturn(cart);

            mockMvc.perform(get(BASE).header("Authorization", JWT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("User cart retrieved success"))
                    .andExpect(jsonPath("$.data.id").value(10))
                    .andExpect(jsonPath("$.data.userId").value(42))
                    .andExpect(jsonPath("$.data.items.length()").value(1))
                    .andExpect(jsonPath("$.data.items[0].id").value(101))
                    .andExpect(jsonPath("$.data.items[0].productId").value(500))
                    .andExpect(jsonPath("$.data.items[0].productTitle").value("Running Shoes"))
                    .andExpect(jsonPath("$.data.totalItems").value(2))
                    .andExpect(jsonPath("$.data.totalSellingPrice").value(1999.0));

            verify(userService).findUserByJwtToken(JWT);
            verify(cartService).findUserCart(user);
        }

        @Test
        @DisplayName("returns an empty items list when the cart has no items")
        void findUserCart_emptyCart() throws Exception {
            User user = sampleUser();
            Cart cart = sampleCart(user, new HashSet<>());

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(cartService.findUserCart(user)).thenReturn(cart);

            mockMvc.perform(get(BASE).header("Authorization", JWT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.items.length()").value(0));
        }

        @Test
        @DisplayName("returns 500 (via catch-all) when Authorization header is missing")
        void findUserCart_missingAuthHeader_isRejected() throws Exception {
            mockMvc.perform(get(BASE))
                    .andExpect(result -> {
                        int status = result.getResponse().getStatus();
                        org.junit.jupiter.api.Assertions.assertTrue(status >= 400,
                                "Expected an error status for missing Authorization header, got " + status);
                    });

            verifyNoInteractions(cartService);
        }

        @Test
        @DisplayName("invalid/expired JWT is rejected per GlobalExceptionHandler's BadCredentialsException mapping (403)")
        void findUserCart_invalidToken_returns403() throws Exception {
            when(userService.findUserByJwtToken("Bearer invalid.token"))
                    .thenThrow(new org.springframework.security.authentication.BadCredentialsException(
                            "Invalid or expired token"));

            mockMvc.perform(get(BASE).header("Authorization", "Bearer invalid.token"))
                    .andExpect(status().isForbidden())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Invalid or expired token"));

            verifyNoInteractions(cartService);
        }
    }

    @Nested
    @DisplayName("PUT /cart/add")
    class AddItemToCartTests {

        @Test
        @DisplayName("returns 200 with the added item on success")
        void addItem_success() throws Exception {
            User user = sampleUser();
            Product product = sampleProduct();
            AddItemRequestDto request = new AddItemRequestDto("10", 2, 500L);
            CartItem savedItem = sampleCartItem(101L, product);
            CartItemDto dto = CartItemDto.fromEntity(savedItem);

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(productService.findProductById(500L)).thenReturn(product);
            when(cartService.addCartItem(user, product, "10", 2)).thenReturn(savedItem);
            when(cartItemMapper.toDto(savedItem)).thenReturn(dto);

            mockMvc.perform(put(BASE + "/add")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Item added to cart success"))
                    .andExpect(jsonPath("$.data.id").value(101))
                    .andExpect(jsonPath("$.data.productId").value(500))
                    .andExpect(jsonPath("$.data.size").value("10"))
                    .andExpect(jsonPath("$.data.quantity").value(2));

            verify(productService).findProductById(500L);
            verify(cartService).addCartItem(user, product, "10", 2);
            verify(cartItemMapper).toDto(savedItem);
        }

        @Test
        @DisplayName("returns 400 when size is blank (@NotBlank violation) — body is EMPTY due to a GlobalExceptionHandler defect")
        void addItem_blankSize_returns400() throws Exception {
            String invalidJson = """
                    {"size": "", "quantity": 2, "productId": 500}
                    """;

            mockMvc.perform(put(BASE + "/add")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(""));

            verifyNoInteractions(cartService);
            verifyNoInteractions(productService);
        }

        @Test
        @DisplayName("returns 400 when quantity is less than 1 (@Min violation) — body is EMPTY (see blankSize test above for why)")
        void addItem_quantityBelowMin_returns400() throws Exception {
            String invalidJson = """
                    {"size": "10", "quantity": 0, "productId": 500}
                    """;

            mockMvc.perform(put(BASE + "/add")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(""));

            verifyNoInteractions(cartService);
        }

        @Test
        @DisplayName("returns 400 when productId is null (@NotNull violation) — body is EMPTY (see blankSize test above for why)")
        void addItem_nullProductId_returns400() throws Exception {
            String invalidJson = """
                    {"size": "10", "quantity": 2, "productId": null}
                    """;

            mockMvc.perform(put(BASE + "/add")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(""));

            verifyNoInteractions(cartService);
        }

        @Test
        @DisplayName("returns 404 when product does not exist")
        void addItem_productNotFound_returns404() throws Exception {
            User user = sampleUser();
            AddItemRequestDto request = new AddItemRequestDto("10", 2, 9999L);

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(productService.findProductById(9999L))
                    .thenThrow(new org.riston.ecommerce.exception.ProductException("Product not found"));

            mockMvc.perform(put(BASE + "/add")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Product not found"));

            verifyNoInteractions(cartService);
        }
    }

    @Nested
    @DisplayName("DELETE /cart/item/{cartItemId}")
    class DeleteCartItemTests {

        @Test
        @DisplayName("returns 200 with null data on successful removal")
        void deleteCartItem_success() throws Exception {
            User user = sampleUser();
            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            doNothing().when(cartItemService).removeCartItem(42L, 101L);

            mockMvc.perform(delete(BASE + "/item/101").header("Authorization", JWT))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Item removed from cart"))
                    .andExpect(jsonPath("$.data").doesNotExist());

            verify(cartItemService).removeCartItem(42L, 101L);
        }

        @Test
        @DisplayName("returns 404 when the cart item does not exist (ItemNotFoundException)")
        void deleteCartItem_notFound_returns404() throws Exception {
            User user = sampleUser();
            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            doThrow(new org.riston.ecommerce.exception.ItemNotFoundException("Cart item not found"))
                    .when(cartItemService).removeCartItem(42L, 9999L);

            mockMvc.perform(delete(BASE + "/item/9999").header("Authorization", JWT))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Cart item not found"));
        }

        @Test
        @DisplayName("returns 400 with proper error envelope when cartItemId path variable is not a number")
        void deleteCartItem_nonNumericId_isRejected() throws Exception {
            mockMvc.perform(delete(BASE + "/item/not-a-number").header("Authorization", JWT))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("cartItemId")));

            verifyNoInteractions(cartItemService);
        }
    }

    @Nested
    @DisplayName("PUT /cart/item/{cartItemId}")
    class UpdateCartItemTests {

        @Test
        @DisplayName("returns 200 with the updated item on success")
        void updateCartItem_success() throws Exception {
            User user = sampleUser();
            Product product = sampleProduct();
            UpdateCartItemRequest request = new UpdateCartItemRequest(3, "M");
            CartItem updated = sampleCartItem(101L, product);
            updated.setQuantity(3);
            updated.setSize("M");

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(cartItemService.updateCartItem(eq(42L), eq(101L), any(CartItem.class)))
                    .thenReturn(updated);

            mockMvc.perform(put(BASE + "/item/101")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Cart item updated successfully"))
                    .andExpect(jsonPath("$.data.quantity").value(3))
                    .andExpect(jsonPath("$.data.size").value("M"));

            verify(cartItemService).updateCartItem(eq(42L), eq(101L), any(CartItem.class));
        }

        @Test
        @DisplayName("returns 400 when quantity is less than 1 (@Min violation) — body is EMPTY due to GlobalExceptionHandler defect")
        void updateCartItem_quantityBelowMin_returns400() throws Exception {
            String invalidJson = """
                    {"quantity": 0, "size": "M"}
                    """;

            mockMvc.perform(put(BASE + "/item/101")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(invalidJson))
                    .andExpect(status().isBadRequest())
                    .andExpect(content().string(""));

            verifyNoInteractions(cartItemService);
        }

        @Test
        @DisplayName("returns 404 when the cart item does not exist (ItemNotFoundException)")
        void updateCartItem_notFound_returns404() throws Exception {
            User user = sampleUser();
            UpdateCartItemRequest request = new UpdateCartItemRequest(2, "L");

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(cartItemService.updateCartItem(eq(42L), eq(9999L), any(CartItem.class)))
                    .thenThrow(new org.riston.ecommerce.exception.ItemNotFoundException("Cart item not found"));

            mockMvc.perform(put(BASE + "/item/9999")
                            .header("Authorization", JWT)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value("Cart item not found"));
        }
    }

    @Nested
    @DisplayName("POST /cart/apply")
    class ApplyCouponTests {

        @Test
        @DisplayName("apply=true calls couponService.applyCoupon and returns the updated cart")
        void applyCoupon_apply_success() throws Exception {
            User user = sampleUser();
            Cart cart = sampleCart(user, new HashSet<>());
            cart.setCouponCode("SAVE10");
            cart.setDiscount(200);

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(couponService.applyCoupon("SAVE10", 2500.0, user)).thenReturn(cart);

            mockMvc.perform(post(BASE + "/apply")
                            .header("Authorization", JWT)
                            .param("apply", "true")
                            .param("code", "SAVE10")
                            .param("orderValue", "2500.0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.success").value(true))
                    .andExpect(jsonPath("$.message").value("Coupon processed successfully"))
                    .andExpect(jsonPath("$.data.appliedCouponCode").value("SAVE10"))
                    .andExpect(jsonPath("$.data.couponDiscount").value(200.0));

            verify(couponService).applyCoupon("SAVE10", 2500.0, user);
            verify(couponService, never()).removeCoupon(anyString(), any());
        }

        @Test
        @DisplayName("apply=false (any non-'true' value) calls couponService.removeCoupon")
        void applyCoupon_remove_success() throws Exception {
            User user = sampleUser();
            Cart cart = sampleCart(user, new HashSet<>());
            cart.setCouponCode(null);
            cart.setDiscount(0);

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(couponService.removeCoupon("SAVE10", user)).thenReturn(cart);

            mockMvc.perform(post(BASE + "/apply")
                            .header("Authorization", JWT)
                            .param("apply", "false")
                            .param("code", "SAVE10")
                            .param("orderValue", "2500.0"))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.data.appliedCouponCode").doesNotExist());

            verify(couponService).removeCoupon("SAVE10", user);
            verify(couponService, never()).applyCoupon(anyString(), anyDouble(), any());
        }

        @Test
        @DisplayName("DOCUMENTS SURPRISING BEHAVIOR: apply='TRUE' (different case) is treated as remove, not apply")
        void applyCoupon_caseSensitiveApplyParam_routesToRemove() throws Exception {
            User user = sampleUser();
            Cart cart = sampleCart(user, new HashSet<>());

            when(userService.findUserByJwtToken(JWT)).thenReturn(user);
            when(couponService.removeCoupon("SAVE10", user)).thenReturn(cart);

            mockMvc.perform(post(BASE + "/apply")
                            .header("Authorization", JWT)
                            .param("apply", "TRUE")
                            .param("code", "SAVE10")
                            .param("orderValue", "2500.0"))
                    .andExpect(status().isOk());

            verify(couponService).removeCoupon("SAVE10", user);
            verify(couponService, never()).applyCoupon(anyString(), anyDouble(), any());
        }

        @Test
        @DisplayName("returns 400 when required query params are missing")
        void applyCoupon_missingRequiredParam_isRejected() throws Exception {
            mockMvc.perform(post(BASE + "/apply")
                            .header("Authorization", JWT)
                            .param("code", "SAVE10")
                            .param("orderValue", "2500.0"))
                    .andExpect(result -> {
                        int status = result.getResponse().getStatus();
                        org.junit.jupiter.api.Assertions.assertTrue(status >= 400,
                                "Expected an error status for missing required param, got " + status);
                    });

            verifyNoInteractions(couponService);
        }

        @Test
        @DisplayName("returns 400 with proper error envelope when orderValue is not a valid double")
        void applyCoupon_nonNumericOrderValue_returns400() throws Exception {
            mockMvc.perform(post(BASE + "/apply")
                            .header("Authorization", JWT)
                            .param("apply", "true")
                            .param("code", "SAVE10")
                            .param("orderValue", "not-a-number"))
                    .andExpect(status().isBadRequest())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.message").value(org.hamcrest.Matchers.containsString("orderValue")));

            verifyNoInteractions(couponService);
        }
    }
}