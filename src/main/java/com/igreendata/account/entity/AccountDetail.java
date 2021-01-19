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
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "accountdetails")
@Getter
@Setter
@SequenceGenerator(name = "seq", initialValue = 111111111, allocationSize = 50)
public class AccountDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq")
    @Column(name = "ACCNO")
    private long accno;

    @Column(name = "ACCNAME")
    private String accname;

    @ManyToOne
    @JoinColumn(name = "ACCTYPE", insertable = false, updatable = false)
    private AccountType acctype;

    // Home/Base currency
    @ManyToOne
    @JoinColumn(name = "CURRENCY", insertable = false, updatable = false)
    private Currency currency;

    private BigDecimal balance;

    // @OneToMany
    @OneToMany(mappedBy = "accountDetails", cascade = CascadeType.ALL)
    private List<AccountTransaction> transactions;
}
