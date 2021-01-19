package com.igreendata.account.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Parameter;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author Joby Job
 *
 */
@Entity
@Table(name = "ACCOUNT")
@Getter
@Setter
public class Account {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY, generator = "seq")
	@GenericGenerator(name = "seq", strategy = "increment", parameters = {
			@Parameter(name = "initial_value", value = "111111111") })
	@Column(name = "ACCNO")
	private Long accno;

	@Column(name = "ACCNAME")
	private String accname;

	@ManyToOne
	@JoinColumn(name = "ACCTYPE", insertable = true, updatable = true)
	private AccountType acctype;

	// Home/Base currency
	@ManyToOne
	@JoinColumn(name = "CURRENCY", insertable = true, updatable = true)
	private Currency currency;

	private BigDecimal balance;

	// @OneToMany
	@OneToMany(mappedBy = "accountDetails", cascade = CascadeType.ALL)
	private List<AccountTransaction> transactions;
}
