package com.example.springdatajpa.routers;

import com.example.springdatajpa.DataNotFoundException;
import com.example.springdatajpa.model.Department;
import com.example.springdatajpa.model.Employee;
import com.example.springdatajpa.repository.DepartmentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
@RequestMapping("/department")
@Slf4j
public class DepartmentRestController {
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
