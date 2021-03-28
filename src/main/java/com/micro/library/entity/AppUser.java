package com.micro.library.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import java.util.List;

@Setter
@Getter
@ToString
@Entity
public class AppUser {

    @Id
    private String username;
    private String email;
    private String phone;
    private String password;
    @Transient
    private String confirmPassword;
    private String role;
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "userinfo", referencedColumnName = "username")
    private List<BookBorrow> bookBorrowList;

    public AppUser() {
    }

    public AppUser(String username, String email, String phone, String password, String role) {
        this.username = username;
        this.email = email;
        this.phone = phone;
        this.password = password;
        this.role = role;
    }
    public AppUser(String username, String email, String phone) {
        this.username = username;
        this.email = email;
        this.phone = phone;
    }


}
