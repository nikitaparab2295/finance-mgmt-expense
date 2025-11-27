package com.example.expense.web;

import com.example.expense.domain.Expense;
import com.example.expense.repository.ExpenseRepository;
import com.example.expense.service.ExpenseService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.net.URI;
import java.math.BigDecimal;
import java.time.Instant;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {
    private final ExpenseService service;
    private final ExpenseRepository repo;

    public ExpenseController(ExpenseService service, ExpenseRepository repo){
        this.service = service; this.repo = repo; }

    @PostMapping
    public ResponseEntity<Expense> create(@Valid @RequestBody
                                          ExpenseCreateRequest req){
        Expense e = Expense.builder().ownerId(req.ownerId()).category(req.category()).amount(req.amount())
                        .description(req.description()).createdAt(req.createdAt() == null ? Instant.now() : req.createdAt()).build();
        Expense saved = service.createExpense(e);
        return ResponseEntity.created(URI.create("/api/expenses/" +
                saved.getId())).body(saved);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Expense> get(@PathVariable Long id){
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    public record ExpenseCreateRequest(@NotBlank String ownerId, @NotBlank String category, String description, @DecimalMin("0.01") BigDecimal amount, Instant createdAt) {}
}
