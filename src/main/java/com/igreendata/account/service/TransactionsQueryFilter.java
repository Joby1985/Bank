package com.igreendata.account.service;

import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Joby Job
 *
 */
@Getter
@Setter
@AllArgsConstructor
public class TransactionsQueryFilter {

	private Long accNo;

	private Date startDate;

	private Date endDate;

	private String transType;
}
