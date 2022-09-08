package com.example.applfttest.domain;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;

@Entity
@Getter
@Setter
public class User {
    @Id
    Long id;
    String username;
    String email;
    @Type(type = "uuid-char")
    UUID token;
}
