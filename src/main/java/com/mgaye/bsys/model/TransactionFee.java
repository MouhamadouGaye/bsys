package com.mgaye.bsys.model;

import lombok.*;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TransactionFee {

    @Column(name = "fee_amount")
    private BigDecimal amount;

    @Column(name = "transfer_fee")
    private String feeType;

    @Column(name = "fee_currency", length = 3)
    private String currency;

    @Column(name = "fee_description")
    private String description;

}