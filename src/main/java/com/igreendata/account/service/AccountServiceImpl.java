package com.igreendata.account.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.igreendata.account.entity.Account;
import com.igreendata.account.entity.AccountTransaction;
import com.igreendata.account.entity.AccountType;
import com.igreendata.account.entity.Currency;
import com.igreendata.account.entity.TransactionType;
import com.igreendata.account.exceptions.IGreenDataAccountException;
import com.igreendata.account.exceptions.InvalidAccountException;
import com.igreendata.account.exceptions.MaxDebitLimitReached;
import com.igreendata.account.repository.AccountDetailsRepository;
import com.igreendata.account.repository.AccountTransactionsRepository;
import com.igreendata.account.repository.specs.AccountSpecification;
import com.igreendata.account.repository.specs.SearchCriteria;
import com.igreendata.account.repository.specs.SearchOperation;
import com.igreendata.account.value.AccountTransactionValueObject;
import com.igreendata.account.value.AccountValueObject;

/**
 *
 * @author Joby Job
 *
 */
@Service
public class AccountServiceImpl implements AccountService {

	@Autowired
	private ExchangeRateService exchangeServ;

	@Autowired
	private AccountDetailsRepository accDetailsRepository;

	@Autowired
	private AccountTransactionsRepository accTransRepository;

	private final BigDecimal MAX_DEBIT = BigDecimal.valueOf(-100);

	@Override
	public List<Account> listAllAccounts() {
		return (List<Account>) accDetailsRepository.findAll();
	}

	@Override
	@Transactional
	public Account createAccount(AccountValueObject acc) throws IGreenDataAccountException {
		Account account = fromValue(acc);
		return saveAccountDetails(account);
	}

	@Override
	@Transactional
	public Account updateAccount(AccountValueObject acc) throws IGreenDataAccountException {
		Account account = fromValue(acc);
		Optional<Account> accountDetail = account.getAccno() != null ? accDetailsRepository.findById(account.getAccno())
				: Optional.empty();
		if (accountDetail.isPresent()) {
			Account savedAcc = accountDetail.get();
			savedAcc.setBalance(acc.getBalance());
			return savedAcc;
		} else {
			Account created = saveAccountDetails(account);
			return created;
		}
	}

	@Override
	public List<AccountTransaction> listTransactions(TransactionsQueryFilter filter) {
		AccountSpecification spec = new AccountSpecification();
		if (!ObjectUtils.isEmpty(filter.getAccNo())) {
			spec.add(new SearchCriteria("accountDetails", filter.getAccNo(), SearchOperation.EQUAL));
		} else if (!ObjectUtils.isEmpty(filter.getStartDate())) {
			spec.add(new SearchCriteria("transDate", filter.getStartDate(), SearchOperation.GREATER_THAN_EQUAL));
		} else if (!ObjectUtils.isEmpty(filter.getEndDate())) {
			spec.add(new SearchCriteria("transDate", filter.getEndDate(), SearchOperation.LESS_THAN_EQUAL));
		} else if (!ObjectUtils.isEmpty(filter.getTransType())) {

			switch (TransactionType.valueOf(filter.getTransType().toUpperCase())) {
			case CREDIT:
				spec.add(new SearchCriteria("transValue", 0, SearchOperation.GREATER_THAN));
				break;
			case DEBIT:
				spec.add(new SearchCriteria("transValue", 0, SearchOperation.LESS_THAN));
				break;
			default:
				spec.add(new SearchCriteria("transValue", 0, SearchOperation.EQUAL));
			}
		}
		List<AccountTransaction> accountTrans = null;
		if (spec.getList().isEmpty()) {
			accountTrans = (List<AccountTransaction>) accTransRepository.findAll();
		} else {
			accountTrans = accTransRepository.findAll(spec);
		}
		return accountTrans;
	}

	@Override
	@Transactional
	public void doTransaction(AccountTransactionValueObject accTransVal) throws IGreenDataAccountException {
		AccountTransaction accTransEntity = fromValue(accTransVal);
		Long accNo = accTransEntity.getAccountDetails().getAccno();
		Optional<Account> optAccountDetail = accDetailsRepository.findById(accNo);
		if (optAccountDetail.isPresent()) {
			Account accountDetail = optAccountDetail.get();
			BigDecimal monetaryVal = exchangeServ.getConvertedValue(accTransEntity.getTransAmt(),
					accTransEntity.getCurrency().getCode(), accountDetail.getCurrency().getCode());
			if (accountDetail.getBalance() != null) {
				BigDecimal netBalance = accountDetail.getBalance().add(monetaryVal);
				if (netBalance.compareTo(MAX_DEBIT) == -1) {
					throw new MaxDebitLimitReached(
							String.format("Net balance would become %f and this exceeds max debit limit of %f.",
									netBalance.doubleValue(), MAX_DEBIT.doubleValue()));
				}
				accountDetail.setBalance(netBalance);
			}
			accTransRepository.save(accTransEntity);
		} else {
			throw new InvalidAccountException(String.format("Account %s does not exist", accNo));
		}
	}

	/**
	 * Basic validation of required fields in transaction.
	 *
	 * @param accTransVal
	 */
	private void validateTransaction(AccountTransactionValueObject accTransVal) {
		if (accTransVal.getAccNo() == null) {
			throw new IGreenDataAccountException("Transaction Account number (accNo) is required");
		}
		if (accTransVal.getCurrency() == null) {
			throw new IGreenDataAccountException("Transaction currency (currency) is required");
		}
		if (accTransVal.getTransType() == null) {
			throw new IGreenDataAccountException("Transaction type (transType - Debit/Credit) is required");
		}
		if (accTransVal.getTransAmount() == null) {
			throw new IGreenDataAccountException("Transaction amount (transAmount) is required");
		}
	}

	/**
	 * Basic validation of required fields in Account.
	 *
	 * @param accObject
	 */
	private void validateAccount(AccountValueObject accObject) {
		if (accObject.getAccname() == null) {
			throw new IGreenDataAccountException("Account name (accname) is required");
		}
		if (accObject.getAcctype() == null) {
			throw new IGreenDataAccountException("Account type (acctype) is required");
		}
		if (accObject.getCurrency() == null) {
			throw new IGreenDataAccountException("Currency is required");
		}
	}

	/**
	 * AccountTransaction - Value Object to Entity
	 *
	 * @param value
	 * @return
	 */
	private AccountTransaction fromValue(AccountTransactionValueObject accTransVal) {
		validateTransaction(accTransVal);
		AccountTransaction accTrans = new AccountTransaction();
		Account details = new Account();
		details.setAccno(accTransVal.getAccNo());
		accTrans.setAccountDetails(details);
		Currency currency = new Currency(accTransVal.getCurrency(), "");
		accTrans.setCurrency(currency);
		// Always set transaction date as today's date
		accTrans.setTransDate(Calendar.getInstance().getTime());

		switch (TransactionType.valueOf(accTransVal.getTransType().toUpperCase())) {
		case DEBIT:
			BigDecimal val = accTransVal.getTransAmount().negate();
			System.out.println(val);
			accTrans.setTransAmt(val);
			break;
		default:
			accTrans.setTransAmt(accTransVal.getTransAmount() == null ? BigDecimal.ZERO : accTransVal.getTransAmount());
		}
		return accTrans;
	}

	/**
	 * Account - Value Object to Entity
	 *
	 * @param value
	 * @return
	 */
	private Account fromValue(AccountValueObject value) {
		validateAccount(value);
		Account account = new Account();
		account.setAccname(value.getAccname());
		account.setAcctype(new AccountType(value.getAcctype(), ""));
		account.setBalance(value.getBalance() == null ? BigDecimal.ZERO : value.getBalance());
		account.setCurrency(new Currency(value.getCurrency(), ""));
		return account;
	}

	/**
	 * Save account details
	 *
	 * @param account
	 * @return
	 */
	private Account saveAccountDetails(Account account) {
		Account created = accDetailsRepository.save(account);
		return created;
	}
}
