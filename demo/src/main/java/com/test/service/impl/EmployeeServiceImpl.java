package com.test.service.impl;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.test.dto.Employee;
import com.test.dto.EmployeeDAO;
import com.test.exception.ValidationException;
import com.test.mapper.EmployeeEntityMapper;
import com.test.service.EmployeeService;
import com.test.vo.EmployeeVO;
import com.test.vo.TaxCalculationReportVO;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeDAO employeeDAO;

	@Override
	public Long saveEmployeeDetails(EmployeeVO vo) {
		try {
			this.doInputValidation(vo);
			EmployeeEntityMapper employeeEntityMapper = new EmployeeEntityMapper();
			Employee employee = employeeDAO.save(employeeEntityMapper.convertVoToEntity(vo));
			return employee.getId();
		} catch (Exception e) {
			throw new ValidationException(e.getMessage());
		}
	}

	@Override
	public List<TaxCalculationReportVO> getSalaryAndTaxDetails() {
		List<Employee> employeeDetails = employeeDAO.findAll();
		if (!CollectionUtils.isEmpty(employeeDetails)) {
			return employeeDetails.stream().map(employee -> this.getSalaryReportForEmployee(employee))
					.collect(Collectors.toList());
		} else {
			return Collections.emptyList();
		}
	}

	private TaxCalculationReportVO getSalaryReportForEmployee(Employee employee) {
		TaxCalculationReportVO taxCalculationReportVO = new TaxCalculationReportVO();
		Double oneMonthsalary = employee.getSalary();
		LocalDate doj = employee.getDoj();
		Double oneYearsalary = oneMonthsalary * 12;
		if (doj.isEqual(LocalDate.parse("16-05-2022"))) {
			Double perMonth = oneYearsalary / 12;
			Double perDay = perMonth / 30;
			Double minusAmount = perDay * 45;
			oneYearsalary = oneYearsalary - minusAmount;
		}
		Double taxAmount = taxCalculation(oneYearsalary);
		Double calculateCess = calculateCess(oneYearsalary);
		taxCalculationReportVO.setFirstName(employee.getFirstName());
		taxCalculationReportVO.setLastName(employee.getLastName());
		taxCalculationReportVO.setTaxAmount(taxAmount);
		taxCalculationReportVO.setCessAmount(calculateCess);
		taxCalculationReportVO.setEmployeeCode(employee.getId());
		taxCalculationReportVO.setYearlySalary(oneYearsalary);
		return taxCalculationReportVO;
	}

	private Double taxCalculation(Double salary) {

		if (salary <= 250000) {
			return 0.0;
		} else if (salary > 250000 && salary <= 500000) {
			return secondSlabCalculation(salary);
		} else if (salary > 500000 && salary <= 1000000) {
			return thirdSlabCalculation(salary);
		} else if (salary > 1000000) {
			return fourthSlabCalculation(salary);
		} else {
			return 0.0;
		}
	}

	private Double secondSlabCalculation(Double salary) {
		return (0.05) * salary;
	}

	private Double thirdSlabCalculation(Double salary) {
		Double secondSlabCalculation = secondSlabCalculation(salary);
		salary = salary - 500000;
		return secondSlabCalculation + (0.1) * salary;
	}

	private Double fourthSlabCalculation(Double salary) {
		Double secondSlabCalculation = secondSlabCalculation(salary);
		Double thirdSlabCalculation = thirdSlabCalculation(salary);
		salary = salary - 1000000;
		return secondSlabCalculation + thirdSlabCalculation + (0.2) * salary;
	}

	private Double calculateCess(Double salary) {
		if (salary > 2500000) {
			Double cessFee = (0.02) * 3000000;
			return cessFee;
		} else {
			return 0.0;
		}
	}

	private void doInputValidation(EmployeeVO vo) {
		this.emailValidation(vo.getEmail());
		this.characterValidation(vo.getFirstName());
		this.characterValidation(vo.getLastName());
		List<String> phoneNumbers = vo.getPhoneNumbers();
		if (CollectionUtils.isEmpty(phoneNumbers)) {
			throw new ValidationException("Phone Numbers Should Not be Empty");
		} else {
			phoneNumbers.stream().forEach(s -> phoneNumberValidation(s));
		}
	}

	private void emailValidation(String email) {
		String emailRegExpression = "^(.+)@(\\S+)$";
		boolean matches = email.matches(emailRegExpression);
		if (!matches) {
			throw new ValidationException("Invalid Email Format");
		}
	}

	private void characterValidation(String name) {
		String regExpression = "^[a-zA-Z]*$";
		boolean matches = name.matches(regExpression);
		if (!matches) {
			throw new ValidationException("Invalid Data");
		}
	}

	private void phoneNumberValidation(String phoneNUmber) {
		String regExp = "^\\d{10}$";
		if (!phoneNUmber.matches(regExp)) {
			throw new ValidationException("Invalid PhoneNumbers");
		}
	}

}
