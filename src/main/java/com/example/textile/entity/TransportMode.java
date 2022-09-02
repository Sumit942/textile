package com.example.textile.entity;

import com.example.textile.enums.TransportModeEnum;

import javax.persistence.*;

@Entity
public class TransportMode {
    private Long id;

    private String mode = TransportModeEnum.ROAD.getTransportMode();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Column(length = 5, nullable = false, unique = true)
    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }

    @Override
    public String toString() {
        return "TransportMode{" +
                "id=" + id +
                ", mode='" + mode + '\'' +
                '}';
    }
}
