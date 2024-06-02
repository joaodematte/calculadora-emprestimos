package com.dematte.backend.response;

import com.dematte.backend.Payment;

import java.util.List;

public class CalculateLoanResponse
{
	List<Payment> payments;
	int parcels;

	public CalculateLoanResponse(List<Payment> payments, int parcels)
	{
		this.payments = payments;
		this.parcels = parcels;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}

	public int getParcels() {
		return parcels;
	}

	public void setParcels(int parcels) {
		this.parcels = parcels;
	}
}
