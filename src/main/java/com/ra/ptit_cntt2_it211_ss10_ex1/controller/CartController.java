package com.ra.ptit_cntt2_it211_ss10_ex1.controller;

import com.ra.ptit_cntt2_it211_ss10_ex1.dto.CartItemDTO;
import com.ra.ptit_cntt2_it211_ss10_ex1.entity.CartItem;
import com.ra.ptit_cntt2_it211_ss10_ex1.service.ICartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
@Slf4j
public class CartController {

    private final ICartService cartService;

    @PostMapping("/add")
    public ResponseEntity<?> addToCart(@Valid @RequestBody CartItemDTO cartItemDTO) {
        log.info("Nhận Request POST /api/cart/add - Payload: [userId: {}, productId: {}, quantity: {}]",
                cartItemDTO.getUserId(), cartItemDTO.getProductId(), cartItemDTO.getQuantity());

        CartItem savedItem = cartService.addToCart(cartItemDTO);
        
        log.info("Kết quả xử lý thành công cho User: {} - Trả về HTTP 201", cartItemDTO.getUserId());
        return new ResponseEntity<>(savedItem, HttpStatus.CREATED);
    }
    @GetMapping("/{userId}")
    public ResponseEntity<List<CartItem>> getCart(@PathVariable String userId) {
        log.info("Nhận Request GET /api/cart/{}", userId);
        List<CartItem> cartItems = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(cartItems);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateCartItem(@PathVariable Long id, @RequestParam Integer quantity) {
        log.info("Nhận Request PUT /api/cart/update/{} - Quantity: {}", id, quantity);
        CartItem updatedItem = cartService.updateQuantity(id, quantity);
        return ResponseEntity.ok(updatedItem);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<?> deleteCartItem(@PathVariable Long id) {
        log.info("Nhận Request DELETE /api/cart/delete/{}", id);
        cartService.deleteCartItem(id);
        return ResponseEntity.ok("Xóa thành công vật phẩm có ID: " + id);
    }
}