package com.example.textile.entity;

import com.example.textile.enums.UserProfileType;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

@Entity
@Table(name = "USER_PROFILE")
public class UserProfile implements Serializable {
    @Id
    private Long id;
    @Column(length = 15, unique = true, nullable = false)
    private String type = UserProfileType.USER.getUserProfileType();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "UserProfile{" +
                "id=" + id +
                ", type='" + type + '\'' +
                '}';
    }
}
