# Enviro365Assessment - Complete Full-Stack Application

## 📋 Project Overview

This is a complete full-stack application built with **Spring Boot** backend and **React** frontend to manage investor portfolios, withdrawals, and CSV reports. The application enforces strict business rules for retirement withdrawals and balance management.

---

## 🏗️ Project Structure

```
Enviro365Assessment-Junior-DickKhosa/
├── backend/
│   └── Enviro365Backend/                 # Spring Boot backend
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/                 # Java source code
│       │   │   │   └── com/enviro/assessment/junior/dickkhosa/
│       │   │   │       ├── entity/       # JPA entities
│       │   │   │       ├── dto/          # Data Transfer Objects
│       │   │   │       ├── repository/   # JPA repositories
│       │   │   │       ├── service/      # Business logic
│       │   │   │       ├── controller/   # REST controllers
│       │   │   │       ├── exception/    # Exception handling
│       │   │   │       └── mapper/       # DTO mappers
│       │   │   └── resources/
│       │   │       ├── application.yaml  # Spring Boot config
│       │   │       └── data.sql          # Sample data
│       │   └── test/java/                # Unit tests
│       └── pom.xml                       # Maven config
├── frontend/
│   └── enviro365-frontend/               # React frontend
│       ├── src/
│       │   ├── components/               # React components
│       │   ├── services/                 # API service
│       │   ├── styles/                   # CSS files
│       │   ├── App.js                    # Main App component
│       │   └── index.js                  # React entry point
│       └── package.json                  # NPM dependencies
└── README.md                             # This file
```

---

## 🚀 Getting Started

### Prerequisites
- **Java 17+** installed
- **Node.js 16+** and npm installed
- **Maven** for building Spring Boot
- Port 8080 (backend) and 3000 (frontend) available

### Backend Setup

1. **Navigate to backend directory:**
   ```bash
   cd backend/Enviro365Backend
   ```

2. **Build with Maven:**
   ```bash
   mvn clean install
   ```

3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

   The backend will start at `http://localhost:8080`
   
   **H2 Database Console:** `http://localhost:8080/h2-console`
   - JDBC URL: `jdbc:h2:mem:enviro365db`
   - Username: `sa`
   - Password: (leave blank)

### Frontend Setup

1. **Navigate to frontend directory:**
   ```bash
   cd frontend/enviro365-frontend
   ```

2. **Install dependencies:**
   ```bash
   npm install
   ```

3. **Start the development server:**
   ```bash
   npm start
   ```

   The frontend will open at `http://localhost:3000`

---

## 📡 API Endpoints

### Investor Endpoints

#### Get Investor Portfolio
```http
GET /api/investors/{id}/portfolio
```
**Response:**
```json
{
  "id": 1,
  "firstName": "John",
  "lastName": "Doe",
  "email": "john@example.com",
  "dateOfBirth": "1955-05-15",
  "phoneNumber": "0123456789",
  "totalBalance": 50000.00,
  "age": 69,
  "isRetirementEligible": true,
  "portfolios": [
    {
      "id": 1,
      "name": "Retirement Portfolio",
      "balance": 30000.00,
      "products": [
        {
          "id": 1,
          "name": "Dividend Stock Fund",
          "type": "EQUITY",
          "value": 15000.00,
          "unitPrice": 150.00
        }
      ]
    }
  ]
}
```

#### Get Investor Details
```http
GET /api/investors/{id}
```
**Response:** Same as above (without portfolios list)

---

### Withdrawal Endpoints

#### Create Withdrawal
```http
POST /api/withdrawals
Content-Type: application/json

{
  "investorId": 1,
  "amount": 20000.00,
  "type": "RETIREMENT"
}
```

**Success Response (200):**
```json
{
  "id": 1,
  "investorId": 1,
  "amount": 20000.00,
  "type": "RETIREMENT",
  "status": "APPROVED",
  "createdAt": "2026-07-19T10:30:00",
  "approvedAt": "2026-07-19T10:30:00",
  "balanceAfter": 30000.00,
  "rejectionReason": null
}
```

**Error Response (400):**
```json
{
  "status": 400,
  "message": "Retirement withdrawals only allowed if age >= 65. Current age: 45",
  "error": "Invalid Withdrawal",
  "timestamp": "2026-07-19T10:30:00",
  "path": "/api/withdrawals"
}
```

#### Get Withdrawal History
```http
GET /api/withdrawals?investorId={id}
```
**Response:** Array of withdrawal objects

#### Get Single Withdrawal
```http
GET /api/withdrawals/{id}
```

#### Export Withdrawals as CSV
```http
GET /api/withdrawals/export?investorId={id}
```
**Response:** CSV file download

---

## 📋 Business Rules

### 1. **Retirement Withdrawal Eligibility**
- **Rule:** Only investors aged 65+ can make retirement withdrawals
- **Error:** "Retirement withdrawals only allowed if age >= 65. Current age: {age}"

### 2. **Balance Limit**
- **Rule:** Withdrawal amount cannot exceed current balance
- **Error:** "Withdrawal amount cannot exceed current balance. Requested: {amount}, Available: {balance}"

### 3. **90% Limit**
- **Rule:** Withdrawal amount cannot exceed 90% of current balance
- **Maximum allowed:** `currentBalance × 0.9`
- **Error:** "Withdrawal amount cannot exceed 90% of balance. Requested: {amount}, Maximum allowed: {maxAllowed}"

---

## 🗄️ Database Schema

### Entities

**Investors Table**
```sql
CREATE TABLE investors (
  id BIGINT PRIMARY KEY,
  first_name VARCHAR(100),
  last_name VARCHAR(100),
  email VARCHAR(100) UNIQUE,
  date_of_birth DATE,
  phone_number VARCHAR(20),
  total_balance DOUBLE
);
```

**Portfolios Table**
```sql
CREATE TABLE portfolios (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100),
  balance DOUBLE,
  investor_id BIGINT
);
```

**Products Table**
```sql
CREATE TABLE products (
  id BIGINT PRIMARY KEY,
  name VARCHAR(100),
  type VARCHAR(50),
  value DOUBLE,
  unit_price DOUBLE,
  portfolio_id BIGINT
);
```

**Withdrawals Table**
```sql
CREATE TABLE withdrawals (
  id BIGINT PRIMARY KEY,
  investor_id BIGINT,
  amount DOUBLE,
  type VARCHAR(50),
  status VARCHAR(50),
  created_at TIMESTAMP,
  approved_at TIMESTAMP,
  balance_after DOUBLE,
  rejection_reason VARCHAR(500)
);
```

---

## 🧪 Unit Tests

### Running Tests

```bash
# Backend tests
cd backend/Enviro365Backend
mvn test
```

### Test Coverage

- **InvestorServiceTest**: Tests investor retrieval and retirement eligibility
- **WithdrawalServiceTest**: Tests all business rules and validation

**Test Scenarios:**
- ✅ Age validation for retirement withdrawals
- ✅ Balance limit enforcement
- ✅ 90% limit enforcement
- ✅ Successful withdrawals
- ✅ Resource not found errors

---

## 🎨 Frontend Features

### Components

#### 1. **Portfolio Dashboard**
- Displays investor information
- Shows all portfolios with products
- Retirement eligibility badge
- Current balance display

#### 2. **Withdrawal Form**
- Amount input with validation
- Withdrawal type selector (Regular/Retirement)
- Client-side business rule validation
- Real-time error messages
- Success confirmation

#### 3. **Withdrawal History**
- Table of all withdrawals
- Status badges (APPROVED, REJECTED, PENDING)
- Export to CSV button
- Sortable by date (newest first)

---

## 🔧 Technologies Used

### Backend
- **Framework:** Spring Boot 4.1.1
- **Database:** H2 (in-memory)
- **ORM:** JPA/Hibernate
- **Testing:** JUnit 5, Mockito
- **Build:** Maven
- **Java Version:** 17

### Frontend
- **Framework:** React 19.2.7
- **Styling:** CSS3
- **HTTP Client:** Fetch API
- **Testing:** React Testing Library

---

## 📝 Configuration

### Backend Configuration (application.yaml)
```yaml
spring:
  application:
    name: Enviro365Backend
  datasource:
    url: jdbc:h2:mem:enviro365db
    driverClassName: org.h2.Driver
    username: sa
    password:
  jpa:
    database-platform: org.hibernate.dialect.H2Dialect
    hibernate:
      ddl-auto: create-drop
    show-sql: true
  h2:
    console:
      enabled: true
      path: /h2-console

server:
  port: 8080
```

---

## 🐛 Error Handling

### Global Exception Handler

The backend implements a global exception handler with the following exceptions:

- **ResourceNotFoundException**: When investor/withdrawal not found (404)
- **InvalidWithdrawalException**: When business rules are violated (400)
- **MethodArgumentNotValidException**: When input validation fails (400)
- **Generic Exception**: For any unhandled errors (500)

All errors return a consistent format:
```json
{
  "status": 400,
  "message": "Error description",
  "error": "Error type",
  "timestamp": "2026-07-19T10:30:00",
  "path": "/api/endpoint"
}
```

---

## ✅ Sample Data

The database is pre-populated with:
- **3 Investors**: John Doe (69), Jane Smith (36), Robert Johnson (81)
- **4 Portfolios**: Retirement, Growth, Balanced, Conservative
- **7 Products**: Various stocks, bonds, and money market funds

---

## 📦 Dependencies

### Backend (pom.xml)
- spring-boot-starter-webmvc
- spring-boot-starter-data-jpa
- spring-boot-h2console
- h2database
- lombok
- jakarta.validation

### Frontend (package.json)
- react (19.2.7)
- react-dom (19.2.7)
- react-scripts (5.0.1)
- testing-library/react
- testing-library/jest-dom

---

## 🔐 Security Notes

**Current Implementation:**
- CORS enabled for `http://localhost:3000`
- No authentication/authorization (demo purposes)
- Input validation on both client and server

**Production Recommendations:**
- Add Spring Security with JWT
- Implement proper authentication
- Add HTTPS/TLS
- Secure password handling
- API rate limiting
- CORS configuration refinement

---

## 📚 API Documentation

### Response Status Codes

| Code | Description |
|------|-------------|
| 200 | Success |
| 400 | Bad Request / Business Rule Violation |
| 404 | Resource Not Found |
| 500 | Internal Server Error |

### Request Headers
```
Content-Type: application/json
```

### CORS Headers
The backend allows requests from `http://localhost:3000`

---

## 🤝 Frontend-Backend Integration

The frontend communicates with the backend through:

1. **API Service** (`apiService.js`)
   - Centralized API calls
   - Error handling
   - CSV file download management

2. **State Management**
   - React useState for local component state
   - Refresh triggers for data synchronization
   - Error boundaries

3. **Validation**
   - Client-side validation in forms
   - Server-side validation in backend
   - Clear error messages to users

---

## 📖 Usage Example

### Withdraw Money as a Retired Investor

1. **Open Frontend** at `http://localhost:3000`
2. **View Portfolio** showing investor details and balance
3. **Fill Withdrawal Form:**
   - Amount: 25000.00
   - Type: Retirement
   - Click "Submit Withdrawal"
4. **View Confirmation** on success
5. **Check History** to see the withdrawal record
6. **Export CSV** for your records

---

## 🚨 Common Issues & Solutions

### Backend Won't Start
- Ensure port 8080 is available
- Check Java version (17+)
- Run `mvn clean install`

### Frontend Connection Error
- Ensure backend is running on port 8080
- Check CORS configuration
- Verify `http://localhost:3000` is in allowed origins

### Database Issues
- H2 Console at `http://localhost:8080/h2-console`
- Check `data.sql` for sample data
- Verify H2 driver in dependencies

---

## 📄 AI Disclosure

**This project was developed with assistance from GitHub Copilot**, an AI-powered code assistant. Copilot assisted with:
- Code generation and structure
- Exception handling implementation
- React component architecture
- CSS styling and layout
- Documentation generation

The logic, business rules, and architecture were designed and validated to ensure correctness and adherence to requirements.

---

## 👨‍💼 Author

**Dick Khosa**
- Full-Stack Developer Assessment Submission
- Enviro365 Investments (2026)

---

## 📄 License

This project is submitted as part of an assessment and is not intended for production use without proper modifications.

---

## 📞 Support

For issues or questions regarding this project, please refer to the code comments and this documentation.

---

**Version:** 1.0.0  
**Last Updated:** July 19, 2026
