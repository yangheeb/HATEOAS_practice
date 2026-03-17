package dev.fisa.hateoas_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderResponse {

    private Long orderId;
    private Long productId;
    private int quantity;
    private String status;
}