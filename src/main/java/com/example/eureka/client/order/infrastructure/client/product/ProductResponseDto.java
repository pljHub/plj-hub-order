package com.example.eureka.client.order.infrastructure.client.product;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ProductResponseDto {
    private UUID id;
    private String name;
    private Long price;
    private Long stock;

    @Override
    public String toString() {
        return "ProductResponseDto{" +
            "id=" + id +
            ", name='" + name + '\'' +
            ", price=" + price +
            ", stock=" + stock +
            '}';
    }
}
