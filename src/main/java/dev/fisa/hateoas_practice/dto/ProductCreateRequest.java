package dev.fisa.hateoas_practice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCreateRequest {

    @NotBlank(message = "상품명은 필수입니다")
    private String name;

    @Size(max = 1000, message = "설명은 1000자 이하")
    private String description;

    @Min(value = 0, message = "가격은 0 이상")
    private int price;

    @Min(value = 1, message = "재고는 1 이상")
    private int stock;

    @NotBlank(message = "카테고리는 필수입니다")
    private String category;
}