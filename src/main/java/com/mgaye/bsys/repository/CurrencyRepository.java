package com.mgaye.bsys.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mgaye.bsys.model.Currency;

public interface CurrencyRepository extends JpaRepository<Currency, String> {
    boolean existsByCode(String code);

    List<Currency> findAll();

}