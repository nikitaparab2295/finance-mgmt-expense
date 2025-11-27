package com.example.expense.repository;

import com.example.expense.domain.Expense;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {
    List<Expense> findByOwnerIdAndCategoryAndCreatedAtBetween(String ownerId,
                                                              String category, Instant start, Instant end);
    @Query("SELECT COALESCE(SUM(e.amount), 0) FROM Expense e WHERE e.ownerId= :ownerId AND e.category = :category AND e.createdAt >= :start AND e.createdAt < :end")
    BigDecimal sumAmountForOwnerCategoryBetween(@Param("ownerId") String ownerId, @Param("category") String category, @Param("start") Instant start, @Param("end") Instant end);
}