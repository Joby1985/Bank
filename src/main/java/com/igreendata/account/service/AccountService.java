package com.igreendata.account.service;

import java.util.List;

import com.igreendata.account.entity.AccountDetail;
import com.igreendata.account.entity.AccountTransaction;
import com.igreendata.account.exceptions.IGreenDataAccountException;
import com.igreendata.account.value.AccountTransactionValueObject;

public interface AccountService {

    public List<AccountDetail> listAllAccounts();

    public AccountDetail createAccount(AccountDetail acc) throws IGreenDataAccountException;

    public AccountDetail updateAccount(AccountDetail acc) throws IGreenDataAccountException;

    public List<AccountTransaction> listTransactions(TransactionsQueryFilter filter);

    public void doTransaction(AccountTransactionValueObject accTransVal)
            throws IGreenDataAccountException;
}
