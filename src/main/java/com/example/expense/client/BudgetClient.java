package com.example.expense.client;

import com.example.expense.dto.BudgetResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "budgetClient", url = "http://localhost:8081")
public interface BudgetClient {
    @GetMapping("/api/budgets/search")
    BudgetResponse getBudgetByOwnerCategoryAndMonth(@RequestParam String ownerId, @RequestParam String category, @RequestParam String month);
}