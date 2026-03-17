package dev.fisa.hateoas_practice.controller;

import dev.fisa.hateoas_practice.dto.OrderCreateRequest;
import dev.fisa.hateoas_practice.dto.OrderResponse;
import dev.fisa.hateoas_practice.model.Order;
import dev.fisa.hateoas_practice.model.User;
import dev.fisa.hateoas_practice.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    // 🔥 주문 생성
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(
            @RequestBody @Valid OrderCreateRequest request
    ) {
        User user = new User();

        Order order = orderService.createOrder(request, user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new OrderResponse(
                        order.getId(),
                        order.getProduct().getId(),
                        order.getQuantity(),
                        order.getStatus().name()
                ));
    }

    // 🔥 주문 조회
    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> getOrder(@PathVariable Long id) {

        Order order = orderService.getOrder(id);

        return ResponseEntity.ok(
                new OrderResponse(
                        order.getId(),
                        order.getProduct().getId(),
                        order.getQuantity(),
                        order.getStatus().name()
                )
        );
    }
}