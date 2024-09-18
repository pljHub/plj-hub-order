### 1. 주문 업체의 COMPANY_MANAGER가 주문 생성

* `createOrder`

* 상태

    * 주문

        * OrderStatus : `Pending`

* 검증

    * 주문 받을 업체 Id와 상품 Id가 존재하는지 검증

* 주문 생성 요청 시 파라미터

```json
{
  "주문 상품 목록":
  [
  	{
      "상품1 ID": "",
      "상품1 수량": ""
    },
  	{
      "상품2 ID": "",
			"상품2 수량": ""
    }
  ],
  "주문 받을 업체 ID": "id"
}
```

* 주문 생성



### 2. 주문 받은 업체의 COMPANY_MANAGER가 주문 수락 (OrderStatus: Pending -> Accepted)

* `acceptOrder`

* 상태

    * 주문
        * OrderStatus: `Pending` -> `Accepted`



* 검증
    * currentUser의 companyId가 주문 내역에 있는 공급 companyId와 같은지 검증 (주문 수락 유저가 실제 주문 받은 회사의 담당자인지 검증)
    * ProductOrder 부분에 해당 Product 의 stock 검증 및 재고 감소 처리
      ```java
      currentUser.companyId == order.
      ```



* 주문 수락 시 파라미터

```json
{
  "주문ID": "id"
}
```

### 3. HubManager가 배송 생성 (OrderStatus: Accepted  || DeliveryStatus: Pending)

* `createDelivery`

* 상태

    * 주문
        * OrderStatus: `Accepted` (그대로)
    * 배송
        * DliveryStatus : `Pending`

* 검증

    * currentUser(HubManager)의 hubId와 Order의 공급 Company의 hubId가 같은지 비교

      ```java
      currentUser.hubId == (order.supplyCompanyId).get().getHubId()
      ```

    * 허브 매니저는 본인 허브와 연결된 업체의 모든 주문 내역 중 OrderStatus가 Accepted 인 주문 내역 중 Delivery가 없는 Order에 CompanyDeliveryUser를 선택해서 delivery를 생성
    * 배송 생성은 Order가 Accepted이고 Delivery가 없는 Order에만 가능

      ```java
      order.OrderStatus == Accepted && order.deliveryId == null
      ```

* 즉, 업체에서 주문 수락이 되면 주문 받은 업체쪽의 허브 매니저가 담당 배송 기사 배정과 배송 생성

    * 배송 생성 : HubManager(주문 받은 업체 쪽)



* 생성된 배송Id를 Order에 넘겨주며 Order 상태 변경() -> CompanyToHubDeliveryId 배정 

* 배송 생성 시 파라미터

  ```json
  {
    "orderId": "id",
    "deliveryUserId": "id"
  }
  ```

### 4 - 1. CompanyDeliveryUser가 배송 수락 (OrderStatus: Accepted -> In_Transit || DeliveryStatus: IN_TRANSIT_TO_HUB)

* `accpetDelivery`

* 상태
    * 주문
        * OrderStatus: `Accepted` -> `ORDER_IN_TRANSIT`
    * 배송
        * DeliveryStatus: `Pending` -> `IN_TRANSIT_TO_HUB`

* CompanyDeliveryUser는 본인이 할당받은 배송을 수락 Or 거절 할 수 있음

* 검증

    * CurrentUser(CompanyDeliveryUser)가 delivery의 deliveryUser와 같은지 검증 (지정 받은 배송 담당자만 해당 배송을 수락 or 거절 가능)

      ```java
      currentUserId == delivery.companyToHubDeliveryUserId
      ```

* 배송 수락

* 배송 수락 시 파라미터

  ```json
  {
    "deliveryId" "id"
  }
  ```



### 4-2. CompanyDeliveryUser가 배송 거절 (OrderStatus:Accepted -> Accepted || DeliveryStatus: Pending -> Rejected)

* rejectDelivery - 구현 보류 
* 상태
    * 주문
        * OrderStatus: `Accepted` -> `Accepted`  (그대로)
    * 배송
        * DeliveryStatus: `IN_TRANSIT_TO_HUB` -> `Rejected`

* 취소된 배송은 소프트 삭제 처리
* delivery.isDeleted -> true

이후 다시 3. HubManager가 배송 생성 (OrderStatus: Accepted  || DeliveryStatus: Pending) 부터 다시 진행 가능.

위와 동일하게 가며 OrderStatus는 최종 수요 Company에 도착했을 때 Completed로 수정하고 나머지는 그대로, deliveryStatus만 변경하며 과정 진행

### 5. CompanyDeliveryUser가 출발 허브에 도착 완료
* arriveStartHub
* 상태
    * 주문
        * OrderStatus: `ORDER_IN_TRANSIT` -> `ORDER_IN_TRANSIT`  (그대로)
    * 배송
        * DeliveryStatus: `IN_TRANSIT_TO_HUB` -> `ARRIVED_AT_START_HUB`
### 6. 출발지 HubManager가 HubDeliveryUser 배정
* assignHubDeliveryUser
* 
* deliveryStatus가 출발 허브에 도착 완료 상태 && delivery.hubDeliveryUserId == null

### 7. HubDeliveryUser 가 배송 수락 또는 거절(보류)
* acceptHubDeliveryUser
* 상태
    * 주문
        * OrderStatus: `ORDER_IN_TRANSIT` -> `ORDER_IN_TRANSIT`  (그대로)
    * 배송
        * DeliveryStatus: `ARRIVED_AT_START_HUB` -> `ARRIVED_AT_START_HUB`
### 8. HubDeliveryUser가 도착지 Hub에 도착 완료
* 
* 상태
    * 주문
        * OrderStatus: `Accepted` -> `Accepted`  (그대로)
    * 배송
        * DeliveryStatus: `Pending` -> `Rejected`
### 9. 도착지 HubManager가 CompanyDeliveryUser 배정

* deliveryStatus가 도착 허브에 도착 완료 상태 && delivery.HubToCompanyDeliveryUserId == null
* 상태
    * 주문
        * OrderStatus: `Accepted` -> `Accepted`  (그대로)
    * 배송
        * DeliveryStatus: `Pending` -> `Rejected`
### 10. CompanyDeliveryUser가 배송 수락 또는 거절
* 상태
    * 주문
        * OrderStatus: `Accepted` -> `Accepted`  (그대로)
    * 배송
        * DeliveryStatus: `Pending` -> `Rejected`
### 11. CompanyDeliveryUser가 배송 완료
* 상태
    * 주문
        * OrderStatus: `Accepted` -> `ORDER_COMPLETED`  (그대로)
    * 배송
        * DeliveryStatus: `Pending` -> `DELIVERED_TO_RECIPIENT`

### test 
* Company Manager 주문 생성
* Company Manager 주문 수락
* HumManager 배달 생성 
* CompanyToHubUser 생성
  {
  "status": "success",
  "message": "CREATED",
  "data": {
  "userId": 1,
  "username": "inhee111",
  "role": "HUB_MANAGER"
  }
  }
* {
  "status": "success",
  "message": "CREATED",
  "data": {
  "userId": 2,
  "username": "inhee222",
  "role": "COMPANY_MANAGER"
  }
  }
* {
  "status": "success",
  "message": "CREATED",
  "data": {
  "userId": 3,
  "username": "inhee333",
  "role": "COMPANY_DELIVERY_USER"
  }
  }