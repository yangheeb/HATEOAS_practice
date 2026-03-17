package dev.fisa.hateoas_practice.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private int quantity;

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    // 주문자
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name= "user_id")
    private User user;

    // 상품
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    // 생성 메서드
    public static Order create(User user, Product product, int quantity){
        Order order = new Order();
        order.user = user;
        order.product = product;
        order.quantity = quantity;
        order.status = OrderStatus.ORDERED;

        product.decreaseStock(quantity);  // 비즈니스 로직을 Entity에 두었음!

        return order;
    }

}
