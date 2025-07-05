package com.mgaye.bsys.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
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
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.*;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.Map;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;

// @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)

// // @TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
// @TypeDef(name = "json", typeClass = JsonType.class)

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionType type;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false, precision = 19, scale = 4)
    private BigDecimal balance;

    @Column(nullable = false, length = 3)
    private String currency;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionStatus status;

    @Column(nullable = false)
    private Instant timestamp;

    @Column(nullable = false)
    private Instant date;

    private String description;

    @Column(name = "reference_id")
    private String referenceNumber;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "merchant_id")
    private Merchant merchant;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TransactionDirection direction;

    @Version
    private Long version;

    @Embedded
    private TransactionFee fee;

    // No need for extra dependency in newer versions:
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Map<String, Object> metadata;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_account_id")
    private BankAccount destinationAccount;

    @OneToOne
    @JoinColumn(name = "linked_transaction_id")
    private Transaction linkedTransaction;

    public enum TransactionType {
        DEPOSIT, WITHDRAWAL, TRANSFER, PAYMENT, FEE, REFUND, CHARGEBACK, DEBIT, CREDIT, INTEREST
    }

    public enum TransactionStatus {
        PENDING, COMPLETED, FAILED, CANCELLED, REVERSED
    }

    public enum TransactionDirection {
        INBOUND, OUTBOUND
    }
}
