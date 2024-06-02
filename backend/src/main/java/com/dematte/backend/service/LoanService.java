package com.dematte.backend.service;

import com.dematte.backend.Payment;
import com.dematte.backend.dto.LoanDTO;
import com.dematte.backend.response.CalculateLoanResponse;
import com.dematte.backend.utils.Holidays;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

public class LoanService
{

	public static CalculateLoanResponse calculate(LoanDTO loanDTO)
	{
		LocalDate initialDate = loanDTO.getInitialDate();
		LocalDate endDate = loanDTO.getEndDate();
		LocalDate firstPaymentDate = loanDTO.getFirstPaymentDate();
		double value = loanDTO.getValue();
		double interestRate = loanDTO.getInterestRate();
		boolean isPaymentOnLastDayOfMonth = firstPaymentDate.getDayOfMonth() == firstPaymentDate.lengthOfMonth();

		int parcels = calculateParcels(initialDate, endDate);
		int consolidatedParcels = 1;

		List<Payment> payments = new ArrayList<>();
		List<LocalDate> paidDates = new ArrayList<>();
		Payment tempPayment = new Payment(initialDate, value);

		payments.add(tempPayment);

		LocalDate tempDate = initialDate;

		while (consolidatedParcels <= parcels)
		{
			tempDate = calculateNextDate(tempDate, firstPaymentDate, endDate, paidDates, isPaymentOnLastDayOfMonth);
			boolean isConsolidated = !paidDates.isEmpty() && paidDates.getLast().equals(tempDate);

			double daysDiff = Math.abs(ChronoUnit.DAYS.between(tempDate, tempPayment.getDate()));

			double provision = calculateProvision(interestRate, daysDiff, tempPayment.getDebit(),
				tempPayment.getAccumulated());
			double paid = isConsolidated ? tempPayment.getAccumulated() + provision : 0;
			double accumulated = tempPayment.getAccumulated() + provision - paid;
			double amortization = isConsolidated ? value / parcels : 0;
			double debit = tempPayment.getDebit() - amortization;
			double total = amortization + paid;
			double remainingBalance = debit + accumulated;

			Payment payment = new Payment(tempDate, remainingBalance, total, amortization, debit, provision,
				accumulated, paid, (isConsolidated ? consolidatedParcels : -1));

			payments.add(payment);

			tempPayment = payment;

			if (tempDate.isAfter(endDate) || tempDate.equals(endDate))
			{
				consolidatedParcels = parcels + 1;
			}
			else if (isConsolidated)
			{
				consolidatedParcels++;
			}
		}

		return new CalculateLoanResponse(payments, parcels);
	}

	public static LocalDate calculateNextDate(LocalDate currentDate, LocalDate firstPaymentDate, LocalDate endDate,
		List<LocalDate> paidDates, boolean isPaymentOnLastDayOfMonth)
	{
		LocalDate nextDate = currentDate;
		boolean isLastDayOfMonth = isLastDayOfMonth(nextDate);

		if (isLastDayOfMonth)
		{
			if (isPaymentOnLastDayOfMonth)
			{
				if (paidDates.contains(nextDate))
				{
					nextDate = nextDate.plusMonths(1);
				}
			}
			else
			{
				nextDate = nextDate.plusMonths(1);
			}
		}

		if (shouldPayOnDate(nextDate, firstPaymentDate, endDate, paidDates))
		{
			nextDate = getNextPaymentDate(nextDate, firstPaymentDate, endDate);
			paidDates.add(nextDate);
		}
		else
		{
			if (nextDate.isAfter(endDate))
			{
				nextDate = endDate;
			}
			else
			{
				nextDate = nextDate.withDayOfMonth(nextDate.lengthOfMonth());
			}
		}

		return nextDate;
	}

	private static LocalDate getNextPaymentDate(LocalDate currentDate, LocalDate firstPaymentDate, LocalDate endDate)
	{
		LocalDate nextDate;

		int lastDayOfMonth = currentDate.lengthOfMonth();
		int paymentDate = firstPaymentDate.getDayOfMonth();

		if (paymentDate > lastDayOfMonth)
		{
			nextDate = currentDate.withDayOfMonth(lastDayOfMonth);
		}
		else
		{
			nextDate = currentDate.withDayOfMonth(firstPaymentDate.getDayOfMonth());
		}

		if (nextDate.isAfter(endDate))
		{
			return getNextWorkDate(endDate);
		}

		return getNextWorkDate(nextDate);
	}

	private static boolean shouldPayOnDate(LocalDate currentDate, LocalDate firstPaymentDate, LocalDate endDate,
		List<LocalDate> paidDates)
	{
		return checkIfHasToPay(currentDate, firstPaymentDate, endDate) && !paidDates.contains(currentDate);
	}

	public static boolean isLastDayOfMonth(LocalDate date)
	{
		return date.getDayOfMonth() == date.lengthOfMonth();
	}

	public static boolean checkIfHasToPay(LocalDate date, LocalDate firstPaymentDate, LocalDate endDate)
	{
		return
			(firstPaymentDate.getMonthValue() == date.getMonthValue() && firstPaymentDate.getYear() == date.getYear())
				|| date.isAfter(firstPaymentDate) || date.equals(endDate);
	}

	private static int calculateParcels(LocalDate initialDate, LocalDate endDate)
	{
		return ((endDate.getYear() - initialDate.getYear()) * 12) + endDate.getMonthValue()
			- initialDate.getMonthValue();
	}

	public static LocalDate getNextWorkDate(LocalDate date)
	{
		List<LocalDate> holidays = Holidays.calculate(date.getYear());
		LocalDate tempDate = date;

		if (holidays.contains(date))
		{
			tempDate = date.plusDays(1);
		}

		if (isHoliday(tempDate))
		{
			return getNextWorkDate(tempDate);
		}
		else
		{
			return getNextWeekDate(tempDate);
		}
	}

	public static boolean isHoliday(LocalDate date)
	{
		List<LocalDate> holidays = Holidays.calculate(date.getYear());

		return holidays.contains(date);
	}

	public static LocalDate getNextWeekDate(LocalDate date)
	{
		DayOfWeek dayOfWeek = date.getDayOfWeek();

		if (dayOfWeek == DayOfWeek.SATURDAY)
		{
			return date.plusDays(2);
		}
		else if (dayOfWeek == DayOfWeek.SUNDAY)
		{
			return date.plusDays(1);
		}

		return date;
	}

	private static double calculateProvision(double interestRate, double daysDiff, double debit, double accumulated)
	{
		int DAYS_IN__YEAR = 360;

		return (Math.pow((interestRate / 100) + 1, daysDiff / DAYS_IN__YEAR) - 1) * (debit + accumulated);
	}
}
