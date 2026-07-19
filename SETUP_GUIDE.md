# 🚀 Enviro365 - Complete Setup & Run Guide

## System Requirements
- **Java:** 17 or higher
- **Node.js:** 16 or higher
- **npm:** 8 or higher
- **Maven:** 3.8 or higher
- **Ports:** 8080 (backend), 3000 (frontend)

---

## 🔧 Step-by-Step Setup

### Step 1: Backend Setup

#### 1.1 Navigate to Backend Directory
```bash
cd backend/Enviro365Backend
```

#### 1.2 Build with Maven
```bash
mvn clean install
```
This will:
- Download all dependencies
- Compile Java source code
- Run unit tests
- Package the application

#### 1.3 Start the Backend Server
```bash
mvn spring-boot:run
```

**Expected Output:**
```
2026-07-19 10:30:45.123  INFO 12345 --- Tomcat started on port(s): 8080 (http)
2026-07-19 10:30:45.456  INFO 12345 --- Started Enviro365BackendApplication
```

**✓ Backend is now running at:** `http://localhost:8080`

#### 1.4 Verify Backend is Running
Open in browser:
- H2 Console: `http://localhost:8080/h2-console`
  - JDBC URL: `jdbc:h2:mem:enviro365db`
  - Username: `sa`
  - Password: (leave blank)
  - Click "Connect"

---

### Step 2: Frontend Setup

#### 2.1 Open New Terminal/Command Prompt
(Keep backend terminal running)

#### 2.2 Navigate to Frontend Directory
```bash
cd frontend/enviro365-frontend
```

#### 2.3 Install Dependencies
```bash
npm install
```
This will install:
- React and ReactDOM
- React Scripts
- Testing libraries
- All other dependencies listed in package.json

#### 2.4 Start the Frontend Development Server
```bash
npm start
```

**Expected Output:**
```
Compiled successfully!

You can now view enviro365-frontend in the browser.

  Local:            http://localhost:3000
```

**✓ Frontend will automatically open at:** `http://localhost:3000`

---

## ✅ Verification Checklist

### Backend Verification
```
[ ] mvn clean install succeeds
[ ] mvn spring-boot:run starts without errors
[ ] Port 8080 is accessible
[ ] H2 Console loads at http://localhost:8080/h2-console
[ ] Sample data is loaded (check H2 console)
```

### Frontend Verification
```
[ ] npm install completes without errors
[ ] npm start opens browser to http://localhost:3000
[ ] Page loads without JavaScript errors
[ ] Can see "Enviro365 - Investor Portfolio Management"
```

### API Verification
From browser or curl:
```bash
# Test GET request
curl http://localhost:8080/api/investors/1

# Should return investor data
```

---

## 📋 Testing the Application

### Test 1: View Portfolio
1. Open `http://localhost:3000`
2. You should see:
   - Investor: John Doe
   - Age: 69
   - Retirement Eligible: Yes
   - Total Balance: R50,000.00
   - Two portfolios with products listed

### Test 2: Submit Withdrawal (Success)
1. Scroll to "Submit Withdrawal" form
2. Enter:
   - Amount: **20000**
   - Type: **Regular Withdrawal**
3. Click "Submit Withdrawal"
4. Should see: ✓ Withdrawal approved successfully!
5. Check history table - new withdrawal should appear

### Test 3: Submit Withdrawal (Fail - 90% Limit)
1. In "Submit Withdrawal" form
2. Enter:
   - Amount: **95000** (more than 90%)
   - Type: **Regular Withdrawal**
3. Click "Submit Withdrawal"
4. Should see error: "Cannot exceed 90% of balance"

### Test 4: CSV Export
1. Click "📥 Export to CSV" button
2. CSV file should download: `withdrawals_1_[timestamp].csv`
3. Open file - should contain withdrawal records

---

## 🧪 Running Tests

### Backend Unit Tests
```bash
cd backend/Enviro365Backend
mvn test
```

**Test Results Should Show:**
```
InvestorServiceTest ... OK
WithdrawalServiceTest ... OK
WithdrawalIntegrationTest ... OK

Tests run: 13, Failures: 0, Errors: 0
```

### Frontend Tests (Optional)
```bash
cd frontend/enviro365-frontend
npm test
```

---

## 🔍 Debugging

### Backend Issues

#### Port 8080 Already in Use
```bash
# Find process using port 8080
netstat -ano | findstr :8080

# Kill the process (replace PID)
taskkill /PID <PID> /F

# Or change port in application.yaml
```

#### Maven Build Fails
```bash
# Clear cache and rebuild
mvn clean
mvn install -U
```

#### Database Connection Error
```bash
# Check H2 driver in pom.xml
# Or restart with fresh database
mvn spring-boot:run
```

### Frontend Issues

#### Port 3000 Already in Use
```bash
# Kill process on port 3000
lsof -ti:3000 | xargs kill -9

# Or set different port
PORT=3001 npm start
```

#### npm install fails
```bash
# Clear npm cache
npm cache clean --force

# Remove node_modules and lock file
rm -rf node_modules package-lock.json

# Reinstall
npm install
```

#### API Connection Error
```
Check that:
- Backend is running on port 8080
- CORS is enabled (it is by default)
- Network tab in DevTools shows requests
```

---

## 📡 API Endpoints Reference

### All Available Endpoints

#### Investors
```
GET /api/investors/{id}
  → Get investor details

GET /api/investors/{id}/portfolio
  → Get investor with portfolios and products
```

#### Withdrawals
```
POST /api/withdrawals
  → Create withdrawal
  → Body: { investorId, amount, type }

GET /api/withdrawals?investorId={id}
  → Get withdrawal history

GET /api/withdrawals/{id}
  → Get single withdrawal

GET /api/withdrawals/export?investorId={id}
  → Download CSV
```

#### H2 Database Console
```
GET /h2-console
  → Web-based database viewer
  → JDBC: jdbc:h2:mem:enviro365db
```

---

## 🎯 Sample Data

The application loads with these investors:

**Investor 1: John Doe**
- Age: 69
- Retirement Eligible: ✓ YES
- Balance: R50,000
- Portfolios: 2 (Retirement, Growth)

**Investor 2: Jane Smith**
- Age: 36
- Retirement Eligible: ✗ NO
- Balance: R75,000
- Portfolios: 1 (Balanced)

**Investor 3: Robert Johnson**
- Age: 81
- Retirement Eligible: ✓ YES
- Balance: R100,000
- Portfolios: 1 (Conservative)

To test different investors, modify App.js:
```javascript
const INVESTOR_ID = 1; // Change to 2 or 3
```

---

## 🔄 Project Structure

```
Enviro365Assessment-Junior-DickKhosa/
│
├── backend/
│   └── Enviro365Backend/
│       ├── src/
│       │   ├── main/java/com/enviro/assessment/junior/dickkhosa/Enviro365Backend/
│       │   │   ├── entity/          # JPA entities
│       │   │   ├── dto/             # DTOs
│       │   │   ├── repository/      # Repositories
│       │   │   ├── service/         # Services
│       │   │   ├── controller/      # Controllers
│       │   │   ├── exception/       # Exception handlers
│       │   │   └── mapper/          # Mappers
│       │   ├── main/resources/
│       │   │   ├── application.yaml # Config
│       │   │   └── data.sql         # Sample data
│       │   └── test/java/           # Tests
│       └── pom.xml
│
├── frontend/
│   └── enviro365-frontend/
│       ├── src/
│       │   ├── components/          # React components
│       │   ├── services/            # API service
│       │   ├── styles/              # CSS files
│       │   ├── App.js               # Main app
│       │   └── index.js             # Entry point
│       └── package.json
│
├── README.md                         # Main readme
├── BACKEND_README.md                 # Backend docs
└── INTEGRATION_SUMMARY.md            # Integration docs
```

---

## 🎓 What Each Component Does

### Backend Components

**Entity Models**
- Define database structure
- Handle relationships between tables
- Calculate derived values (age, eligibility)

**Services**
- Implement business logic
- Validate withdrawals (age, balance, 90% limit)
- Manage data transformations

**Controllers**
- Handle HTTP requests
- Call services
- Return responses to frontend

**Repositories**
- Execute database queries
- Handle CRUD operations
- Spring Data JPA handles implementation

**Exception Handlers**
- Catch errors
- Return consistent error format
- Show helpful messages to frontend

### Frontend Components

**PortfolioDashboard**
- Fetches investor portfolio from backend
- Displays in formatted cards
- Shows retirement eligibility

**WithdrawalForm**
- Validates user input (client-side)
- Submits to backend API
- Shows success/error messages

**WithdrawalHistory**
- Fetches withdrawal records
- Displays in sortable table
- Provides CSV export

**API Service**
- Centralized API calls
- Error handling
- File download management

---

## 🛠️ Troubleshooting Flowchart

```
Application not loading?
├─ Backend not starting?
│  ├─ Check Java version (17+)
│  ├─ Check port 8080 availability
│  └─ Check Maven installation
├─ Frontend not starting?
│  ├─ Check Node.js version (16+)
│  ├─ Check port 3000 availability
│  └─ Try: npm cache clean --force
└─ APIs not responding?
   ├─ Check CORS configuration
   ├─ Verify backend is running
   └─ Check browser console for errors

Withdrawal failing?
├─ Check age for retirement type
├─ Verify balance is sufficient
├─ Ensure amount ≤ 90% of balance
└─ Check error message from backend

Tests failing?
├─ Run: mvn clean install
├─ Check database initialization
├─ Verify all dependencies loaded
└─ Try: mvn test -X (verbose)
```

---

## 📚 Additional Resources

### Documentation Files
- `README.md` - Project overview
- `BACKEND_README.md` - Complete backend documentation
- `INTEGRATION_SUMMARY.md` - How everything connects

### Code Comments
- All Java files have detailed comments
- All React components have clear explanations
- CSS files are organized by component

### Learning Resources
- Spring Boot Docs: https://spring.io/projects/spring-boot
- React Docs: https://react.dev
- H2 Database: https://www.h2database.com

---

## ✨ Tips & Tricks

### Test Different Scenarios
```
# Test investor #1 (eligible for retirement)
Change INVESTOR_ID = 1 in App.js

# Test investor #2 (NOT eligible)
Change INVESTOR_ID = 2 in App.js

# Test investor #3 (eligible, higher balance)
Change INVESTOR_ID = 3 in App.js
```

### View Database Content
```
1. Go to http://localhost:8080/h2-console
2. Login with: jdbc:h2:mem:enviro365db
3. Run SQL:
   SELECT * FROM investors;
   SELECT * FROM withdrawals;
   SELECT * FROM portfolios;
```

### View API Responses
```
In browser DevTools:
1. F12 to open DevTools
2. Go to Network tab
3. Perform actions
4. Click on requests to see responses
5. Check Console for any errors
```

### Modify Sample Data
```
Edit: backend/Enviro365Backend/src/main/resources/data.sql
Then restart backend (data reloads on startup)
```

---

## 🎉 Success Indicators

✓ **Backend Running:**
- Console shows "Tomcat started on port(s): 8080"
- H2 console accessible at port 8080
- API endpoints respond with data

✓ **Frontend Running:**
- Browser automatically opens to localhost:3000
- Portfolio data is displayed
- No console errors in DevTools

✓ **Integration Working:**
- Can view portfolio
- Can submit withdrawal (success and fail cases)
- Can export CSV
- Error messages appear correctly

---

## 📞 Common Questions

**Q: Can I run both on the same machine?**
A: Yes! Keep backend terminal open and use new terminal for frontend.

**Q: What if I want to change the port?**
A: 
- Backend: Edit `application.yaml` (server.port)
- Frontend: Use `PORT=3001 npm start`

**Q: How do I reset the database?**
A: Restart backend (H2 in-memory DB resets automatically)

**Q: Can I use a different database?**
A: Yes, but would need to configure in `application.yaml`

**Q: What if tests fail?**
A: Run `mvn clean install` first, then `mvn test`

---

## 🏁 Next Steps

After successful setup:

1. **Explore the Code**
   - Read entity classes to understand data structure
   - Check service layer for business logic
   - Review React components for UI patterns

2. **Experiment**
   - Try different withdrawal amounts
   - Test age restrictions
   - Export and view CSV files

3. **Customize**
   - Add more investors to data.sql
   - Modify styling in CSS files
   - Add new API endpoints

4. **Deploy** (Future)
   - Build frontend: `npm run build`
   - Build backend: `mvn clean package`
   - Deploy to cloud platform

---

## ✅ Final Checklist

Before considering setup complete:
```
[ ] Backend runs without errors
[ ] Frontend runs without errors
[ ] Can view investor portfolio
[ ] Can submit successful withdrawal
[ ] Can see withdrawal history
[ ] Can export to CSV
[ ] Error handling works (test with invalid data)
[ ] Tests pass (mvn test)
[ ] No console errors in DevTools
[ ] Database contains sample data
```

---

**Status:** Ready to use! 🚀
**Last Updated:** July 19, 2026
**Version:** 1.0.0
