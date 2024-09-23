package com.example.eureka.client.order.application.service;

import com.example.eureka.client.order.domain.model.Order;
import com.example.eureka.client.order.domain.model.OrderStatus;
import com.example.eureka.client.order.domain.repository.OrderRepository;
import com.example.eureka.client.order.global.util.PermissionUtil;
import com.example.eureka.client.order.infrastructure.client.product.CompanyResponseDTO;
import com.example.eureka.client.order.infrastructure.client.product.HubPathSequenceDTO;
import com.example.eureka.client.order.infrastructure.client.product.ProductClient;
import com.example.eureka.client.order.infrastructure.client.product.ProductResponseDto;
import com.example.eureka.client.order.infrastructure.client.user.GetUserResponseDto;
import com.example.eureka.client.order.infrastructure.client.user.UserClient;
import com.example.eureka.client.order.presentation.request.OrderRequest;
import com.example.eureka.client.order.presentation.request.ProductDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@Slf4j(topic = "OrderServiceTests")
class OrderServiceImplTest {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private ProductClient productClient;

    @Mock
    private UserClient userClient;

    @Mock
    private PermissionUtil permissionUtil;

    @InjectMocks
    private OrderServiceImpl orderServiceImpl;

    private Order mockOrder;
    private final Long userId = 1L;
    private final String role = "ADMIN";
    private OrderRequest mockOrderRequest;
    private ProductResponseDto productResponseDto;
    private CompanyResponseDTO companyResponseDTO;
    private GetUserResponseDto getUserResponseDto;
    private List<HubPathSequenceDTO> hubPathSequenceDTOList;

    @BeforeEach
    void setUp(){
        mockOrder = Order.builder()
            .id(UUID.randomUUID())  // UUID 생성
            .supplierId(UUID.randomUUID())
            .consumerId(UUID.randomUUID())
            .userId(1L)
            .status(OrderStatus.ORDER_CREATED)
            .totalPrice(10000L)
            .build();

        // Mock ProductDetails 생성
        Map<UUID, ProductDetails> productMap = new HashMap<>();
        UUID productId = UUID.randomUUID();
        productMap.put(productId, new ProductDetails(5L, 2000L)); // 예시: 수량 5, 가격 2000

        // Mock OrderRequest 생성
        mockOrderRequest = new OrderRequest();
        mockOrderRequest.setSupplyId(mockOrder.getSupplierId());
        mockOrderRequest.setConsumerId(mockOrder.getConsumerId());
        mockOrderRequest.setCompanyId(UUID.randomUUID());

        // Mock ProductResponseDto 생성
        productResponseDto = ProductResponseDto.builder()
            .productId(productId)
            .name("Sample Product")
            .companyId(UUID.randomUUID())
            .hubId(UUID.randomUUID())
            .price(2000)
            .stock(100)
            .build();

        // Mock CompanyResponseDTO 생성
        companyResponseDTO = CompanyResponseDTO.builder()
            .companyId(UUID.randomUUID())
            .name("Sample Company")
            .type(null)  // 실제 사용 시 적절한 ENUM 값 사용
            .hubId(UUID.randomUUID())
            .address("Sample Address")
            .build();

        // Mock GetUserResponseDto 생성
        getUserResponseDto = new GetUserResponseDto(
            userId, "admin", role, "slackId", UUID.randomUUID(), UUID.randomUUID(), null, null);

        // Mock HubPathSequenceDTO List 생성
        hubPathSequenceDTOList = new ArrayList<>();
        hubPathSequenceDTOList.add(HubPathSequenceDTO.builder()
            .startHubId(UUID.randomUUID())
            .destHubId(UUID.randomUUID())
            .sequence(1)
            .routePath("Path1 -> Path2")
            .build()
        );
    }

    @Test
    void createOrder() {

    }

    @Test
    void acceptOrder() {
    }

    @Test
    void deleteOrder() {
    }

    @Test
    void getOrder() {
    }

    @Test
    void getOrders() {
    }

    @Test
    void getOrdersByRequestTime() {
    }
}