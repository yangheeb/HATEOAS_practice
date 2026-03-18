package dev.fisa.hateoas_practice.dto;

import dev.fisa.hateoas_practice.controller.OrderController;
import dev.fisa.hateoas_practice.controller.ProductController;
import dev.fisa.hateoas_practice.model.Order;
import lombok.Getter;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.RepresentationModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Getter
public class OrderModel extends RepresentationModel<OrderModel> {

    private final Long orderId;
    private final Long productId;
    private final int quantity;
    private final String status;

    public OrderModel(Order order) {
        this.orderId = order.getId();
        this.productId = order.getProduct().getId();
        this.quantity = order.getQuantity();
        this.status = order.getStatus().name();
    }

    // 주문 생성 후 링크
    public static OrderModel ofCreated(Order order) {
        OrderModel model = new OrderModel(order);
        Long orderId = order.getId();
        Long productId = order.getProduct().getId();

        model.add(linkTo(methodOn(OrderController.class)
                .getOrder(orderId, null)).withSelfRel());
        model.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        model.add(linkTo(methodOn(ProductController.class)
                .getProduct(productId,null)).withRel("product"));
        model.add(Link.of("/api/products").withRel("list-products"));

        return model;
    }

    // 주문 조회 링크
    public static OrderModel ofDetail(Order order) {
        OrderModel model = new OrderModel(order);
        Long orderId = order.getId();
        Long productId = order.getProduct().getId();

        model.add(linkTo(methodOn(OrderController.class)
                .getOrder(orderId, null)).withSelfRel());
        model.add(Link.of("/swagger-ui/index.html").withRel("profile"));
        model.add(linkTo(methodOn(ProductController.class)
                .getProduct(productId,null)).withRel("product"));
        model.add(Link.of("/api/products").withRel("list-products"));

        return model;
    }
}