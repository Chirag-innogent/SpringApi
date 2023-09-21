package com.validate.controller;

import com.validate.entities.Company;
import com.validate.entities.Employee;
import com.validate.repositary.CompanyRepo;
import com.validate.repositary.EmployeeRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    EmployeeRepo employeeRepo;
    @GetMapping
    public List<Company> get(){
        return companyRepo.findAll();
    }
    @PostMapping
    public Company add(@RequestBody Company company){
    	if(company.getEmployees().isEmpty())
        return companyRepo.save(company);
    	else {
        List<Employee> employee=company.getEmployees();
        employee.stream().forEach(e->e.setCompany(company));
        System.out.println(employee);
        company.setEmployees(employee);
        return companyRepo.save(company);
    	}
    }
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
    	if(companyRepo.findById(id).isPresent()) {
    	companyRepo.deleteById(id);
    		return "successfully deleted";
    	}else {
    		return "server down";
		}
    	
    }
    @PutMapping
    public Company update(@RequestBody Company company) {
    	return companyRepo.save(company);
    }
    @GetMapping("/{id}")
    public Company getById(@PathVariable Long id ) {
    	return companyRepo.findById(id).get();
    }
    @GetMapping("/{id}/employees")
    public List<Employee> getAllEmployee(@PathVariable("id") Long id){
    	return companyRepo.findById(id).get().getEmployees();
    	
    }
    @PostMapping("/{id}/employees")
    public Employee addEmployee(@PathVariable("id")Long id,@RequestBody Employee employee) {
    	Company company =companyRepo.findById(id).get();
    	employee.setCompany(company);
    	company.getEmployees().add(employee);
    	companyRepo.save(company);
    	return employee;
    }
	
	  @GetMapping("/{id}/employees/{eid}") 
	  public Employee getByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
		  return companyRepo.findById(id).get().getEmployees().stream().filter(e->e.getId()==eid).findAny().get();
	  }
	  @DeleteMapping("/{id}/employees/{eid}")
	  public void deleteByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
		   employeeRepo.deleteById(eid);
		   
	  }
	  
    
}
