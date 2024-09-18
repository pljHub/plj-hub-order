package com.example.eureka.client.order.domain.repository;

import static com.example.eureka.client.order.domain.model.QOrder.order;

import com.example.eureka.client.order.application.dto.GetOrderResponseDto;
import com.example.eureka.client.order.application.dto.OrderSearchRequestTimeDto;
import com.example.eureka.client.order.domain.model.Order;
import com.example.eureka.client.order.domain.model.OrderStatus;
import com.example.eureka.client.order.domain.model.QOrder;
import com.example.eureka.client.order.domain.model.QProductOrder;
import com.example.eureka.client.order.global.exception.order.OrderAccessDeniedException;
import com.example.eureka.client.order.presentation.request.OrderSearchDto;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.nio.file.AccessDeniedException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.support.PageableExecutionUtils;

@RequiredArgsConstructor
@Slf4j
public class OrderRepositoryImpl implements OrderRepositoryCustom{

    private final JPAQueryFactory queryFactory;

    QProductOrder productOrder = QProductOrder.productOrder;

    @Override
    public Page<GetOrderResponseDto> searchOrders(OrderSearchDto searchDto, Pageable pageable,
        String role, Long userId) {
        /*
            ProductOrder Quantity 를 기준으로 가져오려면 별도의 method 필요 (innerJoin)
         */
        List<Order> orders = queryFactory
            .selectFrom(order)
            .leftJoin(order.productOrderList, productOrder)
            .where(
                // null 값 무시되지 않도록 하려며 and 연산자 필요
//                supplyEq(searchDto.getSupplyId())
//                    .and(consumerEq(searchDto.getConsumerId()))

                supplyEq(searchDto.getSupplyId()),
                consumerEq(searchDto.getConsumerId()),
                statusEq(OrderStatus.valueOf(searchDto.getOrderStatus())),
                productQuantityGreaterThanOrEqual(searchDto.getQuantity())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getAllOrderSpecifiers(pageable))
            .fetch();
        JPAQuery<Long> countQuery = queryFactory
            .select(order.count())
            .where(
                supplyEq(searchDto.getSupplyId()),
                consumerEq(searchDto.getConsumerId()),
                statusEq(OrderStatus.valueOf(searchDto.getOrderStatus()))
            );

        Page<Order> page = PageableExecutionUtils.getPage(orders, pageable, countQuery::fetchOne);

        log.info("주문 조회 성공 userId: {}, orderId: {}", userId, searchDto.getOrderId());

        return page.map(GetOrderResponseDto::new);
    }

    @Override
    public Page<GetOrderResponseDto> searchOrdersByRequestTime(OrderSearchRequestTimeDto searchDto,
        Pageable pageable) {

        Integer requestTime = searchDto.getRequestTime();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime startOfYesterday = now.minusDays(1)
            .withHour(requestTime)
            .withMinute(0)
            .withSecond(0);

        List<Order> orders = queryFactory
            .selectFrom(order)
            .leftJoin(order.productOrderList, productOrder)
            .where(
                order.createdAt.between(startOfYesterday, now)
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getAllOrderSpecifiers(pageable))
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(order.count())
            .where(
                order.createdAt.between(startOfYesterday, now)
            );

        Page<Order> page = PageableExecutionUtils.getPage(orders,pageable, countQuery::fetchOne);
        log.info("주문 조회 성공 (시간 범위 기준) requestTime: {}",searchDto.getRequestTime());

        return page.map(GetOrderResponseDto::new);
    }

    private BooleanExpression productQuantityGreaterThanOrEqual(Long quantity) {
        return quantity != null ? QProductOrder.productOrder.quantity.goe(quantity) : null;
    }

    // supplierId에 대한 조건
    private BooleanExpression supplyEq(UUID supplyId) {
        return supplyId != null ? order.supplierId.eq(supplyId) : null;
    }

    // consumerId에 대한 조건
    private BooleanExpression consumerEq(UUID consumerId) {
        return consumerId != null ? order.consumerId.eq(consumerId) : null;
    }

    private BooleanExpression statusEq(OrderStatus status) {
        return status != null ? order.status.eq(status) : null;
    }

    private OrderSpecifier<?> getAllOrderSpecifiers(Pageable pageable){
        if (pageable.getSort().isEmpty()){
            return null;
        }

        Sort.Order sortOrder = pageable.getSort().iterator().next();

        switch (sortOrder.getProperty()){
            case "createdAt":
                return new OrderSpecifier<>(
                    sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                    order.createdAt
                );
            case "updatedAt":
                return new OrderSpecifier<>(
                    sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                    order.updatedAt
                );
            // 다른 필드에 대한 정렬 조건 추가
            default:
                throw new OrderAccessDeniedException();
        }
    }


}
