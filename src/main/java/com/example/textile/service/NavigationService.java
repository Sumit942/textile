package com.example.textile.service;

import com.example.textile.entity.NavigationItem;

import java.util.List;

public interface NavigationService {

    List<NavigationItem> getNavigationStructure();

    List<NavigationItem> saveNavigations(List<NavigationItem> navigationItems);

    NavigationItem saveNavigation(NavigationItem navigationItems);
}
