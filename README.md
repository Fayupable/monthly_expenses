# Monthly Expenses
Monthly Expenses Java Project


**Overview**
The Monthly Expense Tracker is a Java-based application that helps users manage their monthly expenses efficiently. The application supports basic CRUD (Create, Read, Update, Delete) operations, enabling users to add, view, update, and delete their expenses. Additionally, it includes functionalities to search expenses and export data to Excel or XML formats for better data management and reporting.


# **Table of Contents**
1. [Database](#Database)
2. [Usage](#usage)
3. [Modules](#modules)
4. [Contributing](#contributing)


# **Database**
Create Database

**Mysql**
```
-- Create the new database
CREATE DATABASE IF NOT EXISTS monthly_expense;

-- Use the new database
USE monthly_expense;

```
**Persons**
```
-- Create the Persons table
CREATE TABLE Persons (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    e_mail VARCHAR(100) NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL,
    person_type VARCHAR(100),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

```
**Categories**
```
-- Create the Categories table
CREATE TABLE Categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);


```
**Payment_methods**
```
-- Create the Payment_Methods table
CREATE TABLE Payment_Methods (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);
```

**Expenses**
```
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

```

# **Usage**

**DbConnector**
Requirements

	•	Java Development Kit (JDK) 8 or higher
	•	MySQL database server
	•	MySQL JDBC Connector (Connector/J)

Project Setup

Make sure to include the necessary dependencies for the MySQL JDBC connector in your project. If you are using Maven, add the following dependency to your pom.xml file:

```
<dependency>
    <groupId>mysql</groupId>
    <artifactId>mysql-connector-java</artifactId>
    <version>8.0.26</version>
</dependency>
```
If you are using Gradle, add the following line to your build.gradle file:
```
implementation 'mysql:mysql-connector-java:8.0.26'
```


