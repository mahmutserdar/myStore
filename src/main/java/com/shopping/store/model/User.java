package com.shopping.store.model;

import org.apache.tomcat.util.net.openssl.ciphers.Authentication;

import javax.persistence.*;

@Entity
@Table(name = "app_users")
public class User {
    public User(String name, String password) {
        this.username = name;
        this.password = password;
        this.basket = new Basket();
    }

    public User() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;
    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "basket_id")
    private Basket basket;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Basket getBasket() {
        return basket;
    }

    public boolean isEnabled(){
        return true;
    }

    public void setBasket(Basket basket) {
        this.basket = basket;
    }

}