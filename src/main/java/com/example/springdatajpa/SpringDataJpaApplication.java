package com.example.springdatajpa;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@SpringBootApplication
public class SpringDataJpaApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringDataJpaApplication.class, args);
	}

}


@Entity
@Data
@ToString(exclude = "employee")
class Department {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String name;
	@OneToMany(mappedBy = "department", cascade = CascadeType.ALL)
	@JsonIgnore
	List<Employee> employee;
}


@Entity
@Data
@ToString(exclude = "department")
class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Integer id;
	String name;
	@ManyToOne
	@JoinColumn(name = "department_id")
	Department department;

}

@Repository
interface DepartmentRepository extends ListCrudRepository<Department, Integer> {}
@Repository
interface EmployeeRepository extends ListCrudRepository<Employee, Integer> {}

@ResponseStatus(HttpStatus.NOT_FOUND)
class DataNotFoundException extends RuntimeException {
	public DataNotFoundException(String message) {
		super(message);
	}
}


@RestController
@RequiredArgsConstructor
@RequestMapping("/department")
@Slf4j
class DepartmentRestController {
	private final DepartmentRepository departmentRepository;

	@GetMapping
	public ResponseEntity<List<Department>> department() {
		return ResponseEntity.ok(departmentRepository.findAll());
	}

	@GetMapping("/{id}/employee")
	public ResponseEntity<List<Employee>> employee(@PathVariable("id") Integer id) {
		return departmentRepository.findById(id)
				.map(Department::getEmployee)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new DataNotFoundException("Department not found"));
	}

	@GetMapping("/{id}")
	public ResponseEntity<Department> department(@PathVariable("id") Integer id) {
		return departmentRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new DataNotFoundException("Department not found"));
	}

	@PostMapping
	public ResponseEntity<Department> department(@RequestBody Department department) {
		log.info("department: {}", department);
		return ResponseEntity.ok(departmentRepository.save(department));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Department> department(@PathVariable("id") Integer id, @RequestBody Department department) {
		return departmentRepository.findById(id)
				.map(d -> {
					d.setName(department.getName());
					d.setEmployee(department.getEmployee());
					return d;
				})
				.map(departmentRepository::save)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new DataNotFoundException("Department not found"));
	}
	@DeleteMapping("/{id}")
	public ResponseEntity<Department> delDepartment(@PathVariable("id") Integer id) {
		departmentRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
class EmployeeRestController {
	private final EmployeeRepository employeeRepository;

	@GetMapping
	public ResponseEntity<List<Employee>> employees() {
		return ResponseEntity.ok(employeeRepository.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<Employee> employee(@PathVariable("id") Integer id) {
		return employeeRepository.findById(id)
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new DataNotFoundException("Employee not found"));
	}

	@PostMapping
	public ResponseEntity<Employee> employee(@RequestBody Employee employee) {
		return ResponseEntity.ok(employeeRepository.save(employee));
	}

	@PutMapping("/{id}")
	public ResponseEntity<Employee> employee(@PathVariable("id") Integer id, @RequestBody Employee employee) {
		return employeeRepository.findById(id)
				.map(e -> {
					e.setName(employee.getName());
					e.setDepartment(employee.getDepartment());
					return e;
				})
				.map(ResponseEntity::ok)
				.orElseThrow(() -> new DataNotFoundException("Employee not found"));
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Employee> delEmployee(@PathVariable("id") Integer id) {
		employeeRepository.deleteById(id);
		return ResponseEntity.ok().build();
	}
}