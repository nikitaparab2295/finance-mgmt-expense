package com.example.expense.client;

import com.example.expense.dto.NotificationRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "notificationClient", url = "http://localhost:8083")
public interface NotificationClient {
    @PostMapping("/api/notifications")
    void sendNotification(@RequestBody NotificationRequest req);
}
