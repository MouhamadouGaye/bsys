package com.mgaye.bsys.model;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.util.List;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import com.mgaye.bsys.model.Loan.LoanType;

@Entity
@Table(name = "loan_product")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoanProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;

    @Enumerated(EnumType.STRING)
    private LoanType type;

    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal interestRate;
    private Integer minTerm;
    private Integer maxTerm;

    @Column
    @JdbcTypeCode(SqlTypes.JSON) // If storing as JSON
    private List<String> eligibilityCriteria;
}