DELETE FROM employees;

INSERT INTO employees (id, name, surname, department, salary) VALUES
(1, 'John', 'Smith', 'IT', 1000),
(2, 'Maria', 'Brown', 'HR', 1500),
(3, 'John', 'Miller', 'IT', 2000);

ALTER TABLE employees ALTER COLUMN id RESTART WITH 10;
