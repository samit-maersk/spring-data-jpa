package com.example.springdatajpa;

import com.example.springdatajpa.model.Department;
import com.example.springdatajpa.model.Employee;
import com.example.springdatajpa.repository.DepartmentRepository;
import com.example.springdatajpa.repository.EmployeeRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.containers.wait.strategy.Wait;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.MountableFile;

import java.util.Arrays;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
@Testcontainers
class SpringDataJpaApplicationTests {

	@Container
	@ServiceConnection
	static PostgreSQLContainer PGSQL = new PostgreSQLContainer<>("postgres:latest")
			// mount this file so that we have the table before we do any crud operation.
			.withCopyFileToContainer(MountableFile.forClasspathResource("db/schema.sql"), "/docker-entrypoint-initdb.d/schema.sql")
			.waitingFor(Wait.forListeningPort());

	@Autowired
	DepartmentRepository departmentRepository;

	@Autowired
	EmployeeRepository employeeRepository;

	@Test
	void contextLoads() {
	}

	@Test
	@DisplayName("create/read/update/delete Test")
	void crudTest() {
		assertAll("create/read/update/delete",
				() -> {
					//create
					var departments = Arrays
							.asList(
									Department.builder().name("IT").build(),
									Department.builder().name("FINANCE").build()
							)
							.stream()
							.map(departmentRepository::save)
							.collect(Collectors.toList());
					assertEquals(2, departments.size());

					var employees = Arrays
							.asList(
									Employee.builder().department(departments.get(0)).name("John Doe").build(),
									Employee.builder().department(departments.get(0)).name("K Rama").build(),
									Employee.builder().department(departments.get(1)).name("Billu Barber").build()
							)
							.stream()
							.map(employeeRepository::save)
							.collect(Collectors.toList());
					assertEquals(3, employees.size());

				},
				() -> {
					//find
					assertEquals(2, departmentRepository.findAll().size());
					assertEquals(3, employeeRepository.findAll().size());

					//Department findById
					var department1 = departmentRepository.findById(1).get();
					assertEquals("IT", department1.getName());

					//Employee findById
					var employee1 = employeeRepository.findById(1).get();
					assertEquals("John Doe", employee1.getName());
					assertEquals(1, employee1.getDepartment().getId());
					assertEquals("IT", employee1.getDepartment().getName());

					//Find all employee by departmentId
					var allEmployee = departmentRepository.allEmployeeByDepartmentId(1);
					assertEquals(2, allEmployee.size());
				});
	}

}
