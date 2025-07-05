package com.mgaye.bsys.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mgaye.bsys.exception.ConflictException;
import com.mgaye.bsys.model.Currency;
import com.mgaye.bsys.repository.CurrencyRepository;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CurrencyService {

    private final CurrencyRepository currencyRepository;

    @Transactional
    public Currency createCurrency(String code, String name, int decimalPlaces) {
        if (currencyRepository.existsByCode(code)) {
            throw new ConflictException("Currency " + code + " already exists");
        }

        Currency currency = Currency.builder()
                .code(code)
                .name(name)
                .decimalPlaces(decimalPlaces)
                .build();

        return currencyRepository.save(currency);
    }

    public List<Currency> getAllCurrencies() {
        return currencyRepository.findAll();
    }
}