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
    
    //gettting list of company
    @GetMapping
    public List<Company> get(){
        return companyRepo.findAll();
    }
    //add a new company
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
    //deleting a company
    @DeleteMapping("/{id}")
    public String delete(@PathVariable Long id) {
    	if(companyRepo.findById(id).isPresent()) {
    	companyRepo.deleteById(id);
    		return "successfully deleted";
    	}else {
    		return "server down";
		}
    	
    }
    //updating a company
    @PutMapping
    public Company update(@RequestBody Company company) {
    	company.getEmployees().forEach(e->e.setCompany(company));
    	
    	
    	return companyRepo.save(company);
    }
    //getting companies by id
    @GetMapping("/{id}")
    public Company getById(@PathVariable Long id ) {
    	return companyRepo.findById(id).get();
    }
    //getting list of employee by company id
    @GetMapping("/{id}/employees")
    public List<Employee> getAllEmployee(@PathVariable("id") Long id){
    	return companyRepo.findById(id).get().getEmployees();
    	
    }
    //adding employees in a company
    @PostMapping("/{id}/employees")
    public Employee addEmployee(@PathVariable("id")Long id,@RequestBody Employee employee) {
    	Company company =companyRepo.findById(id).get();
    	employee.setCompany(company);
    	company.getEmployees().add(employee);
    	companyRepo.save(company);
    	return employee;
    }
	
    //getting employee by id in a company
	  @GetMapping("/{id}/employees/{eid}") 
	  public Employee getByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
		  Optional <Company> com = companyRepo.findById(id);
		  Company byId= com.get();
		  Optional<Employee> emp=byId.getEmployees().stream().filter(e->e.getId().equals(eid)).findAny();
		  System.out.println(emp.isPresent());
		  
		  return emp.get();
	  }
	  @DeleteMapping("/{id}/employees/{eid}")
	  public void deleteByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
		   employeeRepo.deleteById(eid);
		   
	  }
	  @PutMapping("/{id}/employees")
	  public Employee Update(@PathVariable("id")Long id,@RequestBody Employee emp) {
		  emp.setCompany(companyRepo.findById(id).get());
		  return employeeRepo.save(emp);
	  }
	
}
