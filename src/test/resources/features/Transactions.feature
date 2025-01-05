Feature: API can return and filter transaction data

  Scenario: Call the REST API to get all transactions
    When the client calls endpoint "/api/transactions"
    Then response status code is 200
    And response body should contain all transactions

  Scenario: Call the REST API to get all transactions with a category of Groceries
    When the client calls endpoint "/api/transactions/Groceries"
    Then response status code is 200
    And response body should contain only transactions with a category of "Groceries"