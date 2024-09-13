package com.example.eureka.client.order.global.exception.company;

import com.example.eureka.client.order.global.exception.domainErrorCode.CompanyErrorCode;
import com.example.eureka.client.order.global.exception.dto.CustomException;
import com.example.eureka.client.order.global.exception.dto.ErrorCode;

public class CompanyNotFoundException extends CustomException {

    public CompanyNotFoundException(){
        super(CompanyErrorCode.COMPANY_NOT_FOUND);
    }
}
