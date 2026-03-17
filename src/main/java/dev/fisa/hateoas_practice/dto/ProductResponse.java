package dev.fisa.hateoas_practice.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ProductResponse {

    private Long id;
    private String name;
    private String description;
    private int price;
    private int stock;
    private String category;
    private Long userId;
}