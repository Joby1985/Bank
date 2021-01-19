package com.igreendata.account.service;

import java.math.BigDecimal;

import com.igreendata.account.exceptions.InvalidCurrencyException;

/**
 *
 * @author Joby Job
 *
 */
public interface ExchangeRateService {

	/**
	 * Convert the provided money value in the 'from' currency to equivalent value
	 * in 'to'
	 *
	 * @param value
	 * @param fromCurrency
	 * @param toCurrency
	 * @return
	 * @throws InvalidCurrencyException
	 */
	public BigDecimal getConvertedValue(BigDecimal value, String fromCurrency, String toCurrency)
			throws InvalidCurrencyException;
}
