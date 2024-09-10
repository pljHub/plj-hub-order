package com.example.eureka.client.order.domain.repository;

import static com.example.eureka.client.order.domain.model.QDelivery.delivery;
import static com.example.eureka.client.order.domain.model.QOrder.order;

import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.domain.model.Delivery;
import com.example.eureka.client.order.domain.model.DeliveryStatus;
import com.example.eureka.client.order.global.exception.order.OrderAccessDeniedException;
import com.example.eureka.client.order.presentation.request.DeliverySearchDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.List;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
@Slf4j
public class DeliveryRepositoryImpl implements DeliveryRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    @Override
    public Page<GetDeliveryResponseDto> searchDeliveries(DeliverySearchDto searchDto,
        Pageable pageable, String role, Long userId) {

        /*
            delivery 를 조회시 @CollectionTable 조회는 분리 필요
         */
        List<Delivery> deliveries = queryFactory
            .selectFrom(delivery)
            .where(
                startHubEq(searchDto.getStartHubId()),
                destHubEq(searchDto.getDestHubId())
//                statusEq(DeliveryStatus.valueOf(searchDto.getStatus()))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getAllDeliverySpecifiers(pageable))
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(delivery.count())
            .where(
                startHubEq(searchDto.getStartHubId()),
                destHubEq(searchDto.getDestHubId())
//                statusEq(DeliveryStatus.valueOf(searchDto.getStatus()))
            );

        Page<Delivery> page = PageableExecutionUtils.getPage(deliveries, pageable, countQuery::fetchOne);

        log.info("주문 조회 성공 userId: {}, deliveryId: {}", userId, searchDto.getDeliveryId());

        return page.map(GetDeliveryResponseDto::new);
    }

    private BooleanExpression startHubEq(UUID startHubId) {
        return startHubId != null ? delivery.startHubIds.any().eq(startHubId) : null;

    }

    private BooleanExpression destHubEq(UUID destHubId) {
        return destHubId != null ? delivery.destHubIds.any().eq(destHubId) : null;

    }

    private BooleanExpression statusEq(DeliveryStatus status) {
        return status != null ? delivery.status.eq(status) : null;
    }

    private OrderSpecifier<?> getAllDeliverySpecifiers(Pageable pageable){
        if (pageable.getSort().isEmpty()){
            return null;
        }

        Sort.Order sortOrder = pageable.getSort().iterator().next();

        switch (sortOrder.getProperty()){
            case "createdAt":
                return new OrderSpecifier<>(
                    sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                    delivery.createdAt
                );
            case "updatedAt":
                return new OrderSpecifier<>(
                    sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                    delivery.updatedAt
                );
            // 다른 필드에 대한 정렬 조건 추가
            default:
                throw new OrderAccessDeniedException();
        }
    }
}
