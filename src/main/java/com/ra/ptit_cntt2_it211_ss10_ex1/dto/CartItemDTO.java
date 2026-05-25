package com.ra.ptit_cntt2_it211_ss10_ex1.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Min;
import lombok.Data;

@Data
public class CartItemDTO {
    
    @NotBlank(message = "User ID không được để trống")
    private String userId;

    @NotBlank(message = "Product ID không được để trống")
    private String productId;

    @NotNull(message = "Số lượng không được để trống")
    @Min(value = 1, message = "Số lượng thêm vào giỏ hàng phải lớn hơn hoặc bằng 1")
    private Integer quantity;
}