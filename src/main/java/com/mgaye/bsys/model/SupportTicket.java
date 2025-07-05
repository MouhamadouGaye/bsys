
package com.mgaye.bsys.model;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.type.SqlTypes;

import lombok.*;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "support_tickets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SupportTicket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false, length = 100)
    private String subject;

    @Column(nullable = false, length = 2000)
    private String description;

    @Column(nullable = false, length = 20)
    private String status; // OPEN, IN_PROGRESS, RESOLVED, CLOSED

    @Column(nullable = false)
    private Instant createdAt;

    @Column
    private Instant resolvedAt;

    @Column
    private Instant updatedAt;

    @Column(length = 50)
    private String category; // ACCOUNT, TRANSACTION, CARD, LOAN, OTHER

    @Column(length = 50)
    private String priority; // LOW, MEDIUM, HIGH, URGENT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to")
    private User assignedTo;

    @OneToMany(mappedBy = "ticket", cascade = CascadeType.ALL)
    @OrderBy("createdAt ASC")
    private List<TicketMessage> messages = new ArrayList<>();

    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private TicketMetadata metadata;

    @Data
    @Builder
    public static class TicketMetadata {
        private List<String> relatedTransactionIds;
        private List<String> relatedAccountNumbers;
        private String contactPhone;
        private String preferredContactTime;
        private Map<String, Object> customFields;
    }
}
