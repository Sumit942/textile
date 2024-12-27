package com.example.textile.serviceImpl;

import com.example.textile.entity.NavigationItem;
import com.example.textile.repo.NavigationItemRepo;
import com.example.textile.service.NavigationService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NavigationServiceImpl implements NavigationService {

    NavigationItemRepo navigationItemRepo;

    public NavigationServiceImpl(NavigationItemRepo navigationItemRepo) {
        this.navigationItemRepo = navigationItemRepo;
    }
    @Override
//    @Cacheable(value = Constants.NAVIGATION_CACHE)
    public List<NavigationItem> getNavigationStructure() {
        // Fetch root items (those with no parent)
        List<NavigationItem> rootItems = navigationItemRepo.findRootItems();

        // Load children recursively
        for (NavigationItem rootItem : rootItems) {
            loadChildren(rootItem);
        }
        return rootItems;
    }

    @Override
    public NavigationItem saveNavigation(NavigationItem navigationItem) {
        return navigationItemRepo.save(navigationItem);
    }

    @Override
//    @CacheEvict(value = Constants.NAVIGATION_CACHE, allEntries = true)
//    @CacheEvict(value = Constants.NAVIGATION_CACHE, key = "#navigationItems.parentId") //TODO: optimization - if high traffic partial cache evict
//    @CacheEvict(value = "navigationCache", allEntries = true) //TODO: evict cache on update only
    public List<NavigationItem> saveNavigations(List<NavigationItem> navigationItems) {
        return navigationItemRepo.saveAll(navigationItems);
//        CompletableFuture.runAsync(() -> getNavigationStructure());//TODO: evict cache on update only
    }

    private void loadChildren(NavigationItem parent) {
        List<NavigationItem> children = parent.getChildren();
        for (NavigationItem child : children) {
            loadChildren(child); // Recursive call to load children of children
        }
    }
}
