-- Sample Investors
INSERT INTO investors (first_name, last_name, email, date_of_birth, phone_number, total_balance) 
VALUES ('John', 'Doe', 'john.doe@example.com', DATE '1955-05-15', '0123456789', 50000.00);

INSERT INTO investors (first_name, last_name, email, date_of_birth, phone_number, total_balance) 
VALUES ('Jane', 'Smith', 'jane.smith@example.com', DATE '1990-03-22', '0987654321', 75000.00);

INSERT INTO investors (first_name, last_name, email, date_of_birth, phone_number, total_balance) 
VALUES ('Robert', 'Johnson', 'robert.j@example.com', DATE '1945-07-10', '0555444333', 100000.00);

-- Sample Portfolios for Investor 1
INSERT INTO portfolios (name, balance, investor_id) 
VALUES ('Retirement Portfolio', 30000.00, 1);

INSERT INTO portfolios (name, balance, investor_id) 
VALUES ('Growth Portfolio', 20000.00, 1);

-- Sample Portfolios for Investor 2
INSERT INTO portfolios (name, balance, investor_id) 
VALUES ('Balanced Portfolio', 75000.00, 2);

-- Sample Portfolios for Investor 3
INSERT INTO portfolios (name, balance, investor_id) 
VALUES ('Conservative Portfolio', 100000.00, 3);

-- Sample Products for Portfolio 1 (Retirement)
INSERT INTO products (name, type, product_value, unit_price, portfolio_id) 
VALUES ('Dividend Stock Fund', 'EQUITY', 15000.00, 150.00, 1);

INSERT INTO products (name, type, product_value, unit_price, portfolio_id) 
VALUES ('Bonds Index Fund', 'BONDS', 15000.00, 100.00, 1);

-- Sample Products for Portfolio 2 (Growth)
INSERT INTO products (name, type, product_value, unit_price, portfolio_id) 
VALUES ('Tech Growth Fund', 'EQUITY', 20000.00, 250.00, 2);

-- Sample Products for Portfolio 3 (Balanced)
INSERT INTO products (name, type, product_value, unit_price, portfolio_id) 
VALUES ('Global Equity Fund', 'EQUITY', 40000.00, 200.00, 3);

INSERT INTO products (name, type, product_value, unit_price, portfolio_id) 
VALUES ('Fixed Income Fund', 'BONDS', 35000.00, 175.00, 3);

-- Sample Products for Portfolio 4 (Conservative)
INSERT INTO products (name, type, product_value, unit_price, portfolio_id) 
VALUES ('Government Bonds', 'BONDS', 60000.00, 120.00, 4);

INSERT INTO products (name, type, product_value, unit_price, portfolio_id) 
VALUES ('Money Market Fund', 'MONEY_MARKET', 40000.00, 100.00, 4);
