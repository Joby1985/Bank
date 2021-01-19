package com.igreendata.account.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author Joby Job
 *
 */
@Entity
@Table(name = "acctype")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AccountType {

	@Id
	@Column(name = "CODE")
	private String code;

	@Column(name = "DESCRIPTION")
	private String description;
}
