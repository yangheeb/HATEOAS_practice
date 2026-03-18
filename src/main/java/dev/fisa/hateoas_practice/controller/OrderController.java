package dev.fisa.hateoas_practice.controller;

import dev.fisa.hateoas_practice.dto.OrderCreateRequest;
import dev.fisa.hateoas_practice.dto.OrderModel;
import dev.fisa.hateoas_practice.model.Order;
import dev.fisa.hateoas_practice.model.User;
import dev.fisa.hateoas_practice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 주문 생성
    @PostMapping
    public ResponseEntity<OrderModel> createOrder(
            @RequestBody @Valid OrderCreateRequest request,
            @AuthenticationPrincipal User user
    ) {
        Order order = orderService.createOrder(request, user);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(OrderModel.ofCreated(order));
    }

    // 주문 조회
    @GetMapping("/{id}")
    public ResponseEntity<OrderModel> getOrder(
            @PathVariable Long id,
            @AuthenticationPrincipal User user
    ) {
        Order order = orderService.getOrder(id, user);
        return ResponseEntity.ok(OrderModel.ofDetail(order));
    }
}