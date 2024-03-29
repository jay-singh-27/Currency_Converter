package com.example.currencyconverter.services;

import com.example.currencyconverter.repositories.AuditHistoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.currencyconverter.models.ConversionCurrency;
import com.example.currencyconverter.models.Currency;
import com.example.currencyconverter.repositories.CurrencyRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Service
public class CurrencyService {

    private CurrencyRepository currencyRepository;

    @Autowired
    private AuditHistoryService auditHistoryService;

    public CurrencyService(CurrencyRepository currencyRepository) {
        this.currencyRepository = currencyRepository;
    }

    public List<Currency> getAllCurrencies() {
        List<Currency> currencyList = this.currencyRepository.findAll();
        currencyList.sort(Comparator.comparing(Currency::getName));
        return currencyList;
    }

    public Optional<Double> convert(ConversionCurrency conversionCurrency) {
        Optional<Currency> toOptional = this.currencyRepository.findById(conversionCurrency.getTo().toUpperCase());
        Optional<Currency> fromOptional = this.currencyRepository.findById(conversionCurrency.getFrom().toUpperCase());

        if(toOptional.isPresent() && fromOptional.isPresent()) {

            if(conversionCurrency.getValue() < 0) {
                return Optional.empty();
            }

            Currency to = toOptional.get();
            Currency from = fromOptional.get();
            Double toValue = to.getValueInEuros();
            Double fromValue = from.getValueInEuros();

            Double result = toValue * conversionCurrency.getValue() / fromValue;
            performAudit(conversionCurrency, result);
            return Optional.of(result);

        }

        return Optional.empty();
    }

    private void performAudit(ConversionCurrency conversionInfo, Double result ) {
    	String user = "admin";
        String auditString = conversionInfo.getAuditString(result);
        auditHistoryService.addAuditEntry(user, auditString);
    }

}
