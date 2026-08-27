package com.example.courseenrollmentsystem.Entity;

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
    private boolean enabled = true;
    private boolean AccountNotBlocked = true;

}
