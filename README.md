# Online Ticket Booking System

This is a Java Swing desktop application for managing an online ticket booking system. It uses MySQL database through JDBC, so routes, passengers, and issued tickets are saved permanently.

## Technologies Used

- Java
- Java Swing
- MySQL
- JDBC
- MySQL Connector/J `9.7.0`
- XAMPP MySQL server

## Main Features

- Dashboard with live summary cards
- Add route and vehicle information
- Add passenger details
- View all passenger details
- Remove passengers from the passenger list
- Issue tickets for registered passengers
- Select route and seat while issuing tickets
- View issued tickets
- Cancel issued tickets
- Available seats update after ticket issue or cancellation
- Data is stored in MySQL database

## Project Files

- `MainDashboard.java` - Main dashboard and sidebar menu
- `DBConnection.java` - MySQL database connection
- `TicketDataStore.java` - Database table creation, insert, select, update, delete logic
- `AddPassengerForm.java` - Add new passenger form
- `PassengerDetailsForm.java` - View and remove passenger details
- `AddRouteForm.java` - Add new route form
- `IssueTicketForm.java` - Issue ticket form
- `IssuedTicketsForm.java` - View and cancel tickets
- `AppStyle.java` - Common UI style
- `run.bat` - Compile and run the project with MySQL connector

## Database

Database name:

```sql
ticket_db
```

The project creates these tables automatically if they do not exist:

- `routes`
- `passengers`
- `tickets`

The database connection is configured in `DBConnection.java`:

```java
private static final String URL = "jdbc:mysql://localhost:3306/ticket_db?createDatabaseIfNotExist=true";
private static final String USER = "root";
private static final String PASSWORD = "";
```

For XAMPP, the default username is usually `root` and password is blank.

## How To Run

1. Open XAMPP.
2. Start MySQL.
3. Open terminal in the project folder:

```powershell
cd C:\texxt
```

4. Run:

```powershell
.\run.bat
```

The `run.bat` file compiles and runs the project using:

```bat
javac -cp ".;mysql-connector-j-9.7.0.jar" *.java
java -cp ".;mysql-connector-j-9.7.0.jar" MainDashboard
```

## MySQL Connector

This project needs MySQL Connector/J jar file:

```text
mysql-connector-j-9.7.0.jar
```

Keep this jar file inside the project folder. Without it, this error can happen:

```text
No suitable driver found for jdbc:mysql://localhost:3306/ticket_db
```

## Common Problems

### No suitable driver found

Reason: MySQL connector jar is missing from classpath.

Solution: Use `run.bat` or add `mysql-connector-j-9.7.0.jar` to project library/classpath.

### Access denied for user root

Reason: MySQL password is wrong.

Solution: Update password in `DBConnection.java`.

### Database connection failed

Reason: MySQL server is not running.

Solution: Start MySQL from XAMPP Control Panel.

## Workflow

1. Add routes from `+ Add Route`.
2. Add passengers from `+ Add Passenger`.
3. View passenger list from `Passengers`.
4. Issue tickets from `Issue Ticket`.
5. View or cancel tickets from `Issued Tickets`.
6. Dashboard cards update after changes.

## Author

Student Java project for an online ticket booking system.
