package dev.fisa.hateoas_practice.controller;

import dev.fisa.hateoas_practice.dto.ProductCreateRequest;
import dev.fisa.hateoas_practice.dto.ProductModel;
import dev.fisa.hateoas_practice.dto.ProductUpdateRequest;
import dev.fisa.hateoas_practice.model.Product;
import dev.fisa.hateoas_practice.model.User;
import dev.fisa.hateoas_practice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Links;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductModel> create(
            @RequestBody @Valid ProductCreateRequest request,
            @AuthenticationPrincipal User user
    ) {
        Product product = productService.create(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ProductModel.ofCreatedOrUpdated(product));
    }

    // 전체 조회 (페이징 + 필터)
    @GetMapping
    public ResponseEntity<Map<String, Object>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Product> products = productService.getProducts(category, page, size);

        List<ProductModel> productModels = products.stream()
                .map(ProductModel::new)
                .toList();

        Map<String, Object> links = new LinkedHashMap<>();
        links.put("self", Map.of("href", "/api/products?page=" + page + "&size=" + size));
        links.put("profile", Map.of("href", "/swagger-ui/index.html"));

        if (products.hasNext()) {
            links.put("next", Map.of("href", "/api/products?page=" + (page + 1) + "&size=" + size));
        }
        if (products.hasPrevious()) {
            links.put("prev", Map.of("href", "/api/products?page=" + (page - 1) + "&size=" + size));
        }

        Map<String, Object> response = new LinkedHashMap<>();
        response.put("products", productModels);
        response.put("_links", links);

        return ResponseEntity.ok(response);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductModel> getProduct(
            @PathVariable Long id,
            @AuthenticationPrincipal User user  // 비로그인이면 null
    ) {
        Product product = productService.getProduct(id);
        return ResponseEntity.ok(ProductModel.ofDetail(product, user));
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProductModel> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateRequest request,
            @AuthenticationPrincipal User user
    ) {
        Product product = productService.update(id, request, user);
        return ResponseEntity.ok(ProductModel.ofCreatedOrUpdated(product));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<ProductModel> delete(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        Product product = productService.delete(id, user);
        return ResponseEntity.ok(ProductModel.ofDeleted(product));
    }
}