package com.example.textile.controller;

import com.example.textile.entity.NavigationItem;
import com.example.textile.service.NavigationService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class NavigationController {

    private final NavigationService navigationService;
    public NavigationController(NavigationService navigationService) {
        this.navigationService = navigationService;
    }

    @GetMapping("/navigations")
    public ResponseEntity<List<NavigationItem>> getNavigation() {
        List<NavigationItem> navigationStructure = navigationService.getNavigationStructure();
        return ResponseEntity.ok(navigationStructure);
    }

    @PostMapping("/navigations")
    public ResponseEntity<NavigationItem> saveNavigation(@RequestBody NavigationItem navigationItems) {
        NavigationItem saveNavigationItems = navigationService.saveNavigation(navigationItems);
        return ResponseEntity.ok(saveNavigationItems);
    }
}
