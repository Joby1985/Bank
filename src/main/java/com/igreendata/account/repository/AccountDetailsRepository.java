package com.igreendata.account.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.igreendata.account.entity.Account;

/**
 *
 * @author Joby Job
 *
 */
@Repository
public interface AccountDetailsRepository extends CrudRepository<Account, Long> {

}
