package com.bookstore.dto;

import lombok.*;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemDto {

    private Long id;
    private Long bookId;
    private String title;
    private String author;
    private BigDecimal price;
    private Integer quantity;
    private BigDecimal subtotal;
}
