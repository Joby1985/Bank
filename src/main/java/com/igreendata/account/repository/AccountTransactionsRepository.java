package com.igreendata.account.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.igreendata.account.entity.AccountTransaction;

@Repository
public interface AccountTransactionsRepository
        extends CrudRepository<AccountTransaction, Long>,
        JpaSpecificationExecutor<AccountTransaction> {

    /*
     * public default List<AccountTransactions> listTransactions(TransactionsQueryFilter filter) {
     * return (List<AccountTransactions>) findBy }
     */
    /**
     * Find all Debits
     *
     * @return
     */
    /*
     * public List<AccountTransactions> findByAccNoAndTransDateBetweenAndTransValueIsLessThan();
     * public List<AccountTransactions> findByTransDateBetweenAndTransValueIsLessThan();
     *//**
        * Find all Credits
        *
        * @return
        *//*
           * public List<AccountTransactions>
           * findByAccNoAndTransDateBetweenAndTransValueIsGreaterThan();
           */
}
