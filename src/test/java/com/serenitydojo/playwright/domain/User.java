package com.serenitydojo.playwright.domain;

import net.datafaker.Faker;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;


public record User(String first_name,
                   String last_name,
                   String phone,
                   String dob,
                   String email,
                   Address address,
                   String password) {

    public static User randomUser(){
        Faker fake = new Faker();

        int year = fake.number().numberBetween(1970, 2000);
        int month = fake.number().numberBetween(1,12);
        int day = fake.number().numberBetween(1,28);

        LocalDate date = LocalDate.of(year, month, day);

        String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));

        return new User(fake.name().firstName(),
                fake.name().lastName(),
                fake.phoneNumber().phoneNumber(),
                formattedDate,
                fake.internet().emailAddress(),
                new Address(fake.address().streetAddress(), fake.address().city(), fake.address().state(),
                        fake.address().country(), fake.address().postcode()),
                "Az123!&xyz"
                );
    }

    public Object withPassword(String password){
        return new User(first_name,
                last_name,
                phone,
                dob,
                email,
                address,
                password);
    }
}
