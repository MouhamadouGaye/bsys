package com.mgaye.bsys.dto.request;

import java.math.BigDecimal;
import java.util.List;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;

/**
 * Request DTO for setting limits on a beneficiary.
 * This record encapsulates the maximum transaction amount, daily limit,
 * and a list of allowed purposes for transactions to this beneficiary.
 */
public record BeneficiaryLimitsRequest(
        @PositiveOrZero BigDecimal maxTransactionAmount,
        @PositiveOrZero BigDecimal dailyLimit,
        List<@NotBlank String> allowedPurposes) {

}