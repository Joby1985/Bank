package com.igreendata.account.value;

import java.math.BigDecimal;

import lombok.Getter;

/**
 *
 * @author Joby Job
 *
 */
@Getter
public class AccountTransactionValueObject {

	private Long accNo;

	private String currency;

	private BigDecimal transAmount;

	private String transType;
}
