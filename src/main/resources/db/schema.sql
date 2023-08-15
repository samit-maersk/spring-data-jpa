-- department table
CREATE TABLE IF NOT EXISTS department (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- employee table
CREATE TABLE IF NOT EXISTS employee (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    department_id INT NOT NULL,
    CONSTRAINT fk_department FOREIGN KEY (department_id) REFERENCES department(id)
);

-- pgsql function
CREATE OR REPLACE FUNCTION get_employees_by_department(deptId INT)
RETURNS TABLE (id INT, name VARCHAR(255), department_id INT) AS $$
BEGIN
    RETURN QUERY
    SELECT e.id, e.name, e.department_id
    FROM employee AS e
    WHERE e.department_id = deptId;
END;
$$ LANGUAGE plpgsql;

