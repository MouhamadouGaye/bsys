package com.mgaye.bsys.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;

import jakarta.persistence.Table;

// model/AccountStatement.java

@Entity
@Table(name = "account_statements")
public class AccountStatement {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "account_id", nullable = false)
    private String accountId;

    @Column(nullable = false)
    private Instant generatedAt;

    @Column(nullable = false)
    private LocalDate periodStart;

    @Column(nullable = false)
    private LocalDate periodEnd;

    @Lob
    @Column(nullable = false)
    private byte[] pdfContent;

    @Column(length = 50)
    private String statementNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatementStatus status = StatementStatus.GENERATED;

    // Additional fields for statement details

    // Use ElementCollection instead of OneToMany
    @ElementCollection
    @CollectionTable(name = "account_statement_items", joinColumns = @JoinColumn(name = "account_statement_id"))
    private List<StatementItem> items = new ArrayList<>();

    private BigDecimal balance;

    // Constructors
    public AccountStatement() {
    }

    public AccountStatement(String accountId, LocalDate periodStart, LocalDate periodEnd, byte[] pdfContent) {
        this.accountId = accountId;
        this.generatedAt = Instant.now();
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.pdfContent = pdfContent;
    }

    // Getters and Setters
    public String getId() {
        return id;
    }

    public String getAccountId() {
        return accountId;
    }

    public void setAccountId(String accountId) {
        this.accountId = accountId;
    }

    public Instant getGeneratedAt() {
        return generatedAt;
    }

    public LocalDate getPeriodStart() {
        return periodStart;
    }

    public void setPeriodStart(LocalDate periodStart) {
        this.periodStart = periodStart;
    }

    public LocalDate getPeriodEnd() {
        return periodEnd;
    }

    public void setPeriodEnd(LocalDate periodEnd) {
        this.periodEnd = periodEnd;
    }

    public byte[] getPdfContent() {
        return pdfContent;
    }

    public void setPdfContent(byte[] pdfContent) {
        this.pdfContent = pdfContent;
    }

    public String getStatementNumber() {
        return statementNumber;
    }

    public void setStatementNumber(String statementNumber) {
        this.statementNumber = statementNumber;
    }

    public StatementStatus getStatus() {
        return status;
    }

    public void setStatus(StatementStatus status) {
        this.status = status;
    }

    // Enum for statement status
    public enum StatementStatus {
        GENERATED,
        DELIVERED,
        VIEWED,
        ARCHIVED
    }

    public AccountStatement(String accountId, LocalDate periodStart, LocalDate periodEnd,
            List<StatementItem> items, BigDecimal balance) {
        this.accountId = accountId;
        this.periodStart = periodStart;
        this.periodEnd = periodEnd;
        this.items = items;
        this.balance = balance;
    }

    // equals and hashCode
    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        AccountStatement that = (AccountStatement) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // toString
    @Override
    public String toString() {
        return "AccountStatement{" +
                "id='" + id + '\'' +
                ", accountId='" + accountId + '\'' +
                ", generatedAt=" + generatedAt +
                ", periodStart=" + periodStart +
                ", periodEnd=" + periodEnd +
                ", statementNumber='" + statementNumber + '\'' +
                ", status=" + status +
                '}';
    }

}
