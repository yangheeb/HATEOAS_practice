package dev.fisa.hateoas_practice.service;

import dev.fisa.hateoas_practice.model.Order;
import dev.fisa.hateoas_practice.model.Product;
import dev.fisa.hateoas_practice.model.User;
import dev.fisa.hateoas_practice.repository.OrderRepository;
import dev.fisa.hateoas_practice.repository.ProductRepository;
import dev.fisa.hateoas_practice.dto.OrderCreateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    // 주문 생성
    public Order createOrder(OrderCreateRequest request, User user) {

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new IllegalArgumentException("상품 없음"));

        Order order = Order.create(user, product, request.getQuantity());

        return orderRepository.save(order);
    }

    // 주문 조회 - 본인 주문만 조회 가능
    @Transactional(readOnly = true)
    public Order getOrder(Long id, User user) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("주문 없음"));

        // 403 체크
        if (!order.getUser().getId().equals(user.getId())) {
            throw new IllegalArgumentException("조회 권한 없음");
        }

        return order;
    }
}