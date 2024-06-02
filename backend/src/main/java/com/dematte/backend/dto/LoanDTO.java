package com.dematte.backend.dto;

import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;

public class LoanDTO {
	@NotNull
	private final LocalDate initialDate;

	@NotNull
	private final LocalDate endDate;

	@NotNull
	private final LocalDate firstPaymentDate;

	@NotNull
	private final double value;

	@NotNull
	private final int interestRate;

	public LoanDTO(LocalDate initialDate, LocalDate endDate, LocalDate firstPaymentDate, double value, int interestRate) {
		this.initialDate = initialDate;
		this.endDate = endDate;
		this.firstPaymentDate = firstPaymentDate;
		this.value = value;
		this.interestRate = interestRate;
	}

	public LocalDate getInitialDate() {
		return initialDate;
	}

	public LocalDate getEndDate() {
		return endDate;
	}

	public LocalDate getFirstPaymentDate() {
		return firstPaymentDate;
	}

	public double getValue() {
		return value;
	}

	public int getInterestRate() {
		return interestRate;
	}
}
