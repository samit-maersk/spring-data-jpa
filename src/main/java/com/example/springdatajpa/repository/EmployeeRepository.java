package com.example.springdatajpa.repository;

import com.example.springdatajpa.model.Employee;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface EmployeeRepository extends ListCrudRepository<Employee, Integer> {
}
