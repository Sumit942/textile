package com.example.textile.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@ToString(exclude = {"children"})
public class NavigationItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    @JsonBackReference
    private NavigationItem parent;

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @OrderBy("orderIndex")
    @JsonManagedReference
    private List<NavigationItem> children;

    @NotNull(message = "Please specify the display name")
    private String name;
    private String url;
    private String icon;
    private Integer orderIndex;
    private Boolean isActive;
}
