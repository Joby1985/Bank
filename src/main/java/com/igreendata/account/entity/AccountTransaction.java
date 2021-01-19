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

@Entity
@Table(name = "accounttransactions")
@Getter
@Setter
public class AccountTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    // @ManyToOne
    // @JoinColumn(name = "ACCNO")
    @ManyToOne
    @JoinColumn(name = "ACCNO", insertable = false, updatable = false)
    private AccountDetail accountDetails;

    @Column(name = "TRANSDATE")
    private Date transDate;

    @ManyToOne
    @JoinColumn(name = "CURRENCY", insertable = false, updatable = false)
    private Currency currency;

    @Column(name = "TRANSVALUE")
    private BigDecimal transValue;
}
