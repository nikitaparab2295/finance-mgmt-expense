package com.example.expense.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record BudgetResponse(Long id, String ownerId, String category,
                             LocalDate month, BigDecimal amount) {}