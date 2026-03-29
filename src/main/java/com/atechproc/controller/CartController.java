package com.atechproc.controller;

import com.atechproc.dto.CartDto;
import com.atechproc.dto.CartItemDto;
import com.atechproc.mapper.CartMapper;
import com.atechproc.model.Cart;
import com.atechproc.response.ApiResponse;
import com.atechproc.service.cart.ICartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("${api.prefix}/carts")
public class CartController {

    private final ICartService cartService;

    @GetMapping
    public ResponseEntity<ApiResponse> getPharmacyCartHandler(
            @RequestHeader("Authorization") String jwt
    ) {
        Cart cart = cartService.getPharmacyCart(jwt);
        return ResponseEntity.ok(new ApiResponse("Success", CartMapper.toCartDto(cart)));
    }

    @PostMapping("/add-to-cart")
    ResponseEntity<ApiResponse> addItemToCartHandler(
            @RequestParam Long medicineId,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        CartDto cart = cartService.addItemToCart(medicineId, jwt);
        return ResponseEntity.ok(new ApiResponse("Medicine added to the cart", cart));
    }

    @PostMapping("/remove/item/{id}")
    public ResponseEntity<ApiResponse> removeItemFromCartHandler(
            @PathVariable Long id,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        CartDto cart = cartService.removeItemFromCart(id, jwt);
        return ResponseEntity.ok(new ApiResponse("Item removed from the cart", cart));
    }

    @PutMapping("/cart-item/{id}/update-quantity")
    ResponseEntity<ApiResponse> updateQuantityHandler(
            @PathVariable Long id,
            @RequestParam int quantity,
            @RequestHeader("Authorization") String jwt
    ) throws Exception {
        CartItemDto cartItem = cartService.updateCartItemQuantity(id, quantity, jwt);
        return ResponseEntity.ok(new ApiResponse("Quantity updated successfully", cartItem));
    }

    @PostMapping("/cancel")
    ResponseEntity<ApiResponse> cancelCart(@RequestHeader("Authorization") String jwt) throws Exception {
        cartService.cancelCart(jwt);
        return ResponseEntity.ok(new ApiResponse("Cart cancel", null));
    }

    @PostMapping("/clear")
    ResponseEntity<ApiResponse> clearCart(@RequestHeader("Authorization") String jwt) throws Exception {
        cartService.clearCart(jwt);
        return ResponseEntity.ok(new ApiResponse("Cart cleared successfully", null));
    }
}
