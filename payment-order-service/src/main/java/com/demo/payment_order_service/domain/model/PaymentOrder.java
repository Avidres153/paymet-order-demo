package com.demo.payment_order_service.domain.model;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class PaymentOrder {

    private final String orderNumber;
    private final String externalReference;
    private final Account debtorAccount;
    private final Account creditorAccount;
    private final Money instructedAmount;
    private final String remittanceInformation;
    private final LocalDate requestedExecutionDate;
    private PaymentStatus status;
    private LocalDateTime lastUpdate;

    public PaymentOrder(String orderNumber, String externalReference, Account debtorAccount, 
                        Account creditorAccount, Money instructedAmount, String remittanceInformation, 
                        LocalDate requestedExecutionDate, PaymentStatus status, LocalDateTime lastUpdate) {
        this.orderNumber = orderNumber;
        this.externalReference = externalReference;
        this.debtorAccount = debtorAccount;
        this.creditorAccount = creditorAccount;
        this.instructedAmount = instructedAmount;
        this.remittanceInformation = remittanceInformation;
        this.requestedExecutionDate = requestedExecutionDate;
        this.status = status;
        this.lastUpdate = lastUpdate;
    }

    // Getters
    public String getOrderNumber() { return orderNumber; }
    public String getExternalReference() { return externalReference; }
    public Account getDebtorAccount() { return debtorAccount; }
    public Account getCreditorAccount() { return creditorAccount; }
    public Money getInstructedAmount() { return instructedAmount; }
    public String getRemittanceInformation() { return remittanceInformation; }
    public LocalDate getRequestedExecutionDate() { return requestedExecutionDate; }
    public PaymentStatus getStatus() { return status; }
    public LocalDateTime getLastUpdate() { return lastUpdate; }

    // Domain Logic Methods
    public void updateStatus(PaymentStatus newStatus) {
        this.status = newStatus;
        this.lastUpdate = LocalDateTime.now();
    }
}
