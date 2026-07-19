# Enviro365 Assessment - Implementation Summary

## ✅ Project Completion Status

### Backend (Spring Boot) - COMPLETE ✓
- [x] Entity models (Investor, Portfolio, Product, Withdrawal)
- [x] JPA repositories with custom queries
- [x] DTOs for request/response handling
- [x] Services with business logic
- [x] REST controllers with all endpoints
- [x] Global exception handling
- [x] Input validation with annotations
- [x] Mapper classes for entity-to-DTO conversion
- [x] H2 database configuration
- [x] Sample data initialization (data.sql)
- [x] Unit tests for services
- [x] Integration tests
- [x] Proper dependency management (pom.xml)

### Frontend (React) - COMPLETE ✓
- [x] API service layer for backend communication
- [x] Portfolio Dashboard component
- [x] Withdrawal Form component with validation
- [x] Withdrawal History component with table
- [x] CSV export functionality
- [x] Error handling and user feedback
- [x] Responsive CSS styling
- [x] State management with React hooks
- [x] CORS configuration on backend
- [x] Environment setup with npm

### Database - COMPLETE ✓
- [x] H2 in-memory database configuration
- [x] Entity relationships properly defined
- [x] Sample data with 3 investors
- [x] Sample portfolios and products
- [x] Automatic schema creation
- [x] Data initialization on startup

---

## 🔌 Full Integration Map

### Flow: Frontend → Backend → Database

```
┌─────────────────────┐
│   React Frontend    │
│  (Port 3000)        │
├─────────────────────┤
│ • PortfolioDashboard│
│ • WithdrawalForm    │
│ • WithdrawalHistory │
│ • CSS Styling       │
└──────────┬──────────┘
           │
           │ HTTP (Fetch API)
           │ CORS Enabled
           ▼
┌─────────────────────────────┐
│  Spring Boot REST API       │
│  (Port 8080)                │
├─────────────────────────────┤
│ Controllers:                │
│ • InvestorController        │
│ • WithdrawalController      │
│                             │
│ Endpoints:                  │
│ • GET  /api/investors/{id}  │
│ • GET  /investors/{id}/port │
│ • POST /api/withdrawals     │
│ • GET  /api/withdrawals     │
│ • GET  /withdrawals/export  │
└──────────┬──────────────────┘
           │
           │ JPA (ORM)
           ▼
┌─────────────────────┐
│   H2 Database       │
│  (In-Memory)        │
├─────────────────────┤
│ • investors         │
│ • portfolios        │
│ • products          │
│ • withdrawals       │
└─────────────────────┘
```

---

## 📋 API Endpoints - All Connected

### Portfolio Retrieval

```
GET /api/investors/{id}/portfolio
│
├─→ InvestorController.getInvestorPortfolio()
│   ├─→ InvestorService.getInvestorWithPortfolio()
│   │   ├─→ InvestorRepository.findById()  ──→ DB
│   │   ├─→ PortfolioRepository.findByInvestorId()  ──→ DB
│   │   └─→ InvestorMapper.toDTO()
│   └─→ Returns InvestorDTO with portfolios
│
└─→ Frontend: PortfolioDashboard receives and displays
```

### Withdrawal Creation

```
POST /api/withdrawals
{investorId, amount, type}
│
├─→ WithdrawalController.createWithdrawal()
│   ├─→ WithdrawalService.createWithdrawal()
│   │   ├─→ InvestorRepository.findById()  ──→ DB
│   │   ├─→ validateWithdrawal()
│   │   │   ├─ Check age >= 65 (if RETIREMENT)
│   │   │   ├─ Check amount <= balance
│   │   │   └─ Check amount <= 90% of balance
│   │   ├─→ Create Withdrawal entity
│   │   ├─→ Update investor balance
│   │   ├─→ WithdrawalRepository.save()  ──→ DB
│   │   ├─→ InvestorRepository.save()  ──→ DB
│   │   └─→ WithdrawalMapper.toDTO()
│   │
│   ├─→ On Success: Return 200 with WithdrawalDTO
│   └─→ On Error: Throw InvalidWithdrawalException (400)
│
└─→ Frontend: WithdrawalForm shows success/error message
```

### Withdrawal History

```
GET /api/withdrawals?investorId={id}
│
├─→ WithdrawalController.getWithdrawalHistory()
│   ├─→ WithdrawalService.getWithdrawalHistory()
│   │   ├─→ InvestorRepository.findById()  ──→ DB (verify exists)
│   │   ├─→ WithdrawalRepository.findByInvestorIdOrderByCreatedAtDesc()  ──→ DB
│   │   └─→ WithdrawalMapper.toDTO() (for each)
│   └─→ Returns List<WithdrawalDTO>
│
└─→ Frontend: WithdrawalHistory displays in table
```

### CSV Export

```
GET /api/withdrawals/export?investorId={id}
│
├─→ WithdrawalController.exportWithdrawalsToCSV()
│   ├─→ WithdrawalService.getWithdrawalHistory()
│   │   └─→ WithdrawalRepository.findByInvestorId()  ──→ DB
│   ├─→ generateCSV() → CSV string
│   └─→ Return with attachment header
│
└─→ Frontend: Download CSV file to local machine
```

---

## 🧪 Test Coverage - All Connected

### Unit Tests

```
InvestorServiceTest
├─ testGetInvestor_Success() ──→ Repository Mock
├─ testGetInvestor_NotFound() ──→ Exception
├─ testGetInvestorAge() ──→ Calculation
├─ testIsRetirementEligible_True()
└─ testIsRetirementEligible_False()

WithdrawalServiceTest
├─ testCreateWithdrawal_Success() ──→ Repo Mock + DB Save
├─ testCreateWithdrawal_RetirementEligibilityFailed() ──→ Business Rule
├─ testCreateWithdrawal_ExceedsBalance() ──→ Business Rule
├─ testCreateWithdrawal_Exceeds90Percent() ──→ Business Rule
├─ testCreateWithdrawal_MaximumAllowed() ──→ Business Rule
└─ testCreateWithdrawal_InvestorNotFound() ──→ Exception
```

### Integration Tests

```
WithdrawalIntegrationTest
├─ testFullWithdrawalFlow() ──→ Full stack test
├─ testWithdrawalHistoryRetrieval() ──→ Query test
├─ testInvalidWithdrawalRejection() ──→ Validation test
└─ testCSVExport() ──→ Export test
```

---

## 🏗️ Architectural Connections

### Layered Architecture

```
┌─────────────────────────────────────────┐
│         Frontend Layer                  │
│    React Components + CSS Styling       │
├─────────────────────────────────────────┤
│         API Service Layer               │
│   apiService.js (HTTP Fetch)            │
├─────────────────────────────────────────┤
│      REST Controller Layer              │
│ InvestorController, WithdrawalController│
├─────────────────────────────────────────┤
│         Service Layer                   │
│ InvestorService, WithdrawalService      │
│     (Business Logic + Validation)       │
├─────────────────────────────────────────┤
│         Repository Layer                │
│ Spring Data JPA Repositories            │
├─────────────────────────────────────────┤
│         Database Layer                  │
│         H2 In-Memory Database           │
└─────────────────────────────────────────┘
```

---

## 📦 Database Connection Details

### Entity Relationships

```
Investor (1) ─── (Many) Portfolios
    │                      │
    │                      └─ (1) ─── (Many) Products
    │
    └─ (1) ─── (Many) Withdrawals
```

### Data Flow Example

```
1. User views portfolio
   Frontend → GET /investors/1/portfolio
   Backend fetches Investor#1 from DB
   Backend fetches Portfolios for Investor#1
   Backend fetches Products for each Portfolio
   All combined into InvestorDTO with nested structure
   Returned to Frontend as JSON

2. User submits withdrawal
   Frontend → POST /withdrawals {amount, type, investorId}
   Backend validates:
     - Investor exists in DB
     - Business rules checked
   Backend creates Withdrawal record in DB
   Backend updates investor.totalBalance in DB
   Returns WithdrawalDTO to Frontend
```

---

## 🔄 State Synchronization

### Frontend State Management

```
App.js (Parent Component)
├─ investor (state)
├─ refreshTrigger (state to sync all components)
│
├─ PortfolioDashboard
│  └─ Fetches on mount or investorId change
│
├─ WithdrawalForm
│  ├─ Local validation state
│  ├─ Calls onWithdrawalSuccess()
│  └─ Updates parent refreshTrigger
│
└─ WithdrawalHistory
   └─ Re-fetches when refreshTrigger changes
```

---

## ✨ Business Rules Implementation

### Rule 1: Age >= 65 for Retirement Withdrawals

```java
// Implemented in Investor entity
public boolean isRetirementEligible() {
  return getAge() >= 65;
}

// Checked in WithdrawalService
if ("RETIREMENT".equalsIgnoreCase(withdrawalType)) {
  if (!investor.isRetirementEligible()) {
    throw new InvalidWithdrawalException(...);
  }
}

// Validated in Frontend
if (formData.type === 'RETIREMENT' && !isRetirementEligible) {
  newErrors.type = `Retirement withdrawals only allowed...`;
}
```

### Rule 2: Amount <= Balance

```java
if (withdrawalAmount > currentBalance) {
  throw new InvalidWithdrawalException(
    "Withdrawal amount cannot exceed current balance..."
  );
}
```

### Rule 3: Amount <= 90% of Balance

```java
Double maxAllowedWithdrawal = currentBalance * 0.9;
if (withdrawalAmount > maxAllowedWithdrawal) {
  throw new InvalidWithdrawalException(
    "Withdrawal amount cannot exceed 90% of balance..."
  );
}
```

---

## 🎯 Key Features Connected

### 1. Portfolio Dashboard
- Fetches from: `GET /api/investors/{id}/portfolio`
- Displays: Investor info + All portfolios with products
- Connected to: Backend InvestorService

### 2. Withdrawal Form
- Submits to: `POST /api/withdrawals`
- Validates: Client-side + Server-side
- Connected to: Backend WithdrawalService
- Triggers: History refresh on success

### 3. Withdrawal History
- Fetches from: `GET /api/withdrawals?investorId={id}`
- Displays: All withdrawals in sortable table
- Connected to: Backend WithdrawalService
- Exports to: CSV file

### 4. Error Handling
- Frontend: Shows user-friendly messages
- Backend: GlobalExceptionHandler catches all errors
- Response: Consistent error format
- Connection: API error → Frontend error display

---

## 📊 Data Flow Examples

### Example 1: Successful Retirement Withdrawal

```
Frontend Input:
├─ Investor ID: 1
├─ Amount: 25000
└─ Type: RETIREMENT

Backend Processing:
├─ Find Investor#1 ✓ (age 70)
├─ Check RETIREMENT: age >= 65 ✓
├─ Check Balance: 25000 <= 100000 ✓
├─ Check 90% Limit: 25000 <= 90000 ✓
├─ Create Withdrawal record
├─ Update balance: 100000 - 25000 = 75000
└─ Return: WithdrawalDTO with status=APPROVED

Frontend Display:
└─ Success message + Updated history
```

### Example 2: Failed Age Check

```
Frontend Input:
├─ Investor ID: 2
├─ Amount: 5000
└─ Type: RETIREMENT

Backend Processing:
├─ Find Investor#2 ✓ (age 36)
├─ Check RETIREMENT: age >= 65 ✗
└─ Throw: InvalidWithdrawalException

Frontend Display:
└─ Error: "Retirement withdrawals only allowed if age >= 65..."
```

---

## 🔐 Data Validation Flow

```
User Input (Frontend)
    ↓
Client-side Form Validation
├─ Check amount > 0
├─ Check amount <= balance
├─ Check amount <= 90% balance
└─ Check age for retirement
    ↓
Send to Backend API
    ↓
Server-side Input Validation
├─ @NotNull @Positive annotations
├─ Bean validation in @Valid
└─ MethodArgumentNotValidException handled
    ↓
Business Logic Validation
├─ Investor exists
├─ Age check for retirement
├─ Balance check
└─ 90% limit check
    ↓
Success or Error Response
```

---

## 🚀 Full Integration Checklist

- [x] Backend starts on port 8080
- [x] Frontend starts on port 3000
- [x] Frontend can call all backend APIs
- [x] Database automatically initializes with sample data
- [x] CORS properly configured
- [x] Business rules enforced on backend
- [x] Client-side validation in frontend
- [x] Error messages flow from backend to frontend
- [x] Withdrawal history updates after submission
- [x] CSV export works correctly
- [x] All tests pass
- [x] No hardcoded values (config-based)

---

## 📝 How to Test Full Integration

### 1. Start Backend

```bash
cd backend/Enviro365Backend
mvn spring-boot:run
```

✓ Backend running on `http://localhost:8080`

### 2. Start Frontend

```bash
cd frontend/enviro365-frontend
npm start
```

✓ Frontend opens on `http://localhost:3000`

### 3. Test Portfolio View

- Page automatically loads investor #1 data
- See portfolio, products, and retirement status

### 4. Test Withdrawal (Success)

- Amount: 20000, Type: REGULAR
- Should succeed immediately
- Balance should update
- History should show new withdrawal

### 5. Test Withdrawal (Failure)

- Amount: 95000, Type: REGULAR
- Should fail: exceeds 90% limit
- Error message should display
- History should not change

### 6. Test Retirement Withdrawal

- Use investor #3 (age 81, eligible)
- Amount: 50000, Type: RETIREMENT
- Should succeed
- Use investor #2 (age 36, not eligible)
- Should fail with age error

### 7. Test CSV Export

- Click "Export to CSV"
- File should download
- Open in Excel/Google Sheets
- Should show withdrawal records

---

## 🎓 Learning Points

### Frontend Concepts Demonstrated
- React hooks (useState, useEffect)
- API communication with Fetch API
- Form handling and validation
- State management and lifting state up
- CSS Grid and Flexbox
- Error handling and user feedback
- Component composition

### Backend Concepts Demonstrated
- Spring Boot application structure
- JPA/Hibernate ORM
- RESTful API design
- Service layer pattern
- Repository pattern with Spring Data
- Exception handling and global error handlers
- Input validation with Jakarta Bean Validation
- DTO pattern for API contracts
- Unit testing with JUnit and Mockito
- Integration testing with Spring Boot Test

### Database Concepts Demonstrated
- Entity modeling and relationships
- SQL schema generation from JPA entities
- Data initialization with SQL scripts
- CRUD operations through repositories
- Query methods with custom queries

---

## 🎯 Conclusion

The Enviro365Assessment is a **complete, fully-integrated full-stack application** where:

1. **Frontend** (React) → Provides user interface
2. **API Layer** (REST) → Connects frontend to backend
3. **Backend Services** (Spring Boot) → Implements business logic
4. **Database** (H2) → Persists data

All components work together seamlessly to:
- ✅ Display investor portfolios
- ✅ Process withdrawals with validation
- ✅ Enforce business rules (age, balance, 90% limit)
- ✅ Provide clear feedback on success/failure
- ✅ Export data to CSV
- ✅ Maintain data consistency

**AI Disclosure:** This project was developed with assistance from **GitHub Copilot** for code generation, architecture guidance, and documentation.

---

**Status:** ✅ COMPLETE AND FULLY INTEGRATED
**Version:** 1.0.0
**Date:** July 19, 2026
