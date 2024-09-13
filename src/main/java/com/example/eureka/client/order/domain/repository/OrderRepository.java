package com.example.eureka.client.order.domain.repository;

import com.example.eureka.client.order.domain.model.Order;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, UUID>, OrderRepositoryCustom{

    Order findByDeliveryId(UUID deliveryId);

    Optional<Order> findByIdAndDeletedAtIsNull(UUID orderId);
}
