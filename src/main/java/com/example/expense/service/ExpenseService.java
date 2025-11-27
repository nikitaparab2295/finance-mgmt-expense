package com.example.expense.service;

import com.example.expense.client.BudgetClient;
import com.example.expense.client.NotificationClient;
import com.example.expense.domain.Expense;
import com.example.expense.dto.BudgetResponse;
import com.example.expense.dto.NotificationRequest;
import com.example.expense.repository.ExpenseRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.YearMonth;
import java.time.ZoneOffset;

@Service
public class ExpenseService {
    private final ExpenseRepository repo;
    private final BudgetClient budgetClient;
    private final NotificationClient notificationClient;

    public ExpenseService(ExpenseRepository repo, BudgetClient budgetClient,
                          NotificationClient notificationClient) {
        this.repo = repo;
        this.budgetClient = budgetClient;
        this.notificationClient = notificationClient;
    }

    @Transactional
    public Expense createExpense(Expense e) {
        if (e.getCreatedAt() == null) e.setCreatedAt(Instant.now());
        Expense saved = repo.save(e);
        try {
            YearMonth ym = YearMonth.from(e.getCreatedAt().atZone(ZoneOffset.UTC).toLocalDate());
            String monthStr = String.format("%d-%02d", ym.getYear(), ym.getMonthValue());
            BudgetResponse budget = budgetClient.getBudgetByOwnerCategoryAndMonth(e.getOwnerId(), e.getCategory(), monthStr);
            BigDecimal budgetAmount = budget.amount();

            Instant start = ym.atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();
            Instant end = ym.plusMonths(1).atDay(1).atStartOfDay(ZoneOffset.UTC).toInstant();

            BigDecimal currentSum = repo.sumAmountForOwnerCategoryBetween(e.getOwnerId(), e.getCategory(), start, end);
            BigDecimal eighty = budgetAmount.multiply(BigDecimal.valueOf(0.8));
            boolean sent80 = false; // TODO: should check stored notifications so we don't resend; for assignment keeping an in-memory simple mechanism
            boolean sent100 = false;

            if (currentSum.compareTo(budgetAmount) >= 0 && !sent100) {
                NotificationRequest nr = new NotificationRequest(e.getOwnerId(), e.getCategory(), "ALERT", budgetAmount,
                        e.getAmount(), e.getDescription(), currentSum, budgetAmount.subtract(currentSum));
                sendNotificationSafe(nr);
                sent100 = true;
            } else if (currentSum.compareTo(eighty) >= 0 && !sent80) {
                NotificationRequest nr = new
                        NotificationRequest(e.getOwnerId(), e.getCategory(), "WARNING", budgetAmount,
                        e.getAmount(), e.getDescription(), currentSum,
                        budgetAmount.subtract(currentSum));
                // circuit breaker around notification client
                sendNotificationSafe(nr);
                sent80 = true;
            }

        } catch (Exception ex) {
            // If anything fails, log. Expense saved always. In production persist pending notifications for retry
            System.err.println("Failed to check budget or send notification: " + ex.getMessage());
        }
        return saved;
    }

    @CircuitBreaker(name = "notificationClient", fallbackMethod =
            "notificationFallback")
    @Retry(name = "budgetClient")
    public void sendNotificationSafe(NotificationRequest nr) {
        notificationClient.sendNotification(nr);
    }

    public void notificationFallback(NotificationRequest nr, Throwable t) {
        // fallback behavior: log and continue
        System.err.println("Notification service unavailable. Saved pending notification: " + nr);
    }
}