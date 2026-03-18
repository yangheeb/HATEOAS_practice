package dev.fisa.hateoas_practice.service;

import dev.fisa.hateoas_practice.model.Product;
import dev.fisa.hateoas_practice.model.User;
import dev.fisa.hateoas_practice.repository.ProductRepository;
import dev.fisa.hateoas_practice.dto.ProductCreateRequest;
import dev.fisa.hateoas_practice.dto.ProductUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductService {

    private final ProductRepository productRepository;

    // 상품 등록
    public Product create(ProductCreateRequest request, User user) {

        Product product = Product.create(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory(),
                user
        );

        return productRepository.save(product);
    }

    // 전체 조회 (페이징 + 필터)
    @Transactional(readOnly = true)
    public Page<Product> getProducts(String category, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        if (category != null) {
            return productRepository.findByCategory(category, pageable);
        }

        return productRepository.findAll(pageable);
    }

    // 단건 조회
    @Transactional(readOnly = true)
    public Product getProduct(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));
    }

    // 수정 (권한 체크 포함)
    public Product update(Long id, ProductUpdateRequest request, User user) {

        Product product = getProduct(id);

        // 권한 체크
        if (!product.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("수정 권한 없음");
        }

        product.update(
                request.getName(),
                request.getDescription(),
                request.getPrice(),
                request.getStock(),
                request.getCategory()
        );

        return product;
    }

    // 삭제 - Product 반환하도록 변경 (컨트롤러에서 응답 바디로 사용)
    public Product delete(Long id, User user) {
        Product product = getProduct(id);

        if (!product.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("삭제 권한 없음");
        }

        productRepository.delete(product);
        return product;
    }
}