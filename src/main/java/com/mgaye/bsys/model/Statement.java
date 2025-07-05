package com.mgaye.bsys.model;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.GenericGenerator;

import com.mgaye.bsys.model.Transaction.TransactionType;

import jakarta.persistence.AttributeOverride;
import jakarta.persistence.CascadeType;
import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderColumn;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.persistence.Version;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "statements")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Statement {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(columnDefinition = "uuid", updatable = false, nullable = false)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "account_id", nullable = false)
    private BankAccount account;

    @Column(name = "statement_number", nullable = false, unique = true)
    private String statementNumber;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "opening_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal openingBalance;

    @Column(name = "closing_balance", nullable = false, precision = 19, scale = 4)
    private BigDecimal closingBalance;

    @Column(name = "total_deposits", precision = 19, scale = 4)
    private BigDecimal totalDeposits = BigDecimal.ZERO;

    @Column(name = "total_withdrawals", precision = 19, scale = 4)
    private BigDecimal totalWithdrawals = BigDecimal.ZERO;

    @Column(name = "total_fees", precision = 19, scale = 4)
    private BigDecimal totalFees = BigDecimal.ZERO;

    @Column(name = "total_interest", precision = 19, scale = 4)
    private BigDecimal totalInterest = BigDecimal.ZERO;

    @CreationTimestamp
    @Column(name = "generated_at", nullable = false, updatable = false)
    private Instant generatedAt;

    @Column(name = "is_final", nullable = false)
    private boolean isFinal = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "statement_type", nullable = false, length = 20)
    private StatementType statementType;

    @Column(name = "currency", nullable = false, length = 3)
    private String currency;

    @ElementCollection
    @CollectionTable(name = "statement_items", joinColumns = @JoinColumn(name = "statement_id"), uniqueConstraints = @UniqueConstraint(columnNames = {
            "statement_id", "transaction_date", "reference" }))
    @OrderColumn(name = "item_order")
    private List<StatementItem> items = new ArrayList<>();

    @Version
    private Long version;

    // Enum for statement types
    public enum StatementType {
        MONTHLY,
        QUARTERLY,
        ANNUAL,
        AD_HOC,
        CLOSING
    }

    // Business methods
    public void addStatementItem(StatementItem item) {
        this.items.add(item);
        updateTotals(item);
    }

    private void updateTotals(StatementItem item) {
        if (item.getDirection() == TransactionDirection.INBOUND) {
            this.totalDeposits = this.totalDeposits.add(item.getAmount());
        } else {
            this.totalWithdrawals = this.totalWithdrawals.add(item.getAmount());
        }

        if (item.getType() == TransactionType.FEE) {
            this.totalFees = this.totalFees.add(item.getAmount());
        } else if (item.getType() == TransactionType.INTEREST) {
            this.totalInterest = this.totalInterest.add(item.getAmount());
        }
    }

    public void finalizeStatement() {
        if (this.isFinal) {
            throw new IllegalStateException("Statement is already finalized");
        }

        // Verify closing balance matches calculated balance
        BigDecimal calculatedBalance = this.openingBalance
                .add(this.totalDeposits)
                .subtract(this.totalWithdrawals);

        if (calculatedBalance.compareTo(this.closingBalance) != 0) {
            throw new IllegalStateException(
                    String.format("Closing balance %s doesn't match calculated balance %s",
                            this.closingBalance, calculatedBalance));
        }

        this.isFinal = true;
    }

    // Helper method to get statement period as string
    public String getPeriodDescription() {
        return String.format("%s to %s",
                startDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")),
                endDate.format(DateTimeFormatter.ofPattern("MMM d, yyyy")));
    }
}
