package dev.fisa.hateoas_practice.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import dev.fisa.hateoas_practice.controller.OrderController;
import dev.fisa.hateoas_practice.controller.ProductController;
import dev.fisa.hateoas_practice.model.Product;
import dev.fisa.hateoas_practice.model.User;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Getter
@JsonIgnoreProperties("links")
public class ProductModel extends RepresentationModel<ProductModel> {

    private final Long id;
    private final String name;
    private final String description;
    private final int price;
    private final int stock;
    private final String category;
    private final Long userId;

    public ProductModel(Product product) {
        this.id = product.getId();
        this.name = product.getName();
        this.description = product.getDescription();
        this.price = product.getPrice();
        this.stock = product.getStock();
        this.category = product.getCategory();
        this.userId = product.getUser() != null ? product.getUser().getId() : null;
    }

    // 상품 등록 / 수정 후 링크 (self, profile, list, update, delete)
    public static ProductModel ofCreatedOrUpdated(Product product) {
        ProductModel model = new ProductModel(product);
        Long id = product.getId();

        model.add(linkTo(methodOn(ProductController.class).getProduct(id,null)).withSelfRel());
        model.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        model.add(Link.of("/api/products?page=0&size=10{&category}")
                .withRel("list-products").withType("GET"));
        model.add(linkTo(methodOn(ProductController.class)
                .update(id, null, null)).withRel("update-product").withType("PUT"));
        model.add(linkTo(methodOn(ProductController.class)
                .delete(id, null)).withRel("delete-product").withType("DELETE"));

        return model;
    }

    // 상품 상세 조회 링크 (로그인 유저 + 본인 상품 여부에 따라 링크 다름)
    public static ProductModel ofDetail(Product product, User currentUser) {
        ProductModel model = new ProductModel(product);
        Long id = product.getId();

        model.add(linkTo(methodOn(ProductController.class).getProduct(id,null)).withSelfRel());
        model.add(Link.of("/swagger-ui/index.html").withRel("profile"));

        // 인증된 유저이고 재고가 있으면 order 링크 추가
        if (currentUser != null && product.getStock() > 0) {
            model.add(Link.of("/api/orders").withRel("order").withType("POST"));
        }

        // 본인 상품이면 update, delete 링크 추가
        if (currentUser != null && product.getUser().getId().equals(currentUser.getId())) {
            model.add(linkTo(methodOn(ProductController.class)
                    .update(id, null, null)).withRel("update-product").withType("PUT"));
            model.add(linkTo(methodOn(ProductController.class)
                    .delete(id, null)).withRel("delete-product").withType("DELETE"));
        }

        return model;
    }

    // 상품 삭제 후 링크 (self, profile, list)
    public static ProductModel ofDeleted(Product product) {
        ProductModel model = new ProductModel(product);
        Long id = product.getId();

        model.add(linkTo(methodOn(ProductController.class).getProduct(id,null)).withSelfRel());
        model.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        model.add(Link.of("/api/products?page=0&size=10{&category}")
                .withRel("list-products").withType("GET"));

        return model;
    }
}