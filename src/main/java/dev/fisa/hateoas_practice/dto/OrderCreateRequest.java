package dev.fisa.hateoas_practice.dto;

import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class OrderCreateRequest {

    private Long productId;

    @Min(value = 1, message = "수량은 1 이상")
    private int quantity;
}