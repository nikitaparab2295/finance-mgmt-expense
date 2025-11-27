package com.example.expense.web;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ExpenseControllerTest {
    @Autowired
    MockMvc mvc;

    @Test
    void createExpense() throws Exception {
        String body = "{\"ownerId\":\"user1\",\"category\":\"Groceries\",\"description\":\"milk\",\"amount\":200}";
        mvc.perform(post("/api/ expenses").contentType(MediaType.APPLICATION_JSON).content(body))
                        .andExpect(status().isCreated());
    }
}
