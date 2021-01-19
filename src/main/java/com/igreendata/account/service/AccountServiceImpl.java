package com.igreendata.account.service;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import com.igreendata.account.entity.AccountDetail;
import com.igreendata.account.entity.AccountTransaction;
import com.igreendata.account.entity.Currency;
import com.igreendata.account.entity.TransactionType;
import com.igreendata.account.exceptions.IGreenDataAccountException;
import com.igreendata.account.repository.AccountDetailsRepository;
import com.igreendata.account.repository.AccountTransactionsRepository;
import com.igreendata.account.repository.specs.AccountSpecification;
import com.igreendata.account.repository.specs.SearchCriteria;
import com.igreendata.account.repository.specs.SearchOperation;
import com.igreendata.account.value.AccountTransactionValueObject;

@Service
public class AccountServiceImpl implements AccountService {

    @Autowired
    private ExchangeRateService exchangeServ;

    @Autowired
    private AccountDetailsRepository accDetailsRepository;

    @Autowired
    private AccountTransactionsRepository accTransRepository;

    @Override
    public List<AccountDetail> listAllAccounts() {
        return (List<AccountDetail>) accDetailsRepository.findAll();
    }

    @Override
    @Transactional
    public AccountDetail createAccount(AccountDetail acc) throws IGreenDataAccountException {
        return accDetailsRepository.save(acc);
    }

    @Override
    @Transactional
    public AccountDetail updateAccount(AccountDetail acc) throws IGreenDataAccountException {
        Optional<AccountDetail> accountDetail = accDetailsRepository.findById(acc.getAccno());
        if (accountDetail.isPresent()) {
            AccountDetail savedAcc = accountDetail.get();
            savedAcc.setBalance(acc.getBalance());
            return savedAcc;
        } else {
            return accDetailsRepository.save(acc);
        }
    }

    @Override
    public List<AccountTransaction> listTransactions(TransactionsQueryFilter filter) {
        AccountSpecification spec = new AccountSpecification();
        if (!ObjectUtils.isEmpty(filter.getAccNo())) {
            spec.add(
                    new SearchCriteria("accountDetails", filter.getAccNo(), SearchOperation.EQUAL));
        } else if (!ObjectUtils.isEmpty(filter.getStartDate())) {
            spec.add(
                    new SearchCriteria(
                            "transDate", filter.getStartDate(),
                            SearchOperation.GREATER_THAN_EQUAL));
        } else if (!ObjectUtils.isEmpty(filter.getEndDate())) {
            spec.add(
                    new SearchCriteria(
                            "transDate", filter.getEndDate(), SearchOperation.LESS_THAN_EQUAL));
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
    public void doTransaction(AccountTransactionValueObject accTransVal)
            throws IGreenDataAccountException {
        AccountTransaction accTransEntity = fromValue(accTransVal);
        accTransRepository.save(accTransEntity);
        Optional<AccountDetail> optAccountDetail = accDetailsRepository.findById(
                accTransEntity.getAccountDetails().getAccno());
        if (optAccountDetail.isPresent()) {
            AccountDetail accountDetail = optAccountDetail.get();
            BigDecimal monetaryVal = exchangeServ.getConvertedValue(
                    accTransEntity.getTransValue(), accTransEntity.getCurrency().getCode(),
                    accountDetail.getCurrency().getCode());
            if (accountDetail.getBalance() != null) {
                accountDetail.setBalance(accountDetail.getBalance().add(monetaryVal));
            }
        }
    }

    private AccountTransaction fromValue(AccountTransactionValueObject value) {
        AccountTransaction accTrans = new AccountTransaction();
        AccountDetail details = new AccountDetail();
        details.setAccno(value.getAccNo());
        accTrans.setAccountDetails(details);
        Currency currency = new Currency(value.getCurrency(), "");
        accTrans.setCurrency(currency);
        // Always set transaction date as today's date
        accTrans.setTransDate(Calendar.getInstance().getTime());

        switch (TransactionType.valueOf(value.getTransactionType().toUpperCase())) {
            case DEBIT:
                accTrans.setTransValue(value.getTransValue().negate());
            default:
                accTrans.setTransValue(value.getTransValue());
        }
        return accTrans;
    }
}
