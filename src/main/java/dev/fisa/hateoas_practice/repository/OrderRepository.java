package dev.fisa.hateoas_practice.repository;

import dev.fisa.hateoas_practice.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
