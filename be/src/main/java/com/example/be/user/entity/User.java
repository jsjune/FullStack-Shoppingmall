package com.example.be.user.entity;

import com.example.be.product.entity.ImageDao;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "USERS")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private String name;
    private String password;

    @Enumerated(EnumType.STRING)
    private UserEnum role;
    private String profileImg;

    @ElementCollection
    @CollectionTable(name = "USER_CARTS",
            joinColumns = @JoinColumn(name = "USERS_ID"))
    private List<Cart> carts = new ArrayList<>();

    @Builder
    public User(Long id, String email, String name, String password, UserEnum role, String profileImg) {
        this.id = id;
        this.email = email;
        this.name = name;
        this.password = password;
        this.role = role;
        this.profileImg = profileImg;
    }

}
