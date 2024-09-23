package com.example.eureka.client.order.infrastructure.repository;

import static com.example.eureka.client.order.domain.model.QDelivery.delivery;
import static com.example.eureka.client.order.domain.model.QDeliveryRecord.deliveryRecord;
import static com.example.eureka.client.order.domain.model.QOrder.order;

import com.example.eureka.client.order.application.dto.GetDeliveryRecordResponseDto;
import com.example.eureka.client.order.application.dto.GetDeliveryResponseDto;
import com.example.eureka.client.order.domain.model.Delivery;
import com.example.eureka.client.order.domain.model.DeliveryRecord;
import com.example.eureka.client.order.domain.model.DeliveryRecordStatus;
import com.example.eureka.client.order.domain.model.DeliveryStatus;
import com.example.eureka.client.order.domain.repository.DeliveryRecordRepositoryCustom;
import com.example.eureka.client.order.global.exception.order.OrderAccessDeniedException;
import com.example.eureka.client.order.presentation.request.DeliveryRecordSearchDto;
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
public class DeliveryRecordRepositoryImpl implements DeliveryRecordRepositoryCustom {

    private final JPAQueryFactory queryFactory;


    @Override
    public Page<GetDeliveryRecordResponseDto> searchDeliveriesRecords(
        DeliveryRecordSearchDto searchDto, Pageable pageable, String role, Long userId) {

        List<DeliveryRecord> records = queryFactory
            .selectFrom(deliveryRecord)
            .where(
                startHubEq(searchDto.getStartHubId()),
                destHubEq(searchDto.getDestHubId()),
                statusEq(DeliveryRecordStatus.valueOf(searchDto.getStatus()))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getAllDeliverySpecifiers(pageable))
            .fetch();

        JPAQuery<Long> countQuery = queryFactory
            .select(deliveryRecord.count())
            .where(
                startHubEq(searchDto.getStartHubId()),
                destHubEq(searchDto.getDestHubId()),
                statusEq(DeliveryRecordStatus.valueOf(searchDto.getStatus()))
            );

        Page<DeliveryRecord> page = PageableExecutionUtils.getPage(records, pageable, countQuery::fetchOne);

        log.info("주문 조회 성공 userId: {}, recordId: {}", userId, searchDto.getRecordId());

        return page.map(GetDeliveryRecordResponseDto::new);
    }

    private BooleanExpression startHubEq(UUID startHubId) {
        return startHubId != null ? deliveryRecord.startHubId.eq(startHubId): null;
    }

    private BooleanExpression destHubEq(UUID destHubId) {
        return destHubId != null ? deliveryRecord.destHubId.eq(destHubId) : null;

    }

    private BooleanExpression statusEq(DeliveryRecordStatus status) {
        return status != null ? deliveryRecord.status.eq(status) : null;
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
                    deliveryRecord.createdAt
                );
            case "updatedAt":
                return new OrderSpecifier<>(
                    sortOrder.isAscending() ? com.querydsl.core.types.Order.ASC : com.querydsl.core.types.Order.DESC,
                    deliveryRecord.updatedAt
                );
            // 다른 필드에 대한 정렬 조건 추가
            default:
                throw new OrderAccessDeniedException();
        }
    }
}
