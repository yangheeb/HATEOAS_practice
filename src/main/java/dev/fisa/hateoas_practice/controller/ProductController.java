package dev.fisa.hateoas_practice.controller;

import dev.fisa.hateoas_practice.dto.ProductCreateRequest;
import dev.fisa.hateoas_practice.dto.ProductResponse;
import dev.fisa.hateoas_practice.dto.ProductUpdateRequest;
import dev.fisa.hateoas_practice.model.Product;
import dev.fisa.hateoas_practice.model.User;
import dev.fisa.hateoas_practice.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/products")
public class ProductController {

    private final ProductService productService;

    // 상품 등록
    @PostMapping
    public ResponseEntity<ProductResponse> create(
            @RequestBody @Valid ProductCreateRequest request
    ) {
        // TODO: JWT 붙이면 여기서 user 가져옴
        User user = new User();

        Product product = productService.create(request, user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(toResponse(product));
    }

    // 전체 조회 (페이징 + 필터)
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getProducts(
            @RequestParam(required = false) String category,
            @RequestParam int page,
            @RequestParam int size
    ) {

        Page<Product> products = productService.getProducts(category, page, size);

        List<ProductResponse> result = products.stream()
                .map(this::toResponse)
                .toList();

        return ResponseEntity.ok(result);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProduct(@PathVariable Long id) {
        return ResponseEntity.ok(
                toResponse(productService.getProduct(id))
        );
    }

    // 수정
    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> update(
            @PathVariable Long id,
            @RequestBody @Valid ProductUpdateRequest request
    ) {
        User user = new User();

        Product product = productService.update(id, request, user);

        return ResponseEntity.ok(toResponse(product));
    }

    // 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {

        User user = new User();

        productService.delete(id, user);

        return ResponseEntity.noContent().build();
    }


    // DTO 변환
    private ProductResponse toResponse(Product product) {
        return new ProductResponse(
                product.getId(),
                product.getName(),
                product.getDescription(),
                product.getPrice(),
                product.getStock(),
                product.getCategory(),
                product.getUser() != null ? product.getUser().getId() : null
        );
    }
}