package dev.fisa.hateoas_practice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductUpdateRequest {

    @NotBlank
    private String name;

    private String description;

    @Min(0)
    private int price;

    @Min(1)
    private int stock;

    @NotBlank
    private String category;
}