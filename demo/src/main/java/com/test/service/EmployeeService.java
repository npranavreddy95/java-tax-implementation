package com.test.service;

import java.util.List;

import com.test.vo.EmployeeVO;
import com.test.vo.TaxCalculationReportVO;

public interface EmployeeService {
	
	public Long saveEmployeeDetails(EmployeeVO vo);
	
	public List<TaxCalculationReportVO> getSalaryAndTaxDetails(); 

}
