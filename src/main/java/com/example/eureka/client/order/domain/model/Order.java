package com.example.eureka.client.order.domain.model;


import com.example.eureka.client.order.presentation.request.OrderRequest;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PreRemove;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import javax.swing.TransferHandler;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;

@Setter // unit test
@Getter
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PROTECTED)
//@Builder(access = AccessLevel.PRIVATE)
@Builder // unit test
@Entity
@Table(name = "p_order")
@SQLDelete(sql = "UPDATE p_order SET deleted_at = CURRENT_TIMESTAMP, is_deleted = true WHERE order_id = ?")
public class Order extends Auditing{

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "order_id")
    private UUID id;

    @Column(name = "supplier_id", nullable = false)
    private UUID supplierId; // 생산업체

    @Column(name = "consumer_id", nullable = false)
    private UUID consumerId; // 수령업체

    @Column(name = "user_id", nullable = false)
    private Long userId; // companyMangerId

    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private OrderStatus status;

    @Column(name = "total_price", nullable = false)
    private Long totalPrice;

    @Builder.Default
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<ProductOrder> productOrderList = new ArrayList<>();

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "delivery_id")
    private Delivery delivery;

    @PreRemove
    public void softDelete() {
        this.setDeletedAt(LocalDateTime.now());
        this.setIsDeleted(true);
        if (this.delivery != null) {
            this.delivery.setDeletedAt(LocalDateTime.now());
            this.delivery.setIsDeleted(true);
        }

        if (this.productOrderList != null) {
            for (ProductOrder productOrder : productOrderList) {
                productOrder.setDeletedAt(LocalDateTime.now());
                productOrder.setIsDeleted(true);
            }
        }
    }

    public static Order createOrder(OrderRequest request, Long companyMangerId) {
        return Order.builder()
            .supplierId(request.getSupplyId())
            .consumerId(request.getConsumerId())
            .status(OrderStatus.ORDER_CREATED)
            .userId(companyMangerId) // 생산업체 ID
            .totalPrice(0L)
            .build();
    }

    public void addProductOrder(ProductOrder productOrder) {
        productOrderList.add(productOrder);
        productOrder.setOrder(this);
    }

    public void addDelivery(Delivery delivery) {
        this.delivery = delivery;
    }

    public void updateTotalPrice(Long totalPrice) {
        this.totalPrice = totalPrice;
    }

    public void acceptOrder() {
        this.status = OrderStatus.ORDER_ACCEPTED;
    }

    public void completeOrder() {
        this.status = OrderStatus.ORDER_COMPLETED;
    }

    public void rejectOrder() {
        this.status = OrderStatus.ORDER_REJECTED;
    }

    public void cancelOrder() {
        this.status = OrderStatus.ORDER_CANCELED;
    }

    public void processRefundFromDelivery() {
        this.delivery.processRefundDelivery();
        this.status = OrderStatus.ORDER_REFUND_FROM_DELIVERY;
    }

    public void transitOrder() {
        this.status = OrderStatus.ORDER_IN_TRANSIT;
    }
}
