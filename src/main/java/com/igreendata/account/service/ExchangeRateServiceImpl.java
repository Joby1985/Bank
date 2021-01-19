package com.igreendata.account.service;

import java.math.BigDecimal;

import javax.money.Monetary;
import javax.money.MonetaryAmount;
import javax.money.UnknownCurrencyException;
import javax.money.convert.CurrencyConversion;
import javax.money.convert.ExchangeRateProvider;
import javax.money.convert.MonetaryConversions;

import org.springframework.stereotype.Service;

import com.igreendata.account.exceptions.InvalidCurrencyException;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

    private static ExchangeRateProvider ecbExchangeRateProvider = MonetaryConversions.getExchangeRateProvider(
            "IMF");

    @Override
    public BigDecimal getConvertedValue(BigDecimal value, String fromCurrency, String toCurrency)
            throws InvalidCurrencyException {
        MonetaryAmount fromAmount = Monetary.getDefaultAmountFactory().setCurrency(
                fromCurrency).setNumber(value).create();
        try {
            Monetary.getCurrency(fromCurrency);
            Monetary.getCurrency(toCurrency);
        } catch (UnknownCurrencyException e) {
            throw new InvalidCurrencyException(e.getMessage());
        }
        CurrencyConversion conversion = ecbExchangeRateProvider.getCurrencyConversion(toCurrency);
        return BigDecimal.valueOf(fromAmount.with(conversion).getNumber().doubleValue());
    }
}
