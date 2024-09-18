package com.example.eureka.client.order.global.util;

import com.example.eureka.client.order.domain.model.Delivery;
import com.example.eureka.client.order.global.exception.company.CompanyNotFoundException;
import com.example.eureka.client.order.global.exception.delivery.DeliveryNotFoundException;
import com.example.eureka.client.order.infrastructure.client.product.CompanyResponseDTO;
import com.example.eureka.client.order.infrastructure.client.user.GetUserResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class PermissionUtil {

    // 역할과 Hub ID를 검증하는 메서드
    public void validateUserOrHubIdPermission(String role, GetUserResponseDto userData, CompanyResponseDTO companyData, Long userId) {
        // 관리자 또는 허브 관리자일 경우 바로 통과
        if (!isAdminOrHubManager(role)) {
            // 일반 사용자인 경우 Hub ID 검증
            if (!userData.getHubId().equals(companyData.getHubId())) {
                log.warn("주문의 공급업체와 CurrentUser 의 업체 ID 불일치로 주문 수락 요청 실패 loginUserId = {}", userId);
                throw new CompanyNotFoundException();
            }
        }
    }

    public void validateUserOrCompanyToHubDeliveryUserPermission(String role, Delivery delivery, Long userId) {
        // 관리자 또는 허브 관리자일 경우 바로 통과
        if(!isAdminOrHubManager(role)) {
            if (!delivery.getCompanyToHubDeliveryUserId().equals(userId)) {
                log.warn("업체 배송 담당자와 할당된 배달의 업체 배송 담당자와 ID 가 일치하지 않습니다. loginUserId = {}", userId);
                throw new DeliveryNotFoundException();
            }
        }
    }

    public void validateUserOrHubDeliveryUserPermission(String role, Delivery delivery, Long userId) {
        // 관리자 또는 허브 관리자일 경우 바로 통과
        if(!isAdminOrHubManager(role)) {
            if (!delivery.getHubDeliveryUserId().equals(userId)) {
                log.warn("업체 배송 담당자와 할당된 배달의 업체 배송 담당자와 ID 가 일치하지 않습니다. loginUserId = {}", userId);
                throw new DeliveryNotFoundException();
            }
        }
    }

    public void validateUserOrHubToCompanyDeliveryUserPermission(String role, Delivery delivery, Long userId) {
        // 관리자 또는 허브 관리자일 경우 바로 통과
        if(!isAdminOrHubManager(role)) {
            if (!delivery.getHubToCompanyDeliveryUserId().equals(userId)) {
                log.warn("업체 배송 담당자와 할당된 배달의 업체 배송 담당자와 ID 가 일치하지 않습니다. loginUserId = {}", userId);
                throw new DeliveryNotFoundException();
            }
        }
    }

    public boolean isAdminOrHubManager(String role) {
        return "ADMIN".equals(role) || "HUB_MANAGER".equals(role);
    }



}
