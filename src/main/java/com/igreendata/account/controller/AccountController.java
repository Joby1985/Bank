package com.igreendata.account.controller;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.igreendata.account.entity.AccountDetail;
import com.igreendata.account.entity.AccountTransaction;
import com.igreendata.account.exceptions.IGreenDataAccountException;
import com.igreendata.account.service.AccountService;
import com.igreendata.account.service.TransactionsQueryFilter;
import com.igreendata.account.value.AccountTransactionValueObject;

@RestController
public class AccountController {

    @Autowired
    private AccountService service;

    @GetMapping("/accounts")
    public List<AccountDetail> getAllAccounts() {
        return service.listAllAccounts();
    }

    @PostMapping("/accounts")
    ResponseEntity<AccountDetail> createAccount(@RequestBody AccountDetail acc) {
        AccountDetail account = service.updateAccount(acc);
        return new ResponseEntity(account, HttpStatus.OK);
    }

    @PostMapping("/debit")
    ResponseEntity<AccountTransaction> doDebit(@RequestBody AccountTransactionValueObject accTrans) {
        service.doTransaction(accTrans);
        return new ResponseEntity("Transaction successful", HttpStatus.OK);
    }

    @GetMapping("/transactions")
    public List<AccountTransaction> getAllTransactions(@RequestParam(
            value = "accNo",
            required = false) String accNo,
                                                       @RequestParam(
                                                               value = "startDate",
                                                               required = false) String startDate,
                                                       @RequestParam(
                                                               value = "endDate",
                                                               required = false) String endDate,
                                                       @RequestParam(
                                                               value = "transType",
                                                               required = false) String transType) {
        SimpleDateFormat formatter = new SimpleDateFormat("dd MMM yyyy", Locale.ENGLISH);
        TransactionsQueryFilter tf = null;
        try {
            tf = new TransactionsQueryFilter(
                    (accNo != null ? Long.parseLong(accNo) : null),
                    (startDate != null ? formatter.parse(startDate) : null),
                    (endDate != null ? formatter.parse(endDate) : null), transType);
        } catch (Exception e) {
            throw new IGreenDataAccountException(e.getMessage());
        }
        return service.listTransactions(tf);
    }
}
