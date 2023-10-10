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

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/company")
public class CompanyController {
    @Autowired
    CompanyRepo companyRepo;
    @Autowired
    EmployeeRepo employeeRepo;
    
    /**
     *  getting list of company
     * @return List<Company>
     */
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
    	 Optional<Company> findById = companyRepo.findById(id);
    	 if(findById.isPresent())
    		 return findById.get();
    	 return null;
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
//    	company.getEmployees().add(employee);
//    	companyRepo.save(company);
    	return employeeRepo.save(employee);
    }
	
    //getting employee by id in a company
//	  @GetMapping("/{id}/employees/{eid}") 
//	  public Employee getByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
//		  Optional <Company> com = companyRepo.findById(id);
//		  Company byId= com.get();
//		  Optional<Employee> emp=byId.getEmployees().stream().filter(e->e.getId().equals(eid)).findAny();
//		  System.out.println(emp.isPresent());
//		  
//		  return emp.get();
//	  }
	  @GetMapping("/{id}/employees/{eid}") 
	  public Employee getByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
		  return employeeRepo.findByIdAndCompanyId(eid, id);
	  }
	
//	  //deleting employee 
//	  @DeleteMapping("/{id}/employees/{eid}")
//	  public String deleteByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
//		 if(companyRepo.findById(id).isPresent()) {
//			 if(employeeRepo.findById(eid).isPresent()) {
//				 Employee employee = employeeRepo.findById(eid).get();
//				 Company company = companyRepo.findById(id).get();
//				 if(company.getEmployees().contains(employee)) {
//					 company.getEmployees().remove(employee);
//					 employeeRepo.delete(employee);
//					 return "deleted successfully";
//				 }else {
//					 return "Not Authorized ";
//				 }
//			 }else {
//				 return "Employee Doesn't exists";
//			 }
//		 }else {
//			 return "company doesn't exist";
//		 }
//	  }
//	  
	  @DeleteMapping("/{id}/employees/{eid}")
	  public String deleteByid(@PathVariable("id")Long id,@PathVariable("eid")Long eid) {
		  Employee emp = employeeRepo.findByIdAndCompanyId(eid, id);
		  if( emp!=null) {
			  employeeRepo.deleteById(eid);
			  return "deleted";
		  }
		  return "could not delete";
	  }
	  
	  
		  
//	  
//	  //editing employee
//	  @PutMapping("/{id}/employees")
//	  public Employee update(@PathVariable("id")Long id,@RequestBody Employee emp) {
//		  if(companyRepo.findById(id).get().getEmployees().contains(emp)) {
//		  emp.setCompany(companyRepo.findById(id).get());
//		  return employeeRepo.save(emp);
//		  }else {
//			  return null;
//		  }
//
//	  }
	  @PutMapping("/{id}/employees")
	  public Employee updateEmp(@PathVariable("id")Long id,@RequestBody Employee emp) {
		  Employee findByIdAndCompanyId = employeeRepo.findByIdAndCompanyId(emp.getId(), id);
		  if(findByIdAndCompanyId!=null){
			  emp.setCompany(companyRepo.findById(id).get());
			  return employeeRepo.save(emp);
		  }
		  return null;
	  }
	
}
