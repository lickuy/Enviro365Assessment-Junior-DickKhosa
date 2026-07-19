# 📄 Enviro365 - Complete File Inventory

## Project Overview
This document lists ALL files created/modified in the Enviro365Assessment project, with descriptions of what each file does and how they connect.

---

## 📦 Backend Files (Spring Boot)

### Entity Classes (JPA Models)
These define the database structure and relationships.

| File | Purpose | Key Features |
|------|---------|--------------|
| `entity/Investor.java` | Represents investor data | Age calculation, retirement eligibility check |
| `entity/Portfolio.java` | Represents investment portfolio | Links investor to products |
| `entity/Product.java` | Represents investment products | Stock, bonds, money market items |
| `entity/Withdrawal.java` | Records withdrawal transactions | Tracks amounts, status, dates, balance |

### Repository Classes (Data Access)
Spring Data JPA repositories for database queries.

| File | Purpose | Methods |
|------|---------|---------|
| `repository/InvestorRepository.java` | Access investor data | findById(), findByEmail() |
| `repository/PortfolioRepository.java` | Access portfolio data | findByInvestorId() |
| `repository/WithdrawalRepository.java` | Access withdrawal data | findByInvestorId(), findByInvestorIdOrderByCreatedAtDesc() |

### DTO Classes (Data Transfer Objects)
Transfer data between frontend and backend.

| File | Purpose | Used For |
|------|---------|----------|
| `dto/InvestorDTO.java` | Transfer investor data | API responses |
| `dto/PortfolioDTO.java` | Transfer portfolio data | Nested in InvestorDTO |
| `dto/ProductDTO.java` | Transfer product data | Nested in PortfolioDTO |
| `dto/WithdrawalDTO.java` | Transfer withdrawal data | API responses |
| `dto/WithdrawalRequestDTO.java` | Accept withdrawal requests | API requests with @Valid validation |
| `dto/ErrorResponseDTO.java` | Standard error format | Exception responses |

### Service Classes (Business Logic)
Implement business rules and logic.

| File | Purpose | Responsibilities |
|------|---------|-----------------|
| `service/InvestorService.java` | Investor operations | Get investor, fetch portfolio, convert to DTOs |
| `service/WithdrawalService.java` | Withdrawal operations | Create withdrawal, validate rules, fetch history |

**Business Rules Implemented:**
- Age ≥ 65 for retirement withdrawals
- Amount ≤ current balance
- Amount ≤ 90% of balance

### Controller Classes (REST Endpoints)
Handle HTTP requests and route to services.

| File | Endpoints | Methods |
|------|-----------|---------|
| `controller/InvestorController.java` | `/api/investors` | GET /api/investors/{id}, GET /api/investors/{id}/portfolio |
| `controller/WithdrawalController.java` | `/api/withdrawals` | POST (create), GET (history), GET (single), GET /export (CSV) |

### Exception Handling
Custom exceptions and global exception handler.

| File | Purpose | Usage |
|------|---------|-------|
| `exception/ResourceNotFoundException.java` | Investor/withdrawal not found | 404 errors |
| `exception/InvalidWithdrawalException.java` | Business rule violations | 400 errors |
| `exception/GlobalExceptionHandler.java` | Centralized error handling | Catches all exceptions, returns consistent format |

### Mapper Classes (DTO Conversion)
Convert entities to DTOs and vice versa.

| File | Purpose | Methods |
|------|---------|---------|
| `mapper/InvestorMapper.java` | Convert Investor ↔ InvestorDTO | toDTO(), toEntity() |
| `mapper/WithdrawalMapper.java` | Convert Withdrawal ↔ WithdrawalDTO | toDTO(), toEntity() |

### Configuration Files

| File | Purpose | Content |
|------|---------|---------|
| `application.yaml` | Spring Boot config | Database URL, JPA settings, H2 console config, server port |
| `data.sql` | Sample data | INSERT statements for 3 investors, 4 portfolios, 7 products |

### Test Classes
Unit and integration tests.

| File | Purpose | Tests |
|------|---------|-------|
| `service/InvestorServiceTest.java` | Test investor service | Get investor, age calculation, retirement eligibility |
| `service/WithdrawalServiceTest.java` | Test withdrawal service | All business rules, validation, error handling |
| `integration/WithdrawalIntegrationTest.java` | Test full flow | End-to-end withdrawal, history, CSV export |

### Build Configuration

| File | Purpose | Content |
|------|---------|---------|
| `pom.xml` | Maven configuration | Dependencies, plugins, repositories |

**Key Dependencies Added:**
- spring-boot-starter-webmvc (REST controllers)
- spring-boot-starter-data-jpa (Database access)
- spring-boot-h2console (Database UI)
- h2 (In-memory database)
- lombok (Annotations: @Data, @NoArgsConstructor)
- jakarta.validation (Input validation)
- spring-boot-starter-validation (Bean validation)

---

## 🎨 Frontend Files (React)

### Service Layer
API communication with backend.

| File | Purpose | Functions |
|------|---------|-----------|
| `services/apiService.js` | Centralized API calls | getInvestor(), getInvestorPortfolio(), createWithdrawal(), getWithdrawalHistory(), exportWithdrawalsCSV() |

### React Components
UI components for different sections.

| File | Purpose | Features |
|------|---------|----------|
| `components/PortfolioDashboard.js` | Display investor portfolio | Shows investor info, portfolios, products, retirement status |
| `components/WithdrawalForm.js` | Withdrawal submission form | Input validation, type selection, error messages, loading state |
| `components/WithdrawalHistory.js` | Withdrawal history table | Sortable table, status badges, CSV export button |

### CSS Styling
Component-specific and global styles.

| File | Purpose | Styles |
|------|---------|--------|
| `App.css` | Global application styling | Header, footer, containers, messages |
| `styles/PortfolioDashboard.css` | Portfolio dashboard styling | Cards, product lists, colors |
| `styles/WithdrawalForm.css` | Withdrawal form styling | Form inputs, buttons, validation messages |
| `styles/WithdrawalHistory.css` | History table styling | Table design, status badges, responsive |

### Main Application

| File | Purpose | Features |
|------|---------|----------|
| `App.js` | Main React component | State management, API calls, component composition |
| `index.js` | React entry point | Renders App to DOM |

### Configuration

| File | Purpose | Content |
|------|---------|---------|
| `package.json` | NPM configuration | Dependencies, scripts, metadata |

**Key Dependencies:**
- react (UI library)
- react-dom (DOM rendering)
- @testing-library/react (Testing)

---

## 📚 Documentation Files

| File | Purpose | Content |
|------|---------|---------|
| `README.md` | Main project readme | Overview, project structure, getting started |
| `BACKEND_README.md` | Backend documentation | API endpoints, business rules, database schema, error handling |
| `INTEGRATION_SUMMARY.md` | Integration overview | Data flows, connections, architecture diagram |
| `SETUP_GUIDE.md` | Step-by-step setup | Installation, verification, testing, troubleshooting |
| `FILE_INVENTORY.md` | This file | Complete file listing and descriptions |

---

## 🗄️ Database Files

### Sample Data
| File | Purpose | Content |
|------|---------|---------|
| `resources/data.sql` | Sample data initialization | INSERT statements for testing |

---

## 🔄 Data Flow Through Files

### Portfolio Display Flow
```
Frontend User Views Portfolio
  ↓
PortfolioDashboard.js (componentDidMount)
  ↓
apiService.getInvestorPortfolio(1)
  ↓
InvestorController.getInvestorPortfolio(1)
  ↓
InvestorService.getInvestorWithPortfolio(1)
  ↓
InvestorRepository.findById(1) → Investor Entity
PortfolioRepository.findByInvestorId(1) → Portfolio Entities
  ↓
InvestorMapper.toDTO() → InvestorDTO
  ↓
API Response → JSON
  ↓
PortfolioDashboard.js renders with styles from PortfolioDashboard.css
```

### Withdrawal Submission Flow
```
Frontend User Submits Form
  ↓
WithdrawalForm.js (validateForm + handleSubmit)
  ↓
apiService.createWithdrawal(data)
  ↓
WithdrawalController.createWithdrawal()
  ↓
WithdrawalService.createWithdrawal()
  ↓
validateWithdrawal() → Check business rules
  ├─ Check age ≥ 65 (if RETIREMENT)
  ├─ Check amount ≤ balance
  └─ Check amount ≤ 90% balance
  ↓
InvestorRepository.findById() + WithdrawalRepository.save()
InvestorRepository.save() (update balance)
  ↓
WithdrawalMapper.toDTO() → WithdrawalDTO
  ↓
API Response (200) or Error (400)
  ↓
WithdrawalForm.js shows success/error message
WithdrawalHistory.js refreshes
```

---

## 📊 Class Diagram

```
Backend:
─────────────────────────────────────────────────────

Entities:
  ┌─────────────┐
  │  Investor   │─────1───┐
  └─────────────┘         │
                          ├───N─┐ Portfolio
                          │     └─1───┐ Product
                          │
                          └───N─ Withdrawal

Services:
  ┌──────────────────────┐
  │ InvestorService      │
  │ + getInvestor()      │
  │ + getPortfolio()     │
  └──────────────────────┘

  ┌──────────────────────┐
  │ WithdrawalService    │
  │ + createWithdrawal() │
  │ + validateRules()    │
  │ + getHistory()       │
  └──────────────────────┘

Controllers:
  ┌──────────────────────┐
  │ InvestorController   │
  │ /api/investors/*     │
  └──────────────────────┘

  ┌──────────────────────┐
  │ WithdrawalController │
  │ /api/withdrawals/*   │
  └──────────────────────┘

Frontend:
─────────────────────────────────────────────────────

App.js
├── PortfolioDashboard
│   └── Uses: apiService.getInvestorPortfolio()
├── WithdrawalForm
│   └── Uses: apiService.createWithdrawal()
└── WithdrawalHistory
    └── Uses: apiService.getWithdrawalHistory()
              apiService.exportWithdrawalsCSV()
```

---

## 🔗 File Dependencies

### Backend Dependencies
```
Entity Classes
  ↑
Repository Classes
  ↑
Service Classes (use Repositories)
  ↑
Controller Classes (use Services)
  ↑
Exception Handler (catches from Services/Controllers)
  ↑
DTO Classes (converted by Services)
  ↑
Mapper Classes (convert between Entity and DTO)
```

### Frontend Dependencies
```
App.js
├── PortfolioDashboard.js
│   ├── apiService.js
│   └── PortfolioDashboard.css
├── WithdrawalForm.js
│   ├── apiService.js
│   └── WithdrawalForm.css
└── WithdrawalHistory.js
    ├── apiService.js
    └── WithdrawalHistory.css
```

### Shared Dependencies
```
Frontend <---HTTP---> Backend
  App.js        apiService.js        Controllers
  Components                         Services
  Styles                             Repositories
                                     Entities
```

---

## 📈 File Statistics

### Backend
- **Entity Classes:** 4 files
- **Repository Classes:** 3 files
- **DTO Classes:** 6 files
- **Service Classes:** 2 files
- **Controller Classes:** 2 files
- **Exception Classes:** 3 files
- **Mapper Classes:** 2 files
- **Test Classes:** 3 files
- **Configuration Files:** 2 files
- **Build Files:** 1 (pom.xml)

**Total Backend Files:** ~28 Java files

### Frontend
- **Components:** 3 files
- **Services:** 1 file
- **Styles:** 4 files
- **Main:** 2 files
- **Configuration:** 1 (package.json)

**Total Frontend Files:** ~11 files

### Documentation
- **README files:** 4 files
- **Data files:** 1 (data.sql)

**Total Documentation Files:** 5 files

**Grand Total:** ~44 files created/modified

---

## 🎯 What Each File Category Does

### Entities (4 files)
- Define database tables
- Establish relationships
- Provide business methods (age, eligibility)

### Repositories (3 files)
- Query database
- CRUD operations
- Custom query methods

### DTOs (6 files)
- API contracts
- Input validation
- Data transformation

### Services (2 files)
- Business logic
- Validation rules
- Data operations

### Controllers (2 files)
- HTTP endpoints
- Request handling
- Response formatting

### Exceptions (3 files)
- Error definition
- Centralized handling
- Consistent responses

### Mappers (2 files)
- Entity ↔ DTO conversion
- Encapsulation

### Tests (3 files)
- Unit testing
- Integration testing
- Validation testing

### Frontend Components (3 files)
- User interface
- User interaction
- Data display

### Frontend Services (1 file)
- API communication
- HTTP requests
- Error handling

### Frontend Styles (4 files)
- Visual design
- Responsive layout
- Component styling

### Documentation (5 files)
- Setup instructions
- API reference
- Integration guide
- File inventory

---

## 🔐 File Organization Principles

### Backend
```
Layered Architecture:
  Controllers (Request/Response)
    ↓
  Services (Business Logic)
    ↓
  Repositories (Data Access)
    ↓
  Entities (Data Model)

Supporting:
  DTOs (Data Contracts)
  Mappers (Conversions)
  Exceptions (Error Handling)
```

### Frontend
```
Component-Based:
  App (Root)
    ├── PortfolioDashboard
    ├── WithdrawalForm
    └── WithdrawalHistory
  
  Shared:
    apiService (API calls)
    CSS files (Styling)
```

---

## ✅ Completeness Checklist

### Data Access Layer
- [x] All entities defined
- [x] All repositories created
- [x] Relationships established
- [x] Sample data provided

### Business Logic Layer
- [x] All services implemented
- [x] Business rules enforced
- [x] Validation logic added
- [x] Error handling included

### API Layer
- [x] All endpoints defined
- [x] Controllers implemented
- [x] Exception handling configured
- [x] CORS enabled

### DTO Layer
- [x] All DTOs created
- [x] Input validation added
- [x] Mappers implemented
- [x] Error DTOs provided

### Frontend Layer
- [x] All components built
- [x] API service created
- [x] Styling completed
- [x] Form validation added

### Testing Layer
- [x] Unit tests written
- [x] Integration tests created
- [x] Business rules tested
- [x] Error cases tested

### Documentation
- [x] README provided
- [x] Backend docs created
- [x] Integration guide written
- [x] Setup guide provided
- [x] File inventory documented

---

## 🎓 Learning Resources

### For Backend Developers
Study in this order:
1. `entity/` - Understand data model
2. `repository/` - Learn data access
3. `service/` - Study business logic
4. `controller/` - See API exposure
5. `exception/` - Learn error handling
6. `test/` - See testing patterns

### For Frontend Developers
Study in this order:
1. `services/apiService.js` - API integration
2. `components/` - React patterns
3. `styles/` - CSS organization
4. `App.js` - State management

### For Full-Stack Developers
1. Understand entity → repository → service → controller flow
2. See how DTOs bridge frontend/backend
3. Trace a request through all layers
4. Review tests for validation logic

---

## 🔄 Connection Points

### Critical Files That Connect Everything

1. **apiService.js** ↔ **Controllers**
   - Frontend API calls connect to backend REST endpoints

2. **Services** ↔ **Repositories**
   - Business logic uses database access

3. **Controllers** ↔ **Services**
   - HTTP endpoints delegate to business logic

4. **DTOs** ↔ **Entities**
   - Mappers convert between data structures

5. **Exceptions** ↔ **GlobalExceptionHandler**
   - Thrown exceptions caught and formatted

6. **React Components** ↔ **CSS Files**
   - Components import their corresponding styles

---

## 📝 File Naming Conventions

### Backend
- **Entities:** `EntityName.java` (e.g., `Investor.java`)
- **Repositories:** `EntityRepository.java` (e.g., `InvestorRepository.java`)
- **Services:** `EntityService.java` (e.g., `InvestorService.java`)
- **Controllers:** `EntityController.java` (e.g., `InvestorController.java`)
- **DTOs:** `EntityDTO.java` (e.g., `InvestorDTO.java`)
- **Tests:** `EntityServiceTest.java`
- **Mappers:** `EntityMapper.java`

### Frontend
- **Components:** `ComponentName.js` (e.g., `PortfolioDashboard.js`)
- **Styles:** `ComponentName.css` (parallel structure)
- **Services:** `serviceName.js` (e.g., `apiService.js`)

---

## 🎉 Summary

**Total Implementation:**
- ✅ 28+ backend Java files
- ✅ 11+ frontend JavaScript files  
- ✅ 5 documentation files
- ✅ 100% functionality complete
- ✅ All business rules implemented
- ✅ Full integration between frontend and backend
- ✅ Comprehensive error handling
- ✅ Unit and integration tests
- ✅ Sample data included
- ✅ Ready to run and deploy

**All files are fully connected and the application is production-ready (with security improvements for production use).**

---

**Document Version:** 1.0.0
**Date:** July 19, 2026
**Status:** Complete ✅
