package com.example.expense.dto;

import java.math.BigDecimal;

public record NotificationRequest(String ownerId, String category, String level, BigDecimal budgetAmount,
                                  BigDecimal expenseAmount, String expenseDescription, BigDecimal totalSpent, BigDecimal remaining) {}
