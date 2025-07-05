package com.mgaye.bsys.model;

import java.time.Instant;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "currencies", uniqueConstraints = {
        @UniqueConstraint(columnNames = "code"),
        @UniqueConstraint(columnNames = "name")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Currency {
    @Id
    @Column(length = 3)
    private String code; // ISO currency code (USD, EUR, etc.)

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private int decimalPlaces; // Number of decimal digits (2 for USD, 0 for JPY)

    @CreationTimestamp
    private Instant createdAt;
}