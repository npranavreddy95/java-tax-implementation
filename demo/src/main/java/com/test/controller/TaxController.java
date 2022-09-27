package com.test.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.test.service.EmployeeService;
import com.test.vo.EmployeeVO;
import com.test.vo.TaxCalculationReportVO;

@RestController
public class TaxController {

	@Autowired
	private EmployeeService employeeService;

	@PostMapping(path = "/saveEmployeeSalaryDetails", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> saveEmployeeDetails(@RequestBody EmployeeVO vo) {
		try {
			Long saveEmployeeDetails = employeeService.saveEmployeeDetails(vo);
			return new ResponseEntity<>(saveEmployeeDetails, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

	@GetMapping(path = "/getEmployeeSalaryAndTaxDetails", produces = { MediaType.APPLICATION_JSON_VALUE }, consumes = {
			MediaType.APPLICATION_JSON_VALUE })
	public ResponseEntity<?> getEmployeeSalaryDetails() {
		try {
			List<TaxCalculationReportVO> salaryAndTaxDetails = employeeService.getSalaryAndTaxDetails();
			return new ResponseEntity<>(salaryAndTaxDetails, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}

}
