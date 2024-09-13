package com.example.eureka.client.order.infrastructure.client.product;

import java.util.UUID;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CompanyResponseDTO {

    private UUID companyId;
    private String name;
    private CompanyTypeEnum type;
    private UUID hubId;
    private String address;


}
