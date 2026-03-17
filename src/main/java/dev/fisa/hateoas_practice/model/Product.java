package dev.fisa.hateoas_practice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private String category;

    // 판매자
    @ManyToOne(fetch = FetchType.LAZY)  // 필요한 시점에만 조회
    @JoinColumn(name="user_id")
    private User user;

    // 주문
    @OneToMany(mappedBy = "product")
    private List<Order> orders = new ArrayList<>();

    // 생성 메서드
    public static Product create(String name, String description, int price, int stock, String category, User user) {
        Product product = new Product();
        product.name = name;
        product.description = description;
        product.price = price;
        product.stock = stock;
        product.category = category;

        product.setUser(user);
        return product;
    }


    // 양방향 연관관계 편의 메서드
    public void setUser(User user) {
        this.user = user;
        user.getProducts().add(this);
    }

    // 재고 감소
    public void decreaseStock(int quantity){
        if(this.stock < quantity) {
            throw new IllegalArgumentException("재고 부족");
        }
        this.stock -= quantity;
    }

    // 상품 수정
    public void update(String name, String description, int price, int stock, String category) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }
}
