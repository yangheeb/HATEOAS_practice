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
public class User {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    // 상품 등록
    @OneToMany(mappedBy = "user")
    private List<Product> products = new ArrayList<>();

    // 주문
    @OneToMany(mappedBy = "user")
    private List<Order> orders = new ArrayList<>();

}
