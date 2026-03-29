package com.atechproc.service.cart;

import com.atechproc.dto.CartDto;
import com.atechproc.dto.CartItemDto;
import com.atechproc.model.Cart;
import com.atechproc.model.CartItem;

public interface ICartService {
    CartDto addItemToCart(Long medicineId, String jwt) throws Exception;
    CartItemDto updateCartItemQuantity(Long id, int quantity, String jwt) throws Exception;
    CartItem getCartItemById(Long id);
    Cart getPharmacyCart(String jwt);
    CartDto removeItemFromCart(Long id, String jwt) throws Exception;
    void cancelCart(String jwt) throws Exception;
    void clearCart(String jwt) throws Exception;
}
