package com.example.courseenrollmentsystem.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long Userid;
    @Column(unique = true, nullable = false)
    private String Username;
    @Column(nullable = false)
    private String Password;
    @Column(unique = true, nullable = false)
    private String Email;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private String Role;
    private boolean enabled = true;
    private boolean AccountNotBlocked = true;


}
