package com.example.springdatajpa.routers;

import com.example.springdatajpa.DataNotFoundException;
import com.example.springdatajpa.model.Employee;
import com.example.springdatajpa.repository.EmployeeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/employee")
public class EmployeeRestController {
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
                .map(employeeRepository::save)
                .map(ResponseEntity::ok)
                .orElseThrow(() -> new DataNotFoundException("Employee not found"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Employee> delEmployee(@PathVariable("id") Integer id) {
        employeeRepository.deleteById(id);
        return ResponseEntity.ok().build();
    }
}
