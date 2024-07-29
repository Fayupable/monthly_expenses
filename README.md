-- Create the new database
CREATE DATABASE IF NOT EXISTS new_database;

-- Use the new database
USE new_database;

-- Create the Persons table
CREATE TABLE Persons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    e_mail VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    person_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create the Categories table
CREATE TABLE Categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create the Payment_Methods table
CREATE TABLE Payment_Methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

-- Create the Expenses table
CREATE TABLE Expenses (
    id INT AUTO_INCREMENT PRIMARY KEY,
    person_id INT,
    amount DECIMAL(10,4) NOT NULL,
    description VARCHAR(255),
    category_id INT,
    payment_method_id INT,
    date DATE,
    FOREIGN KEY (person_id) REFERENCES Persons(id),
    FOREIGN KEY (category_id) REFERENCES Categories(id),
    FOREIGN KEY (payment_method_id) REFERENCES Payment_Methods(id)
);

-- Create the Expenses_Details table
CREATE TABLE Expenses_Details (
    id INT AUTO_INCREMENT PRIMARY KEY,
    expense_id INT,
    item VARCHAR(100) NOT NULL,
    amount DECIMAL(10,4) NOT NULL,
    person_id INT NOT NULL,
    FOREIGN KEY (person_id) REFERENCES Persons(id),
    FOREIGN KEY (expense_id) REFERENCES Expenses(id)
);

-- Change the delimiter for triggers
DELIMITER //

-- Create the after_expenses_insert trigger
CREATE TRIGGER after_expenses_insert 
AFTER INSERT ON Expenses 
FOR EACH ROW 
BEGIN 
    INSERT INTO Expenses_Details (expense_id, item, amount, person_id) 
    VALUES (NEW.id, '', NEW.amount, NEW.person_id);
END; 
//

-- Create the after_expenses_update trigger
CREATE TRIGGER after_expenses_update 
AFTER UPDATE ON Expenses 
FOR EACH ROW 
BEGIN 
    INSERT INTO Expenses_Details (expense_id, item, amount, person_id) 
    VALUES (NEW.id, '', NEW.amount, NEW.person_id);
END; 
//

-- Revert the delimiter
DELIMITER ;
