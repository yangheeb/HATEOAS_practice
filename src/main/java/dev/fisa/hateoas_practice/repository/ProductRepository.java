package dev.fisa.hateoas_practice.repository;

import dev.fisa.hateoas_practice.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

// CRUD + 페이지 처리를 수행하려면? -> JpaRepository 상속
public interface ProductRepository extends JpaRepository<Product,Long> {
    Page<Product> findByCategory(String category, Pageable pageable);
}
