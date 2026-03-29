package com.atechproc.mapper;

import com.atechproc.dto.CartDto;
import com.atechproc.dto.CartItemDto;
import com.atechproc.model.Cart;
import com.atechproc.model.CartItem;

import java.util.List;

public class CartMapper {
    public static CartItemDto toCartItemDto(CartItem cartItem) {
        CartItemDto dto = new CartItemDto();
        dto.setId(cartItem.getId());
        dto.setMedicine(MedicineMapper.toDto(cartItem.getMedicine()));
        dto.setQuantity(cartItem.getQuantity());
        dto.setTotal(cartItem.getTotal());
        dto.setProfit(cartItem.getProfit());

        return dto;
    }

    public static List<CartItemDto> toCartItemDTOs(List<CartItem> items) {
        return items.stream()
                .map(CartMapper::toCartItemDto).toList();
    }

    public static CartDto toCartDto(Cart cart) {
        CartDto dto = new CartDto();
        dto.setId(cart.getId());
        dto.setCartItems(toCartItemDTOs(cart.getItems()));
        dto.setTotal(cart.getTotal());
        dto.setProfit(cart.getProfit());

        return dto;
    }
}
