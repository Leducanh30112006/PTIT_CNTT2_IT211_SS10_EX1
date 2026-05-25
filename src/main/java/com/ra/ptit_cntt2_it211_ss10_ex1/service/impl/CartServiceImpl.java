package com.ra.ptit_cntt2_it211_ss10_ex1.service.impl;

import com.ra.ptit_cntt2_it211_ss10_ex1.dto.CartItemDTO;
import com.ra.ptit_cntt2_it211_ss10_ex1.entity.CartItem;
import com.ra.ptit_cntt2_it211_ss10_ex1.repository.CartItemRepository;
import com.ra.ptit_cntt2_it211_ss10_ex1.service.ICartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CartServiceImpl implements ICartService {

    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional
    public CartItem addToCart(CartItemDTO dto) {
        log.info("Bắt đầu xử lý logic thêm vào giỏ hàng cho User: {} - Sản phẩm: {}", dto.getUserId(), dto.getProductId());
        Optional<CartItem> existingItemOpt = cartItemRepository.findByUserIdAndProductId(dto.getUserId(), dto.getProductId());

        CartItem cartItem;
        if (existingItemOpt.isPresent()) {
            cartItem = existingItemOpt.get();
            int oldQuantity = cartItem.getQuantity();
            int newQuantity = oldQuantity + dto.getQuantity();
            cartItem.setQuantity(newQuantity);
            
            log.info("Sản phẩm đã tồn tại. Tiến hành cộng dồn số lượng: {} + {} = {}", oldQuantity, dto.getQuantity(), newQuantity);
        } else {
            cartItem = CartItem.builder()
                    .userId(dto.getUserId())
                    .productId(dto.getProductId())
                    .quantity(dto.getQuantity())
                    .build();
            log.info("Sản phẩm chưa có trong giỏ hàng. Tạo mới bản ghi.");
        }

        CartItem savedItem = cartItemRepository.save(cartItem);
        log.info("Lưu thông tin giỏ hàng thành công. CartItem ID: {}", savedItem.getId());
        return savedItem;
    }

    @Override
    public List<CartItem> getCartByUserId(String userId) {
        log.info("Lấy danh sách giỏ hàng của User: {}", userId);
        return cartItemRepository.findByUserId(userId);
    }

    @Override
    @Transactional
    public CartItem updateQuantity(Long id, Integer quantity) {
        log.info("Cập nhật số lượng cho CartItem ID: {} thành {}", id, quantity);
        if (quantity <= 0) {
            log.error("Cập nhật thất bại: Số lượng cập nhật {} phải > 0", quantity);
            throw new IllegalArgumentException("Số lượng cập nhật phải lớn hơn 0");
        }
        CartItem item = cartItemRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Không tìm thấy CartItem với ID: {}", id);
                    return new RuntimeException("Không tìm thấy vật phẩm trong giỏ hàng");
                });
        item.setQuantity(quantity);
        return cartItemRepository.save(item);
    }

    @Override
    @Transactional
    public void deleteCartItem(Long id) {
        log.info("Xóa CartItem ID: {}", id);
        if (!cartItemRepository.existsById(id)) {
            log.warn("CartItem ID {} không tồn tại để xóa", id);
            throw new RuntimeException("Vật phẩm không tồn tại");
        }
        cartItemRepository.deleteById(id);
        log.info("Xóa CartItem ID: {} thành công", id);
    }

    @Override
    @Transactional
    public void clearCart(String userId) {
        log.info("Xóa toàn bộ giỏ hàng của User: {}", userId);
        List<CartItem> items = cartItemRepository.findByUserId(userId);
        cartItemRepository.deleteAll(items);
        log.info("Đã xóa sạch giỏ hàng của User: {}", userId);
    }
}