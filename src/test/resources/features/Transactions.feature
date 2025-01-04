Feature: Account has no meter readings

  Scenario: Call the REST API to get all transactions
    When the client calls endpoint "/api/transactions"
    Then response status code is 200
    And response body should contain all transactions

  Scenario: Call the REST API to get all transactions with a category of Groceries
    When the client calls endpoint "/api/transactions/Groceries"
    Then response status code is 200
    And response body should contain only transactions with a category of "Groceries"