package com.test.mapper;

import org.apache.tomcat.util.buf.StringUtils;
import org.springframework.stereotype.Component;

import com.test.dto.Employee;
import com.test.vo.EmployeeVO;

@Component
public class EmployeeEntityMapper {

	
	public Employee convertVoToEntity(EmployeeVO vo) {
		Employee employee = new Employee();
		employee.setId(vo.getEmployeeId());
		employee.setDoj(vo.getDoj());
		employee.setFirstName(vo.getFirstName());
		employee.setLastName(vo.getLastName());
		employee.setEmail(vo.getEmail());
		employee.setSalary(vo.getSalary());
		employee.setPhoneNumbers(StringUtils.join(vo.getPhoneNumbers(),','));
		return employee;
	}
	
}
