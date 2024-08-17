package com.example.facebook.controller;


import com.example.facebook.entity.Notification;
import com.example.facebook.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class NotificationController {
    private final NotificationService notificationService;

    @GetMapping("/notifications")
    public ResponseEntity<List<Notification>> getNotifications(@RequestParam("page") Integer page,
                                              @RequestParam("size") Integer size) {
        page = page < 0 ? 0 : page-1;
        size = size <= 0 ? 5 : size;
        List<Notification> notifications = notificationService.getNotificationsForAuthUserPaginate(page, size);
        return new ResponseEntity<>(notifications, HttpStatus.OK);
    }

    @PostMapping("/notifications/mark-seen")
    @ResponseStatus(HttpStatus.OK)
    public void markAllSeen() {
        notificationService.markAllSeen();
    }

    @PostMapping("/notifications/mark-read")
    @ResponseStatus(HttpStatus.OK)
    public void markAllRead() {
        notificationService.markAllRead();
    }
}
