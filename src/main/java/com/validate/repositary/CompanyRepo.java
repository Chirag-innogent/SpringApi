package com.validate.repositary;

import com.validate.entities.Company;
import com.validate.entities.Employee;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompanyRepo extends JpaRepository<Company,Long> {
}
