package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


public class hashTest {

    @Test
    void makingHash(){
        System.out.println(new BCryptPasswordEncoder().encode("1234"));
    }
}
