package com.igreendata.account.value;

import java.math.BigDecimal;

import lombok.Getter;

/**
 *
 * @author Joby Job
 *
 */
@Getter
public class AccountValueObject {
	private Long accno;

	private String accname;

	private String acctype;

	// Home/Base currency
	private String currency;

	private BigDecimal balance;
}
