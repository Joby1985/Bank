package com.igreendata.account.repository;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.igreendata.account.entity.AccountTransaction;

/**
 *
 * @author Joby Job
 *
 */
@Repository
public interface AccountTransactionsRepository
		extends CrudRepository<AccountTransaction, Long>, JpaSpecificationExecutor<AccountTransaction> {

}
