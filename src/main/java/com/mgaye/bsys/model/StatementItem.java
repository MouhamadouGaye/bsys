package com.mgaye.bsys.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.mgaye.bsys.model.Transaction.TransactionStatus;
import com.mgaye.bsys.model.Transaction.TransactionType;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Embeddable // Use this if going with Option 1
// OR
// @Entity // Use this if going with Option 2
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StatementItem {

    @Column(name = "transaction_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Instant date;

    @Column(nullable = false)
    private String reference;

    @Column(nullable = false, precision = 19, scale = 4)
    @Positive
    private BigDecimal amount;

    @Column(nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private TransactionType type;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private TransactionStatus status;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 10)
    private TransactionDirection direction;

    @Column(name = "running_balance", precision = 19, scale = 4)
    private BigDecimal runningBalance;

    @Column(name = "is_reconciled")
    private boolean reconciled;

    // ONLY if using bidirectional relationship
    // @ManyToOne(fetch = FetchType.LAZY)
    // @JoinColumn(name = "statement_id")
    // private Statement statement;

    // Helper methods remain the same
    public String getFormattedAmount() {
        String sign = direction == TransactionDirection.INBOUND ? "+" : "-";
        return String.format("%s%s %.2f", sign, type.toString().charAt(0), amount.abs());
    }

    public String getFormattedDate() {
        return date.atZone(ZoneId.systemDefault())
                .format(DateTimeFormatter.ofPattern("MMM dd, yyyy hh:mm a"));
    }
}