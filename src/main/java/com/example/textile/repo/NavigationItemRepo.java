package com.example.textile.repo;

import com.example.textile.entity.NavigationItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface NavigationItemRepo extends JpaRepository<NavigationItem, Long> {

    @Query("SELECT n FROM NavigationItem n WHERE n.parent IS NULL AND n.isActive = true ORDER BY n.orderIndex")
    List<NavigationItem> findRootItems();

    @Query("SELECT n FROM NavigationItem n WHERE n.isActive = true ORDER BY n.parent,n.orderIndex")
    List<NavigationItem>  findAllActiveItems();
}
