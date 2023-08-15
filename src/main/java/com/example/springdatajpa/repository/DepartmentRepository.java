package com.example.springdatajpa.repository;

import com.example.springdatajpa.model.Department;
import com.example.springdatajpa.model.Employee;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.SqlResultSetMapping;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.query.Procedure;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DepartmentRepository extends ListCrudRepository<Department, Integer> {

    @Query(value = "SELECT * FROM get_employees_by_department(:deptId)", nativeQuery = true)
    List<Employee> allEmployeeByDepartmentId(@Param("deptId") int departmentId);
}
