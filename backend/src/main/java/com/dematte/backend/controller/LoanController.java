package com.dematte.backend.controller;

import com.dematte.backend.dto.LoanDTO;
import com.dematte.backend.response.CalculateLoanResponse;
import com.dematte.backend.service.LoanService;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("loan")
@CrossOrigin(origins = "http://localhost:5173")
public class LoanController
{
	@PostMapping("calculate")
	public CalculateLoanResponse calculate(@RequestBody LoanDTO loanDTO)
	{
		return LoanService.calculate(loanDTO);
	}
}