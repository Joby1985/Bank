package com.igreendata.account.entity;

import java.math.BigDecimal;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Joby Job
 *
 */
@Entity
@Table(name = "ACCOUNTTRANSACTIONS")
@Getter
@Setter
public class AccountTransaction {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@ManyToOne
	@JoinColumn(name = "ACCNO", insertable = true, updatable = false)
	private Account accountDetails;

	@Column(name = "TRANSDATE", updatable = false)
	private Date transDate;

	@ManyToOne
	@JoinColumn(name = "CURRENCY", insertable = true, updatable = false)
	private Currency currency;

	@Column(name = "TRANSAMT", updatable = false)
	private BigDecimal transAmt;
}
