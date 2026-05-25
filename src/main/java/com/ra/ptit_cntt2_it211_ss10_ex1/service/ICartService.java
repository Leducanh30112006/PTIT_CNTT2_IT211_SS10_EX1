package com.ra.ptit_cntt2_it211_ss10_ex1.service;

import com.ra.ptit_cntt2_it211_ss10_ex1.dto.CartItemDTO;
import com.ra.ptit_cntt2_it211_ss10_ex1.entity.CartItem;
import java.util.List;

public interface ICartService {
    CartItem addToCart(CartItemDTO cartItemDTO);
    List<CartItem> getCartByUserId(String userId);
    CartItem updateQuantity(Long id, Integer quantity);
    void deleteCartItem(Long id);
    void clearCart(String userId);
}