package com.example.textile.entity;

import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.util.Date;

@Data
@Entity
@Table
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String method;

    @Column(length = 1000)
    private String uri;

    private String queryString;

    private Integer status;

    @Lob
    private String requestBody;

    @Lob
    private String responseBody;

    private String userName;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date timestamp;
}
