package com.example.eureka.client.order.infrastructure.client.product;


import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {

    private UUID productId;
    private String name;
    private UUID companyId;
    private UUID hubId;
    private int price;
    private int stock;

    /*
        String.valueOf(new Dto())
     */
    @Override
    public String toString() {
        return "ProductResponseDto{" +
            "productId=" + productId +
            ", name='" + name + '\'' +
            ", companyId=" + companyId +
            ", hubId=" + hubId +
            ", price=" + price +
            ", stock=" + stock +
            '}';
    }
}
