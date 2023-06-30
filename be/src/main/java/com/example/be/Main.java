package com.example.be;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class Main {
    public static void main(String[] args) {
        String format = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy/MM-dd"));
        System.out.println(format);
    }
}
