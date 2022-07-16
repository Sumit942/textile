package com.example.textile.entity;

import com.example.textile.enums.TransportModeEnum;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class TransportMode {
    @Id
    private Long id;

    @Column(length = 5, nullable = false, unique = true)
    private String mode = TransportModeEnum.ROAD.getTransportMode();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
