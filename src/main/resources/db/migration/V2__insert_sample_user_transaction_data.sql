INSERT INTO user_transaction (id, amount, date, type, vendor, category)
VALUES
    (1, 10.40, '2020-11-01', 'CARD', 'Morrisons', 'Groceries'),
    (2, 600.00, '2020-10-28', 'DIRECT_DEBIT', 'CYBG', 'MyMonthlyDD'),
    (3, 40.00, '2020-10-28', 'DIRECT_DEBIT', 'PureGym', 'MyMonthlyDD'),
    (4, 5.99, '2020-10-03', 'CARD', 'M&S', 'Groceries'),
    (5, 5.99, '2020-10-02', 'CARD', 'M&S', 'Groceries'),
    (6, 5.99, '2020-10-01', 'CARD', 'M&S', 'Groceries'),
    (7, 10, '2020-09-30', 'internet', 'McMillan', null);

-- Obviously wouldn't have this in a production migration but it is useful for debugging the in-memory database
SELECT * FROM USER_TRANSACTION;