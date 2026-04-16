package swp391.old_bicycle_project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "payout_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PayoutProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    @Column(name = "bank_code", nullable = false)
    private String bankCode;

    @Column(name = "bank_bin", nullable = false)
    private String bankBin;

    @Column(name = "account_number", nullable = false)
    private String accountNumber;

    @Column(name = "account_name", nullable = false)
    private String accountName;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Manual Getter
    public UUID getId() { return id; }
    public User getUser() { return user; }
    public String getBankCode() { return bankCode; }
    public String getBankBin() { return bankBin; }
    public String getAccountNumber() { return accountNumber; }
    public String getAccountName() { return accountName; }
    public LocalDateTime getUpdatedAt() { return updatedAt; }

    // Manual Setter
    public void setUser(User user) { this.user = user; }
    public void setBankCode(String bankCode) { this.bankCode = bankCode; }
    public void setBankBin(String bankBin) { this.bankBin = bankBin; }
    public void setAccountNumber(String accountNumber) { this.accountNumber = accountNumber; }
    public void setAccountName(String accountName) { this.accountName = accountName; }

    // Manual Builder
    public static PayoutProfileBuilder builder() { return new PayoutProfileBuilder(); }
    public static class PayoutProfileBuilder {
        private PayoutProfile r = new PayoutProfile();
        public PayoutProfileBuilder id(UUID id) { r.id = id; return this; }
        public PayoutProfileBuilder user(User user) { r.user = user; return this; }
        public PayoutProfileBuilder bankCode(String bankCode) { r.bankCode = bankCode; return this; }
        public PayoutProfileBuilder bankBin(String bankBin) { r.bankBin = bankBin; return this; }
        public PayoutProfileBuilder accountNumber(String accountNumber) { r.accountNumber = accountNumber; return this; }
        public PayoutProfileBuilder accountName(String accountName) { r.accountName = accountName; return this; }
        public PayoutProfile build() { return r; }
    }
}
