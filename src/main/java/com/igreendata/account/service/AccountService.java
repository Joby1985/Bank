package com.igreendata.account.service;

import java.util.List;

import com.igreendata.account.entity.Account;
import com.igreendata.account.entity.AccountTransaction;
import com.igreendata.account.exceptions.IGreenDataAccountException;
import com.igreendata.account.value.AccountTransactionValueObject;
import com.igreendata.account.value.AccountValueObject;

/**
 *
 * @author Joby Job
 *
 */
public interface AccountService {

	/**
	 * List all accounts
	 *
	 * @return
	 */
	public List<Account> listAllAccounts();

	/**
	 * Create new account.
	 *
	 * @param acc
	 * @return
	 * @throws IGreenDataAccountException
	 */
	public Account createAccount(AccountValueObject acc) throws IGreenDataAccountException;

	/**
	 * Update if existing account, else create new one.
	 *
	 * @param acc
	 * @return
	 * @throws IGreenDataAccountException
	 */
	public Account updateAccount(AccountValueObject acc) throws IGreenDataAccountException;

	/**
	 * List transactions based on the filter criteria.
	 *
	 * @param filter
	 * @return
	 */
	public List<AccountTransaction> listTransactions(TransactionsQueryFilter filter);

	/**
	 * Do an account transaction.
	 *
	 * @param accTransVal
	 * @throws IGreenDataAccountException
	 */
	public void doTransaction(AccountTransactionValueObject accTransVal) throws IGreenDataAccountException;
}
