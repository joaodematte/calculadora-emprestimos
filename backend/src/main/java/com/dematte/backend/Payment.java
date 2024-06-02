package com.dematte.backend;

import java.time.LocalDate;

public class Payment {
    public LocalDate date;
    public double value;
    public double remainingBalance;
    public double total;
    public double amortization;
    public double debit;
    public double provision;
    public double accumulated;
    public double paid;
    public int consolidated;

    public Payment(LocalDate date, double value) {
        this.date = date;
        this.value = value;
        this.remainingBalance = value;
        this.total = 0;
        this.amortization = 0;
        this.debit = value;
        this.provision = 0;
        this.accumulated = 0;
        this.paid = 0;
        this.consolidated = -1;
    }

    public Payment(LocalDate date, double remainingBalance, double total, double amortization, double debit, double provision, double accumulated, double paid, int consolidated) {
        this.date = date;
        this.debit = debit;
        this.remainingBalance = remainingBalance;
        this.total = total;
        this.amortization = amortization;
        this.provision = provision;
        this.accumulated = accumulated;
        this.paid = paid;
        this.consolidated = consolidated;

        if (debit < 0.0) {
            this.debit = 0.0;
        }

        if (remainingBalance < 0.0) {
            this.remainingBalance = 0.0;
        }
    }

    public LocalDate getDate() {
        return date;
    }

    public double getDebit() {
        return debit;
    }


    public double getAccumulated() {
        return accumulated;
    }
}