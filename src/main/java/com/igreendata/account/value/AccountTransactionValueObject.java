package com.igreendata.account.value;

import java.math.BigDecimal;

import lombok.Getter;

@Getter
public class AccountTransactionValueObject {

    private long accNo;

    private String currency;

    private BigDecimal transValue;

    private String transactionType;
}
