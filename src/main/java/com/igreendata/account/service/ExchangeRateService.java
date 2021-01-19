package com.igreendata.account.service;

import java.math.BigDecimal;

import com.igreendata.account.exceptions.InvalidCurrencyException;

public interface ExchangeRateService {

    public BigDecimal getConvertedValue(BigDecimal value, String fromCurrency, String toCurrency)
            throws InvalidCurrencyException;
}
