package com.validate.repositary;

import com.validate.entities.Employee;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepo extends JpaRepository<Employee,Long> {
	
	public Employee findByIdAndCompanyId(Long id,Long companyId);
	public List<Employee> findByCompanyId(Long companyId);
}
