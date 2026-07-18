# Enviro365Assessment - Dick Khosa

## 📌 Project Overview
This repository contains my submission for the **Junior Full-Stack Developer Assessment (Enviro365 Investments, 2026)**.  
It includes a **Spring Boot backend** and a **frontend (Angular/React)** to manage investor portfolios, withdrawals, and CSV reports.

---

## ⚙️ Backend (Spring Boot)
- Retrieve investor portfolio (details + products)
- Create withdrawal notices with balance calculations
- Export CSV statements with filtering
- Business rules:
  - Retirement withdrawals only allowed if age > 65
  - Withdrawal must not exceed balance
  - Withdrawal must not exceed 90% of balance

---

## 🎨 Frontend
- Portfolio dashboard
- Withdrawal form
- Withdrawal history table
- CSV download button
- Connected to backend APIs

---

## 🛠 Advanced Features
- Global exception handling
- DTO layer
- Input validation
- Unit tests
- UI validation

---

## 🚀 Setup Instructions
### Backend
```bash
cd backend
mvn spring-boot:run
