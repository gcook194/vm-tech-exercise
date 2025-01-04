package com.virginmoney.transactionlog.repository;

import com.virginmoney.transactionlog.model.*;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {

    List<Transaction> findAll();
    List<Transaction> findByCategoryIgnoreCaseOrderByDateDesc(String category);

    @Query(value = "SELECT ROUND(SUM(amount), 2) AS totalOutgoing, category \n" +
            "FROM user_transaction \n" +
            "WHERE LOWER(category) = LOWER(:category)", nativeQuery = true)
    TransactionTotalOutgoing findTotalOutgoingByTransactionCategory(@Param("category") String category);

    @Query(value = "SELECT ROUND(AVG(monthly_total), 2) AS averageMonthlySpend, category \n" +
            "FROM ( \n" +
            "    SELECT \n" +
            "        FORMATDATETIME(date, 'yyyy-MM'), \n" +
            "        SUM(amount) AS monthly_total, \n" +
            "        category \n" +
            "    FROM \n" +
            "        user_transaction \n" +
            "    WHERE \n" +
            "        LOWER(category) = LOWER(:category) \n" +
            "    GROUP BY \n" +
            "        FORMATDATETIME(date, 'yyyy-MM') \n" +
            ")", nativeQuery = true)
    AverageMonthlySpend getAverageMonthlySpendByCategory(@Param("category") String category);

    @Query(value = "SELECT \n" +
            "MAX(amount) AS annualSpend, category  \n" +
            "FROM user_transaction \n" +
            "WHERE LOWER(category) = LOWER(:category) \n" +
            "AND FORMATDATETIME(date, 'yyyy') = :year", nativeQuery = true)
    AnnualSpend findMaxAmountByCategoryAndYear(@Param("category") String category, @Param("year") int year);


    @Query(value = "SELECT \n" +
            "MIN(amount) AS annualSpend, category \n" +
            "FROM user_transaction \n" +
            "WHERE LOWER(category) = LOWER(:category) \n" +
            "AND FORMATDATETIME(date, 'yyyy') = :year", nativeQuery = true)
    AnnualSpend findMinAmountByCategoryAndYear(String category, int year);

    @Query(value = "SELECT \n" +
            "COALESCE(category, 'Uncategorised') as category, ROUND(SUM(amount), 2) as total \n" +
            "FROM user_transaction \n" +
            "GROUP BY COALESCE(category, 'Uncategorised') \n" +
            "ORDER BY total DESC", nativeQuery = true)
    List<TotalOutgoing> getTotalSpendPerCategory();
}
